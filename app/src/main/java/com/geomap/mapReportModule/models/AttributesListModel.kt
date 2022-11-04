package com.geomap.mapReportModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttributesListModel {
    @SerializedName("ResponseData") @Expose
    var responseData : List<ResponseData>? = null

    @SerializedName("ResponseCode") @Expose
    var responseCode : String? = null

    @SerializedName("ResponseMessage") @Expose
    var responseMessage : String? = null

    @SerializedName("ResponseStatus") @Expose
    var responseStatus : String? = null

    class ResponseData {
        @SerializedName("id") @Expose var id : Int? = null
        @SerializedName("name") @Expose var name : String? = null
        @SerializedName("created_at") @Expose var createdAt : String? = null
        @SerializedName("updated_at") @Expose var updatedAt : String? = null
        @SerializedName("nos") @Expose var nosList : List<Nos>? = null

        class Nos {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("attributeId") @Expose var attributeId : Int? = null
            @SerializedName("name") @Expose var name : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
        }
    }
}