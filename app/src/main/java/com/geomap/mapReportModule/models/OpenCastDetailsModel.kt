package com.geomap.mapReportModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OpenCastDetailsModel(
    val ResponseCode: String,
    val ResponseData: OpenCastDetailResponseData,
    val ResponseMessage: String,
    val ResponseStatus: String
)

data class OpenCastDetailResponseData(
    val actualGradeOfOre: String,
    val benchAngle: String,
    val benchHeightWidth: String,
    val benchRl: String,
    val clientsGeologistSign: String,
    val created_at: String,
    val dipDirectionAndAngle: String,
    val faceArea: String,
    val faceLength: String,
    val faceLocation: String,
    val faceRockType: String,
    val geologistName: String,
    val geologistSign: String,
    val id: Int,
    val image: String,
    val mappingSheetNo: String,
    val minesSiteName: String,
    val notes: String,
    val observedGradeOfOre: String,
    val ocDate: String,
    val pitLoaction: String,
    val pitName: String,
    val rockStregth: String,
    val sampleColledted: String,
    val shift: String,
    val shiftInchargeName: String,
    val thicknessOfInterburden: String,
    val thicknessOfOre: String,
    val thicknessOfOverburdan: String,
    val typeOfFaults: String,
    val typeOfGeologist: String,
    val updated_at: String,
    val userId: String,
    val waterCondition: String,
    val weathring: String
)
