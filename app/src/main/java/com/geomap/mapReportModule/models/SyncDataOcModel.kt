package com.geomap.mapReportModule.models

import retrofit.http.Part
import retrofit.mime.TypedFile

class SyncDataOcModel {
    var userId : String? = ""
    var minesSiteName : String? = ""
    var mappingSheetNo : String? = ""
    var pitName : String? = ""
    var pitLoaction : String? = ""
    var shiftInchargeName  : String? = ""
    var geologistName  : String? = ""
    var faceLocation  : String? = ""
    var faceLength  : String? = ""
    var faceArea  : String? = ""
    var faceRockType  : String? = ""
    var benchRl  : String? = ""
    var benchHeightWidth  : String? = ""
    var benchAngle  : String? = ""
    var thicknessOfOre  : String? = ""
    var thicknessOfOverburdan  : String? = ""
    var thicknessOfInterburden  : String? = ""
    var observedGradeOfOre  : String? = ""
    var actualGradeOfOre  : String? = ""
    var sampleColledted  : String? = ""
    var weathring  : String? = ""
    var rockStregth  : String? = ""
    var waterCondition  : String? = ""
    var typeOfGeologist  : String? = ""
    var typeOfFaults  : String? = ""
    var notes  : String? = ""
    var shift  : String? = ""
    var ocDate  : String? = ""
    var dipDirectionAndAngle  : String? = ""
    var image  : TypedFile? = null
    var geologistSign  : TypedFile? = null
    var clientsGeologistSign  : TypedFile? = null
}