package com.geomap.faqModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class FaqListModel {

    @SerializedName("ResponseData")
    @Expose
    var responseData : List<ResponseData>? = null

    @SerializedName("ResponseCode")
    @Expose
    var responseCode : String? = null

    @SerializedName("ResponseMessage")
    @Expose
    var responseMessage : String? = null

    @SerializedName("ResponseStatus")
    @Expose
    var responseStatus : String? = null

    class ResponseData {
        @SerializedName("ID") @Expose var id : String? = null
        @SerializedName("Desc") @Expose var desc : String? = null
        @SerializedName("Title") @Expose var title : String? = null
    }
}