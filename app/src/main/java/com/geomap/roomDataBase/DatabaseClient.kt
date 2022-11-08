package com.geomap.roomDataBase

import android.content.Context
import androidx.room.Room

class DatabaseClient(private val Ctx: Context?) {
    //our app database object
    val report: GeoMapDatabase = Room.databaseBuilder(Ctx!!, GeoMapDatabase::class.java, "GeoMap_database").build()
    fun getDatabase(): GeoMapDatabase {
        return report
    }

    companion object {
        private var Instance: DatabaseClient? = null
        @Synchronized
        fun getInstance(Ctx: Context?): DatabaseClient? {
            if (Instance == null) {
                Instance = DatabaseClient(Ctx)
            }
            return Instance
        }
    }
}