package com.geomap.roomDataBase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters  {
    @TypeConverter
    fun fromTypeFile(bitMap: Bitmap?): ByteArray? {
        val otStream = ByteArrayOutputStream()
        bitMap!!.compress(Bitmap.CompressFormat.JPEG,100,otStream)
        return otStream.toByteArray()
    }

    @TypeConverter
    fun toBitMap(byteArray: ByteArray?): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray!!.size)
    }
}