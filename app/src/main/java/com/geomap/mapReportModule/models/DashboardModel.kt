package com.geomap.mapReportModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DashboardModel {
    @SerializedName("ResponseData") @Expose
    var responseData : ResponseData? = null

    @SerializedName("ResponseCode") @Expose
    var responseCode : String? = null

    @SerializedName("ResponseMessage") @Expose
    var responseMessage : String? = null

    @SerializedName("ResponseStatus") @Expose
    var responseStatus : String? = null

    class ResponseData {
        @SerializedName("underGround") @Expose var underGround : List<UnderGround>? = null
        @SerializedName("openCast") @Expose var openCast : List<OpenCast>? = null

        class UnderGround {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("name") @Expose var name : String? = null
            @SerializedName("location") @Expose var location : String? = null
            @SerializedName("ugDate") @Expose var ugDate : String? = null
            @SerializedName("scale") @Expose var scale : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
            @SerializedName("mapSerialNo") @Expose var mapSerialNo : String? = null
        }

        class OpenCast {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("minesSiteName") @Expose var minesSiteName : String? = null
            @SerializedName("mappingSheetNo") @Expose var mappingSheetNo : String? = null
            @SerializedName("pitName") @Expose var pitName : String? = null
            @SerializedName("pitLoaction") @Expose var pitLoaction : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
            @SerializedName("ocDate") @Expose var ocDate : String? = null
        }
    }
}