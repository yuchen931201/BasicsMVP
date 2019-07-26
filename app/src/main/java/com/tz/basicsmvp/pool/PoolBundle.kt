package cn.eclicks.drivingtest.pool

import android.content.Context
import android.os.Parcel
import android.os.Parcelable

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/22 11:40
 * @Package: cn.eclicks.drivingtest.pool
 * @Description:
 **/
class PoolBundle() : Parcelable {

    var ctx :Context? = null
    var unique : String? = null //唯一标识符 作为key， 可为ID或token等唯一字符
    var action: String? = null
    var downloadUrl: String? = null
    var saveFileName: String? = null
    var saveFilePath: String? = null
    var request: Any? = null
    var result: Any? = null
    var fileSize:Long = 0
    var progress: Int = 0

    constructor(parcel: Parcel) : this() {
        unique = parcel.readString()
        action = parcel.readString()
        downloadUrl = parcel.readString()
        saveFileName = parcel.readString()
        saveFilePath = parcel.readString()
        fileSize = parcel.readLong()
        progress = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(unique)
        parcel.writeString(action)
        parcel.writeString(downloadUrl)
        parcel.writeString(saveFileName)
        parcel.writeString(saveFilePath)
        parcel.writeLong(fileSize)
        parcel.writeInt(progress)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PoolBundle> {
        override fun createFromParcel(parcel: Parcel): PoolBundle {
            return PoolBundle(parcel)
        }

        override fun newArray(size: Int): Array<PoolBundle?> {
            return arrayOfNulls(size)
        }
    }

}