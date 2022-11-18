package com.geomap.roomDataBase

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "opencastmappingreport")
class OpenCastMappingReport : Serializable {
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    @ColumnInfo(name = "Id", defaultValue = "")
    var iD: Int? = null

    @ColumnInfo(name = "ocDate")
    var ocDate: String? = null

    @ColumnInfo(name = "mappingSheetNo")
    var mappingSheetNo: String? = null

    @ColumnInfo(name = "minesSiteName")
    var minesSiteName: String? = null

    @ColumnInfo(name = "pitName")
    var pitName: String? = null

    @ColumnInfo(name = "pitLocation")
    var pitLocation: String? = null

    @ColumnInfo(name = "shiftInChargeName")
    var shiftInChargeName: String? = null

    @ColumnInfo(name = "geologistName")
    var geologistName: String? = null

    @ColumnInfo(name = "shift")
    var shift: String? = null

    @ColumnInfo(name = "faceLocation")
    var faceLocation: String? = null

    @ColumnInfo(name = "faceLength")
    var faceLength: String? = null

    @ColumnInfo(name = "faceArea")
    var faceArea: String? = null

    @ColumnInfo(name = "faceRockTypes")
    var faceRockTypes: String? = null

    @ColumnInfo(name = "benchRL")
    var benchRL: String? = null

    @ColumnInfo(name = "benchHeightWidth")
    var benchHeightWidth: String? = null

    @ColumnInfo(name = "benchAngle")
    var benchAngle: String? = null

    @ColumnInfo(name = "dipDirectionAngle")
    var dipDirectionAngle: String? = null

    @ColumnInfo(name = "thicknessOfOre")
    var thicknessOfOre: String? = null

    @ColumnInfo(name = "thicknessOfOverburden")
    var thicknessOfOverburden :String? = null

    @ColumnInfo(name = "thicknessOfInterBurden")
    var thicknessOfInterBurden :String? = null

    @ColumnInfo(name = "observedGradeOfOre")
    var observedGradeOfOre :String? = null

    @ColumnInfo(name = "sampleCollected")
    var sampleCollected :String? = null

    @ColumnInfo(name = "actualGradOfOre")
    var actualGradOfOre:String? = null

    @ColumnInfo(name = "weathering")
    var weathering :String? = null

    @ColumnInfo(name = "rockStrength")
    var rockStrength :String? = null

    @ColumnInfo(name = "waterCondition")
    var waterCondition :String? = null

    @ColumnInfo(name = "typeOfGeologicalStructures")
    var typeOfGeologicalStructures :String? = null

    @ColumnInfo(name = "typeOfFaults")
    var typeOfFaults :String? = null

    @ColumnInfo(name = "notes")
    var notes :String? = null

    @ColumnInfo(name = "geologistSign")
    @TypeConverters(Converters::class)
    var geologistSign : Bitmap? = null

    @ColumnInfo(name = "clientsGeologistSign")
    @TypeConverters(Converters::class)
    var clientsGeologistSign : Bitmap? = null

    @ColumnInfo(name = "image")
    @TypeConverters(Converters::class)
    var image : Bitmap? = null

    @ColumnInfo(name = "createDate")
    var createDate :String? = null

    @ColumnInfo(name = "updateDate")
    var updateDate :String? = null

}