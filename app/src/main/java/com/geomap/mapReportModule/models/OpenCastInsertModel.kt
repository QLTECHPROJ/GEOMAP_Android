package com.geomap.mapReportModule.models

data class OpenCastInsertModel(
    var minesSiteName : String? = "",
    var sheetNo : String? = "",
    var ocDate : String? = "",
    var pitName : String? = "",
    var pitLocation : String? = "",
    var shiftInchargeName : String? = "",
    var geologistName : String? = "",
    var faceLocation : String? = "",
    var faceLengthM : String? = "",
    var faceAreaM2 : String? = "",
    var faceRockTypes : String? = "",
    var benchRL : String? = "",
    var benchHeightWidth : String? = "",
    var benchAngle : String? = "",
    var dipDirectionAngle : String? = "",
    var thicknessOfOre : String? = "",
    var thinessOfOverburden : String? = "",
    var thicknessOfInterburden : String? = "",
    var observedGradeOfOre : String? = "",
    var actualGradeOfOre : String? = "",
    var sampleCollected : String? = "",
    var weathering : String? = "",
    var rockStregth : String? = "",
    var waterCondition : String? = "",
    var typeOfGeologist : String? = "",
    var typeOfFaults : String? = "",
    var shift : String? = "",
    var notes : String? = "",
    var image : String? = "",
    var geologistSign : String? = "",
    var clientsGeologistSign : String? = ""
)