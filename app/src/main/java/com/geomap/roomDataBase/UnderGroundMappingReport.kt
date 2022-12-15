package com.geomap.roomDataBase

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "undergroundmappingreport")
class UnderGroundMappingReport : Serializable {
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    @ColumnInfo(name = "userId")
    var userId :String? = null

    @ColumnInfo(name = "name")
    var name :String? = null

    @ColumnInfo(name = "mapSerialNo")
    var mapSerialNo :String? = null

    @ColumnInfo(name = "ugDate")
    var ugDate :String? = null

    @ColumnInfo(name = "shift")
    var shift :String? = null

    @ColumnInfo(name = "mappedBy")
    var mappedBy :String? = null

    @ColumnInfo(name = "scale")
    var scale :String? = null

    @ColumnInfo(name = "location")
    var location :String? = null

    @ColumnInfo(name = "veinOrLoad")
    var veinOrLoad :String? = null

    @ColumnInfo(name = "xCordinate")
    var xCordinate :String? = null

    @ColumnInfo(name = "yCordinate")
    var yCordinate :String? = null

    @ColumnInfo(name = "zCordinate")
    var zCordinate :String? = null

    @ColumnInfo(name = "comment")
    var comment :String? = null

    @ColumnInfo(name = "leftImage")
    @TypeConverters(Converters::class)
    var leftImage : Bitmap? = null

    @ColumnInfo(name = "roofImage")
    @TypeConverters(Converters::class)
    var roofImage :Bitmap? = null

    @ColumnInfo(name = "rightImage")
    @TypeConverters(Converters::class)
    var rightImage :Bitmap? = null

    @ColumnInfo(name = "faceImage")
    @TypeConverters(Converters::class)
    var faceImage :Bitmap? = null

    @ColumnInfo(name = "attributes")
    var attributes :String? = null

    @ColumnInfo(name = "createDate")
    var createDate :String? = null

    @ColumnInfo(name = "updateDate")
    var updateDate :String? = null
}