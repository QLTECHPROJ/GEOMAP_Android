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
        @SerializedName("created_at") @Expose var createdAt : String? = null
        @SerializedName("updated_at") @Expose var updatedAt : String? = null

        /*  Under Ground View All Keys */
        @SerializedName("name") @Expose var name : String? = null
        @SerializedName("location") @Expose var location : String? = null
        @SerializedName("ugDate") @Expose var ugDate : String? = null
        @SerializedName("mappedBy") @Expose var mappedBy : String? = null
        @SerializedName("mapSerialNo") @Expose var mapSerialNo : String? = null

        /*  Open Cast View All Keys */
        @SerializedName("minesSiteName") @Expose var minesSiteName : String? = null
        @SerializedName("mappingSheetNo") @Expose var mappingSheetNo : String? = null
        @SerializedName("pitName") @Expose var pitName : String? = null
        @SerializedName("pitLoaction") @Expose var pitLoaction : String? = null
        @SerializedName("ocDate") @Expose var ocDate : String? = null
    }}