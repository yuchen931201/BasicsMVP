package com.tz.basicsmvp.utils.core.utils

/**
 * @author Pinger
 * @since 18-6-12 下午2:51
 * 图片加载常用字符常量
 */

object ImageConstant {

    const val IMAGE_GIF = ".gif"
    const val IMAGE_JPG = ".jpg"
    const val IMAGE_PATH = "Images"
    const val IMAGE_PLACE_HOLDER_COLOR = "#F2F2F2"

    const val LOAD_NULL_CONTEXT_ANY = "图片加载失败：context为null或者any为null"
    const val LOAD_NULL_CONTEXT = "图片加载失败：context为null"
    const val LOAD_NULL_ANY_VIEW = "图片加载失败：any为null或者view为null"

    const val LOAD_ERROR_VIEW_TYPE = "Glide只支持ImageView展示图片"
    const val LOAD_ERROR = "图片加载失败"

    const val SAVE_NULL_CONTEXT_ANY = "图片保存失败：context为null或者any为null"
    const val SAVE_NOT_PERMISSION = "图片保存失败：没有储存权限"
    const val SAVE_FAIL = "图片保存失败"
    const val SAVE_PATH= "图片已保存至 "

    const val CLEAR_NULL_CONTEXT = "清理失败：context为null"
}