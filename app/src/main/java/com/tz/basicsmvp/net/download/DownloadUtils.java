package com.tz.basicsmvp.net.download;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Evelio Tarazona <evelio@twitvid.com>
 * @author Cristian Castiblanco <cristian@elhacker.net>
 * @version 1.1
 */
public class DownloadUtils {
    private static boolean alreadyCheckedInternetPermission = false;
    /**
     * Amount of maximum allowed redirects number by:
     * http://www.google.com/support
     * /forum/p/Webmasters/thread?tid=3760b68fb305088a&hl=en
     */
    private static final int MAX_REDIRECTS = 5;

    /**
     * Non instance constants class
     */
    private DownloadUtils() {
    }

    /**
     * Read it's name
     */
    private static final int DEFAULT_BUFFER_SIZE = 4096; // 4 KiB

    /**
     * Finds out the cache directory
     *
     * @param context Context to use
     * @return A File where means a directory where cache files should be
     * written
     */
    public static File getCacheDirectory(Context context) {
        File cacheDir = context.getCacheDir();
        if (!cacheDir.exists() && cacheDir.mkdirs()) {
            Log.d(DownloadUtils.class.getSimpleName(),
                    "Cache directory created");
        }
        return cacheDir;
    }

    /**
     * Download a file at <code>fromUrl</code> to a file specified by
     * <code>toFile</code>
     *
     * @param fromUrl An url pointing to a file to download
     * @param toFile  File to save to, if existent will be overwrite
     * @throws IOException If fromUrl is invalid or there is any IO issue.
     */
    public static void downloadFile(Context context, String fromUrl,
                                    File toFile, DownloadProgressListener listener) throws IOException {
        downloadFileHandleRedirect(context, fromUrl, toFile, 0, listener);
    }

    public static String downloadFile(Context context, String fromUrl,
                                      String dir, DownloadProgressListener listener) throws IOException {
        return downloadFileHandleRedirect(context, fromUrl, dir, 0, listener);
    }


    public interface DownloadProgressListener {
        void onProgress(String url, String filePath, int progress);
    }

