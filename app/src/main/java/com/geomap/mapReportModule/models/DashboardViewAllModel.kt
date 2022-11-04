package com.geomap.mapReportModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DashboardViewAllModel {
    @SerializedName("ResponseData") @Expose
    var responseData : List<ResponseData>? = null

    @SerializedName("ResponseCode") @Expose
    var responseCode : String? = null

    @SerializedName("ResponseMessage") @Expose
    var responseMessage : String? = null

    @SerializedName("ResponseStatus") @Expose
    var responseStatus : String? = null

    class ResponseData {
        @SerializedName("id") @Expose
        var id : Int? = null

        @SerializedName("name") @Expose
        var name : String? = null

        @SerializedName("email") @Expose
        var email : String? = null

        @SerializedName("phone") @Expose
        var phone : String? = null

        @SerializedName("logo") @Expose
        var logo : String? = null

        @SerializedName("address") @Expose
        var address : String? = null

        @SerializedName("created_at") @Expose
        var createdAt : String? = null

        @SerializedName("updated_at") @Expose
        var updatedAt : String? = null
    }
}