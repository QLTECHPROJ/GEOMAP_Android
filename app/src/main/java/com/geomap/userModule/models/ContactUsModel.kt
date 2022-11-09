package com.geomap.userModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ContactUsModel {
    @SerializedName("ResponseData") @Expose
    var responseData : ResponseData? = null

    @SerializedName("ResponseCode") @Expose
    var responseCode : String? = null

    @SerializedName("ResponseMessage") @Expose
    var responseMessage : String? = null

    @SerializedName("ResponseStatus") @Expose
    var responseStatus : String? = null

    class ResponseData {
        @SerializedName("userId") @Expose var userId : String? = null
        @SerializedName("name") @Expose var name : String? = null
        @SerializedName("mobile") @Expose var mobile : String? = null
        @SerializedName("email") @Expose var email : String? = null
        @SerializedName("subject") @Expose var subject : String? = null
        @SerializedName("message") @Expose var message : String? = null
        @SerializedName("updated_at") @Expose var updatedAt : String? = null
        @SerializedName("created_at") @Expose var createdAt : String? = null
        @SerializedName("id") @Expose var id : Int? = null
    }
}