    /**
     * Internal version of {@link #downloadFile(Context, String, File,
     * DownloadUtils.DownloadProgressListener }
     * <p/>
     * 支持断点下载
     *
     * @param fromUrl  the url to download from
     * @param toFile   the file to download to
     * @param redirect true if it should accept redirects
     * @param listener used to report result back
     * @throws java.io.IOException
     */
    private static void downloadFileHandleRedirect(Context context,
                                                   String fromUrl, File toFile, int redirect,
                                                   DownloadProgressListener listener) throws IOException {
        if (context == null) {
            throw new RuntimeException("Context shall not be null");
        }
        if (!alreadyCheckedInternetPermission) {
            checkForInternetPermissions(context);
        }

        if (redirect > MAX_REDIRECTS) {
            throw new IOException("Too many redirects for " + fromUrl);
        }


        //断点续传相关
        long downloadedLength = 0;
        if (toFile.exists() && toFile.isFile()) {//如果文件纯在
            downloadedLength = toFile.length();
        }

        URL url = new URL(fromUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setInstanceFollowRedirects(false);
        boolean needBreakpointContinue = false;
        if (downloadedLength > 0) {//发现需要断点 发断点请求
            urlConnection.setRequestProperty("RANGE", "bytes=" + downloadedLength + "-");
            needBreakpointContinue = true;
        }

        urlConnection.connect();
        int contentLength = urlConnection.getContentLength();
        if (contentLength <= 0) {
            fromUrl = urlConnection.getHeaderField("Location");

            if (needBreakpointContinue) {
                //断点续传不被服务器支持，或者某些原因失败，那么清空文件，从头开始下载
                boolean createSuccess = false;
                if (toFile.exists()) {
                    toFile.delete();
                    createSuccess = toFile.createNewFile();
                }
                if (createSuccess) {
                    downloadFileHandleRedirect(context, fromUrl, toFile, redirect, listener);
                } else {
                    throw new IOException("create file -> " + redirect + " <- failed.");
                }
                return;
            } else {
                if (fromUrl == null) {
                    throw new IOException("No content or redirect found for URL "
                            + url + " with " + redirect + " redirects.");
                }
            }
            downloadFileHandleRedirect(context, fromUrl, toFile, redirect + 1, listener);
            return;
        }

        int responseCode = urlConnection.getResponseCode();
        if (responseCode == 416) { //HTTP 416 错误 – 所请求的范围无法满足
            if (listener != null) {
                listener.onProgress(fromUrl, toFile.getAbsolutePath(), 100);
            }
            return;
        }
        InputStream input = urlConnection.getInputStream();

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long total = 0;
        int count;

        if (downloadedLength > 0) { //断点续传 下载后续部分
            RandomAccessFile randomFile = new RandomAccessFile(toFile, "rw");
            randomFile.seek(downloadedLength);

            long fileLength = urlConnection.getContentLength() + downloadedLength;
            total = downloadedLength;
            if (listener != null && fileLength > 0) {
                listener.onProgress(fromUrl, toFile.getAbsolutePath(), (int) (total * 100 / fileLength));
            }

            while ((count = input.read(buffer)) > 0) {// > 0 due zero sized streams
                if (Thread.interrupted()) {
                    randomFile.close();
                    input.close();
                    Thread.currentThread().interrupt();
                    break;
                }

                total += count;
                randomFile.write(buffer, 0, count);

                if (listener != null && fileLength > 0) {
                    listener.onProgress(fromUrl, toFile.getAbsolutePath(), (int) (total * 100 / fileLength));
                }
            }

            if (randomFile != null) randomFile.close();
            if (input != null) input.close();
        } else { //全新下载
            OutputStream output = new FileOutputStream(toFile);
            int fileLength = urlConnection.getContentLength();

            while ((count = input.read(buffer)) > 0) {// > 0 due zero sized streams
                if (Thread.interrupted()) {
                    output.flush();
                    output.close();
                    input.close();
                    Thread.currentThread().interrupt();
                    break;
                }

                total += count;
                output.write(buffer, 0, count);

                if (listener != null && fileLength > 0) {
                    listener.onProgress(fromUrl, toFile.getAbsolutePath(), (int) (total * 100 / fileLength));
                }
            }

            if (output != null) output.close();
            if (input != null) input.close();
        }
    }


    private static String downloadFileHandleRedirect(Context context,
                                                     String fromUrl, String dir, int redirect,
                                                     DownloadProgressListener listener) throws IOException {
        if (context == null) {
//            EventAgent.onEvent(JiaKaoApplication.instance(), UmengEventDefine.V723_DOWN_QUESTION_FAIL, " downloadFileHandleRedirect + Context shall not be null ");
            throw new RuntimeException("Context shall not be null");
        }
        if (!alreadyCheckedInternetPermission) {
            checkForInternetPermissions(context);
        }

        if (redirect > MAX_REDIRECTS) {
//            EventAgent.onEvent(JiaKaoApplication.instance(), UmengEventDefine.V723_DOWN_QUESTION_FAIL, "downloadFileHandleRedirect + Too many redirects");
            throw new IOException("Too many redirects for " + fromUrl);
        }


        String fileName = null;
        try {
            Uri uri = Uri.parse(fromUrl);
            if (null != uri) {
                fileName = new File(uri.getPath()).getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == fileName) {
            fileName = new File(fromUrl).getName();
        }
        File toFile = new File(dir, fileName);
        //断点续传相关
        long downloadedLength = 0;
        if (toFile.exists() && toFile.isFile()) {//如果文件纯在
            downloadedLength = toFile.length();
        }

        URL url = new URL(fromUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setInstanceFollowRedirects(false);
        if (downloadedLength > 0) {//发现需要断点 发断点请求
            urlConnection.setRequestProperty("RANGE", "bytes=" + downloadedLength + "-");
        }

        urlConnection.connect();

        //for redirect
        fromUrl = urlConnection.getHeaderField("Location");
        if (!TextUtils.isEmpty(fromUrl)) {
            return downloadFileHandleRedirect(context, fromUrl, dir, redirect + 1, listener);
        }


        int responseCode = urlConnection.getResponseCode();
        if (responseCode == 416) { //HTTP 416 错误 – 所请求的范围无法满足
//            EventAgent.onEvent(JiaKaoApplication.instance(), UmengEventDefine.V723_DOWN_QUESTION_FAIL, "416");
            if (listener != null) {
                listener.onProgress(fromUrl, toFile.getAbsolutePath(), 100);
            }
            if (downloadedLength > 0) {
                return toFile.getAbsolutePath();
            }

            return null;
        }
        InputStream input = urlConnection.getInputStream();

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long total = 0;
        int count;

        if (downloadedLength > 0) { //断点续传 下载后续部分
            RandomAccessFile randomFile = new RandomAccessFile(toFile, "rw");
            randomFile.seek(downloadedLength);

            long fileLength = urlConnection.getContentLength() + downloadedLength;
            total = downloadedLength;
            if (listener != null && fileLength > 0) {
                listener.onProgress(fromUrl, toFile.getAbsolutePath(), (int) (total * 100 / fileLength));
            }

            while ((count = input.read(buffer)) > 0) {// > 0 due zero sized streams
                if (Thread.interrupted()) {
                    randomFile.close();
                    input.close();
                    Thread.currentThread().interrupt();
                    break;
                }

                total += count;
                randomFile.write(buffer, 0, count);

                if (listener != null && fileLength > 0) {
                    listener.onProgress(fromUrl, toFile.getAbsolutePath(), (int) (total * 100 / fileLength));
                }
            }

            if (randomFile != null) randomFile.close();
            if (input != null) input.close();
        } else { //全新下载
            OutputStream output = new FileOutputStream(toFile);
            int fileLength = urlConnection.getContentLength();

            while ((count = input.read(buffer)) > 0) {// > 0 due zero sized streams
                if (Thread.interrupted()) {
                    output.flush();
                    output.close();
                    input.close();
                    Thread.currentThread().interrupt();
                    break;
                }

                total += count;
                output.write(buffer, 0, count);

                if (listener != null && fileLength > 0) {
                    listener.onProgress(fromUrl, toFile.getAbsolutePath(), (int) (total * 100 / fileLength));
                }
            }

            if (output != null) output.close();
            if (input != null) input.close();
        }
        return toFile.getAbsolutePath();
    }

    private static void checkForInternetPermissions(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            if (requestedPermissions == null) {

                throw new RuntimeException(
                        "You must add android.permission.INTERNET to your app");
            }
            boolean found = false;
            for (String requestedPermission : requestedPermissions) {
                if ("android.permission.INTERNET".equals(requestedPermission)) {
                    found = true;
                }
            }
            if (!found) {
                throw new RuntimeException(
                        "You must add android.permission.INTERNET to your app");
            } else {
                alreadyCheckedInternetPermission = true;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }
}
