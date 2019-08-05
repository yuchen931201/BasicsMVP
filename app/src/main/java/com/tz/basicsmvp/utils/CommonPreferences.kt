package com.tz.basicsmvp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.tz.basicsmvp.MyApp

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/20 15:22
 * @Package: com.tz.basicsmvp.utils
 * @Description:
 **/
class CommonPreferences<T> constructor(key:String, value:T) :SharePreferencesHelper<T>(key, value){

    companion object{

        var prefs: SharedPreferences
            get() = MyApp.context.getSharedPreferences(common_file_name, Context.MODE_PRIVATE)
            set(value) {}

        /** 删除全部数据*/
        fun clearPreference(){
            prefs.edit().clear().apply()
        }

        /** 根据key删除存储数据*/
        fun clearPreference(key : String){
            prefs.edit().remove(key).apply()
        }
    }

    init {
        putSharedPreferences(key, value)
    }

    @SuppressLint("CommitPrefEdits")
    override fun putSharedPreferences(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name,serialize(value))
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getSharedPreferences(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else ->  deSerialization(getString(name,serialize(default)))
        }
        return res as T
    }

    /**
     * 查询某个key是否已经存在
     * @param key
     * @return
     */
    override fun contains(key: String): Boolean {
        return prefs.contains(key)
    }

    /**
     * 返回所有的键值对
     * @param context
     * @return
     */
    override fun getAll(): Map<String, *> {
        return prefs.all
    }
}