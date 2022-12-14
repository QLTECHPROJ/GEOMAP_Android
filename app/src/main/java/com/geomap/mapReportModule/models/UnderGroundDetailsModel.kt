package com.geomap.mapReportModule.models

data class UnderGroundDetailsModel(
    val ResponseCode : String,
    val ResponseData : UnderGroundDetailResponseData,
    val ResponseMessage : String,
    val ResponseStatus : String
)

data class UnderGroundDetailResponseData(
    val attribute : List<Attribute>,
    val comment : String,
    val created_at : String,
    val faceImage : String,
    val leftImage : String,
    val location : String,
    val mapSerialNo : String,
    val mappedBy : String,
    val name : String,
    val rightImage : String,
    val roofImage : String,
    val scale : String,
    val shift : String,
    val ugDate : String,
    val updated_at : String,
    val userId : String,
    val venieLoad : String,
    val xCordinate : String,
    val yCordinate : String,
    val zCordinate : String
)

data class Attribute(
    val created_at : String,
    val id : Int,
    val name : String,
    val nose : String,
    val properties : String,
    val undergroundId : Int,
    val updated_at : String
)
