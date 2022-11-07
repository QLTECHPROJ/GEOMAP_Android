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
        @SerializedName("id") @Expose var id : String? = null
        @SerializedName("question") @Expose var question : String? = null
        @SerializedName("answer") @Expose var answer : String? = null
        @SerializedName("created_at") @Expose var createdAt : String? = null
        @SerializedName("updated_at") @Expose var updatedAt : String? = null
    }
}