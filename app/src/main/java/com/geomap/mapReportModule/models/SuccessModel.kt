package com.geomap.mapReportModule.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class SuccessModel {
    @SerializedName("ResponseCode") @Expose
    var responseCode : String? = null

    @SerializedName("ResponseMessage") @Expose
    var responseMessage : String? = null

    @SerializedName("ResponseStatus") @Expose
    var responseStatus : String? = null
}