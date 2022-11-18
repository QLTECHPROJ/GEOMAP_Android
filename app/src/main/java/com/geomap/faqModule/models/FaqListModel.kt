package com.geomap.faqModule.models

data class FaqListModel(
    val ResponseCode : String,
    val ResponseData : List<FaQResponseData>,
    val ResponseMessage : String,
    val ResponseStatus : String
)

data class FaQResponseData(
    val answer : String,
    val created_at : String,
    val id : Int,
    val question : String,
    val updated_at : String
)
