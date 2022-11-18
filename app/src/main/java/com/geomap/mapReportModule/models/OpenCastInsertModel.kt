package com.geomap.mapReportModule.models

import retrofit.mime.TypedFile

data class OpenCastInsertModel(
    var minesSiteName : String? = null,
    var sheetNo : String? = null,
    var ocDate : String? = null,
    var pitName : String? = null,
    var pitLocation : String? = null,
    var shiftInchargeName : String? = null,
    var geologistName : String? = null,
    var faceLocation : String? = null,
    var faceLengthM : String? = null,
    var faceAreaM2 : String? = null,
    var faceRockTypes : String? = null,
    var benchRL : String? = null,
    var benchHeightWidth : String? = null,
    var benchAngle : String? = null,
    var dipDirectionAngle : String? = null,
    var thicknessOfOre : String? = null,
    var thinessOfOverburden : String? = null,
    var thicknessOfInterburden : String? = null,
    var observedGradeOfOre : String? = null,
    var actualGradeOfOre : String? = null,
    var sampleCollected : String? = null,
    var weathering : String? = null,
    var rockStregth : String? = null,
    var waterCondition : String? = null,
    var typeOfGeologicalStructures : String? = null,
    var typeOfFaults : String? = null,
    var shift : String? = null,
    var notes : String? = null,
    var image : TypedFile? = null,
    var geologistSign : TypedFile? = null,
    var clientsGeologistSign : TypedFile? = null
)