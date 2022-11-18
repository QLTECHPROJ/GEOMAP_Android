package com.geomap.mapReportModule.models

data class SuccessModel(
    val ResponseCode : String,
    val ResponseData : SuccessResponseData?,
    val ResponseMessage : String,
    val ResponseStatus : String
)

data class SuccessResponseData(val temp : String)