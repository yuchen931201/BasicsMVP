package com.tz.basicsmvp.utils

import java.io.*
import kotlin.reflect.KProperty

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/17 18:21
 * @Package: com.tz.basicsmvp.utils
 * @Description:使用 operator 操作符重载 以实现kotlin委托属性，创建SharedPreference实例
 **/
abstract class SharePreferencesHelper<T> constructor(val key:String, private val value:T) {

    abstract fun putSharedPreferences(name: String, value: T)

    abstract fun getSharedPreferences(name: String, default: T): T

    abstract fun getAll(): Map<String, *>

    abstract fun contains(key: String): Boolean

    companion object{

        const val common_file_name = "common_basics_mvp_kotlin_file"

        const val user_file_name = "user_basics_mvp_kotlin_file"

        const val FILE_HEADER = "user_basics_mvp_kotlin_file"

    }

    operator fun getValue(arg1: Any?, arg2: KProperty<*>): T {
        return getSharedPreferences(key, value)
    }

    operator fun setValue(arg1: Any?, arg2: KProperty<*>, arg3: T) {
        putSharedPreferences(key, arg3)
    }

    /**
     * 序列化对象
     * @param person
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun<A> serialize(obj: A): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    /**
     * 反序列化对象
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class, ClassNotFoundException::class)
    fun<A> deSerialization(str: String): A {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1")))
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream)
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }

}
