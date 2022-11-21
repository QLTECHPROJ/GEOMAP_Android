package com.geomap.mapReportModule.models

import retrofit.mime.TypedFile

class SyncDataUgModel {
    var userId: String? = ""
    var name: String? = ""
    var comment: String? = ""
    var attribute: String? = null
    var ugDate: String? = ""
    var mapSerialNo: String? = ""
    var shift: String? = ""
    var mappedBy: String? = ""
    var scale: String? = ""
    var location: String? = ""
    var venieLoad: String? = ""
    var xCordinate: String? = ""
    var yCordinate: String? = ""
    var zCordinate: String? = ""
    var roofImage: TypedFile? = null
    var leftImage: TypedFile? = null
    var rightImage: TypedFile? = null
    var faceImage: TypedFile? = null
}