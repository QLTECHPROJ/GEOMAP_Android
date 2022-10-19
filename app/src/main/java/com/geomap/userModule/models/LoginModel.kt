package com.geomap.userModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginModel {
    @SerializedName("ResponseData")
    @Expose
    var responseData: List<ResponseData>? = null

    @SerializedName("ResponseCode")
    @Expose
    var responseCode: String? = ""

    @SerializedName("ResponseMessage")
    @Expose
    var responseMessage: String? = ""

    @SerializedName("ResponseStatus")
    @Expose
    var responseStatus: String? = ""
    class ResponseData
}