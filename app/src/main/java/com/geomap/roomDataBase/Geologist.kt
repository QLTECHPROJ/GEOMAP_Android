package com.geomap.roomDataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "geologist")
class Geologist : Serializable {
        @PrimaryKey(autoGenerate = true)
        var uid = 0

        @ColumnInfo(name = "Id")
        var iD: Int? = null

        @ColumnInfo(name = "name")
        var name: String? = null

        @ColumnInfo(name = "email")
        var email: String? = null

        @ColumnInfo(name = "phone")
        var phone: String? = null

        @ColumnInfo(name = "company_type")
        var companyType: String? = null

        @ColumnInfo(name = "position")
        var position: String? = null

        @ColumnInfo(name = "company")
        var company: String? = null

        @ColumnInfo(name = "password")
        var password: String? = null

        @ColumnInfo(name = "createDate")
        var createDate: String? = null

        @ColumnInfo(name = "updateDate")
        var updateDate: String? = null
}