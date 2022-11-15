package com.geomap.roomDataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nos")
class Nos {
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    @ColumnInfo(name = "Id")
    var iD: Int? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "attributeId")
    var attributeId: Int? = null

    @ColumnInfo(name = "createDate")
    var createDate: String? = null

    @ColumnInfo(name = "updateDate")
    var updateDate: String? = null
}