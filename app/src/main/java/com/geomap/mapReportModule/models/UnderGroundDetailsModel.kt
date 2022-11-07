package com.geomap.mapReportModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UnderGroundDetailsModel {
    @SerializedName("ResponseData") @Expose
    var responseData : ResponseData? = null

    @SerializedName("ResponseCode") @Expose
    var responseCode : String? = null

    @SerializedName("ResponseMessage") @Expose
    var responseMessage : String? = null

    @SerializedName("ResponseStatus") @Expose
    var responseStatus : String? = null

    class ResponseData {
        @SerializedName("id") @Expose var id : Int? = null
        @SerializedName("name") @Expose var name : String? = null
        @SerializedName("shift") @Expose var shift : String? = null
        @SerializedName("mappedBy") @Expose var mappedBy : String? = null
        @SerializedName("scale") @Expose var scale : String? = null
        @SerializedName("location") @Expose var location : String? = null
        @SerializedName("venieLoad") @Expose var venieLoad : String? = null
        @SerializedName("xCordinate") @Expose var xCordinate : String? = null
        @SerializedName("yCordinate") @Expose var yCordinate : String? = null
        @SerializedName("zCordinate") @Expose var zCordinate : String? = null
        @SerializedName("comment") @Expose var comment : String? = null
        @SerializedName("created_at") @Expose var createdAt : String? = null
        @SerializedName("updated_at") @Expose var updatedAt : String? = null
        @SerializedName("mapSerialNo") @Expose var mapSerialNo : String? = null
        @SerializedName("ugDate") @Expose var ugDate : String? = null
        @SerializedName("leftImage") @Expose var leftImage : Any? = null
        @SerializedName("roofImage") @Expose var roofImage : Any? = null
        @SerializedName("rightImage") @Expose var rightImage : Any? = null
        @SerializedName("faceImage") @Expose var faceImage : Any? = null
        @SerializedName("userId") @Expose var userId : String? = null
        @SerializedName("attribute") @Expose var attribute : List<Attribute>? = null

        class Attribute {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("undergroundId") @Expose var undergroundId : Int? = null
            @SerializedName("name") @Expose var name : String? = null
            @SerializedName("nose") @Expose var nose : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
            @SerializedName("properties") @Expose var properties : String? = null
        }
    }
}