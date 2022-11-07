package com.geomap.userModule.models

data class VersionModel(val ResponseCode: String, val ResponseData: VersionResponseData?,
    val ResponseMessage: String, val ResponseStatus: String)

data class VersionResponseData(val IsForce: String, val supportTitle: String,
    val supportText: String, val supportEmail: String)