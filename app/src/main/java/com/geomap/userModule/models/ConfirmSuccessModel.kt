package com.geomap.userModule.models

data class ConfirmSuccessModel(val ResponseCode : String,
    val ResponseData : ConfirmSuccessResponseData?,
    val ResponseMessage : String, val ResponseStatus : String)

data class ConfirmSuccessResponseData(val temp : String)