package com.geomap.roomDataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "UnderGroundMappingReport")
class UnderGroundMappingReport : Serializable {
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    @ColumnInfo(name = "Id")
    var iD: Int? = null

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
    var locations :String? = null

    @ColumnInfo(name = "veinOrLoad")
    var veinOrLoad :String? = null

    @ColumnInfo(name = "xCoordinate")
    var xCoordinate :String? = null

    @ColumnInfo(name = "yCoordinate")
    var yCoordinate :String? = null

    @ColumnInfo(name = "zCoordinate")
    var zCoordinate :String? = null

    @ColumnInfo(name = "waterCondition")
    var waterCondition :String? = null

    @ColumnInfo(name = "comment")
    var comment :String? = null

    @ColumnInfo(name = "leftImage")
    var leftImage :String? = null

    @ColumnInfo(name = "roofImage")
    var roofImage :String? = null

    @ColumnInfo(name = "rightImage")
    var rightImage :String? = null

    @ColumnInfo(name = "faceImage")
    var faceImage :String? = null

    @ColumnInfo(name = "attributes")
    var attributes :String? = null

    @ColumnInfo(name = "createDate")
    var createDate :String? = null

    @ColumnInfo(name = "updateDate")
    var updateDate :String? = null
}