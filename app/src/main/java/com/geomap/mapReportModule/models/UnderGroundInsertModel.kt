package com.geomap.mapReportModule.models

import retrofit.mime.TypedFile

data class UnderGroundInsertModel(
    var minesSiteName : String? = null,
    var name : String? = null,
    var shift : String? = null,
    var mappedBy : String? = null,
    var scale : String? = null,
    var location : String? = null,
    var venieLoad : String? = null,
    var xCordinate : String? = null,
    var yCordinate : String? = null,
    var zCordinate : String? = null,
    var comment : String? = null,
    var mapSerialNo : String? = null,
    var ugDate : String? = null,
    var leftImage : TypedFile? = null,
    var roofImage : TypedFile? = null,
    var rightImage : TypedFile? = null,
    var faceImage : TypedFile? = null,
    var attribute : String? = null
)