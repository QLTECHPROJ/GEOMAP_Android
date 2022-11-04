package com.geomap.mapReportModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UnderGroundDetailsModel {
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

        @SerializedName("shift") @Expose
        var shift : String? = null

        @SerializedName("mapped_by") @Expose
        var mappedBy : String? = null

        @SerializedName("scale") @Expose
        var scale : String? = null

        @SerializedName("loaction") @Expose
        var loaction : String? = null

        @SerializedName("venie_load") @Expose
        var venieLoad : String? = null


        @SerializedName("x_cordinate") @Expose
        private var xCordinate : String? = null

        @SerializedName("y_cordinate") @Expose
        private var yCordinate : String? = null

        @SerializedName("z_cordinate") @Expose
        private var zCordinate : String? = null

        @SerializedName("properties") @Expose
        var properties : String? = null

        @SerializedName("country") @Expose
        var country : String? = null

        @SerializedName("description") @Expose
        var description : String? = null

        @SerializedName("created_at") @Expose
        var createdAt : String? = null

        @SerializedName("updated_at") @Expose
        var updatedAt : String? = null

        @SerializedName("map_serial_no") @Expose
        var mapSerialNo : Any? = null

        @SerializedName("attribute") @Expose
        var attribute : List<Attribute>? = null

        class Attribute {
            @SerializedName("id") @Expose
            var id : Int? = null

            @SerializedName("undergroundId") @Expose
            var undergroundId : Int? = null

            @SerializedName("name") @Expose
            var name : String? = null

            @SerializedName("nose") @Expose
            var nose : String? = null

            @SerializedName("created_at") @Expose
            var createdAt : String? = null

            @SerializedName("updated_at") @Expose
            var updatedAt : String? = null
        }

    }
}