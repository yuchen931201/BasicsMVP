package com.tz.basicsmvp.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import com.blankj.utilcode.util.LogUtils;
import com.tz.basicsmvp.MyApp;

import java.io.File;
import java.io.IOException;


/**
 * fuck you
 */
public class StorageUtils {

    private static final String INDIVIDUAL_DIR_NAME = "storage-tmp";

    /**
     * @param requireWriteAccess boolean indicating that write access to external storage is required
     * @return true if the device has external storage. If requireWriteAccess is set to true it will also check
     * for write permissions on the external storage. If the media isn't mounted or it is mounted as read-only
     * when writeAccess is required it returns false.
     */
    public static boolean hasExternalStorage(boolean requireWriteAccess) {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else return !requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


    public static boolean hasAvaiableSpace(long bytes) {
        boolean ishasSpace = false;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            long availableSpare = blocks * blockSize;
            if (availableSpare > bytes) {
                ishasSpace = true;
            }
        }
        return ishasSpace;
    }


    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted. Else - Android defines cache directory on
     * device's file system.
     *
     * @param context Application context
     * @return Cache {@link File directory}
     */
    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }

    public static File getVideoDirectory(Context context) {
        File appCacheDir = null;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            appCacheDir = getExternalVideoDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }


    /**
     * Returns individual application cache directory (for only image caching from ImageLoader). Cache directory will be
     * created on SD card <i>("/Android/data/[app_package_name]/cache/uil-images")</i> if card is mounted. Else -
     * Android defines cache directory on device's file system.
     *
     * @param context Application context
     * @return Cache {@link File directory}
     */
    public static File getIndividualCacheDirectory(Context context) {
        File cacheDir = getCacheDirectory(context);
        File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
        if (!individualCacheDir.exists()) {
            if (!individualCacheDir.mkdir()) {
                individualCacheDir = cacheDir;
            }
        }
        return individualCacheDir;
    }

    /**
     * Returns specified application cache directory. Cache directory will be created on SD card by defined path if card
     * is mounted. Else - Android defines cache directory on device's file system.
     *
     * @param context  Application context
     * @param cacheDir Cache directory path (e.g.: "AppCacheDir", "AppDir/cache/images")
     * @return Cache {@link File directory}
     */
    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }

    public static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                LogUtils.w("Unable to create external cache directory");
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                LogUtils.i("Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    private static File getExternalVideoDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "video");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                LogUtils.w("Unable to create external cache directory");
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                LogUtils.i("Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    public static void deleteAllFiles(File f) {
        if (f.exists()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (File file : files)
                    if (file.isDirectory()) {
                        deleteAllFiles(file);
                        file.delete();
                    } else if (file.isFile()) {
                        file.delete();
                    }
            }
            f.delete();
        }
    }

    /**
     * 在SD卡上创建app的下载目录
     */
    public static final String MATERIAL_DIR = "material";

    private static File getExternalMaterialDir(Context context) {

        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), MATERIAL_DIR);
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                LogUtils.w("Unable to create external cache directory");
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                LogUtils.i("Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    public static File getExternalQuestionMaterialDir(Context context) {
        if (null == context) context = MyApp.Companion.getInstance();
        return getOwnCacheDirectory(context, context.getPackageName() + File.separator + MATERIAL_DIR);
    }

    public static File getExternalQuestionMaterialDirs(Context context) {
        if (null == context) context = MyApp.Companion.getInstance();
        return getOwnCacheDirectory(context, context.getPackageName() + File.separator + MATERIAL_DIR + File.separator);
    }

    public static File getExternalQuestionMaterialDownDir(Context context) {
        if (null == context) context = MyApp.Companion.getInstance();
        return getOwnCacheDirectory(context, context.getPackageName() + File.separator);
    }

    private static final String BACK_UP = "back_up";

    public static File getExternalQuestionBackDir(Context context) {
        if (null == context) return null;
        File dir = getOwnCacheDirectory(context, context.getPackageName() + File.separator + BACK_UP);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return null;
            }
            try {
                new File(dir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return dir;
    }


    /**
     * 题库素材
     */
    public static File getMaterialDir(Context context, String fileName) {
        if (null == context) return null;
        File cacheDirectory = StorageUtils.getExternalQuestionMaterialDir(context);
        if (!cacheDirectory.exists()) {
            if (!cacheDirectory.mkdirs()) {
                return null;
            }
            try {
                new File(cacheDirectory, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return new File(cacheDirectory, fileName);
    }



}
