package com.geomap.userModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserCommonDataModel {
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
        @SerializedName("deviceToken") @Expose var deviceToken : String? = null
        @SerializedName("deviceId") @Expose var deviceId : String? = null
        @SerializedName("deviceType") @Expose var deviceType : String? = null
        @SerializedName("name") @Expose var name : String? = null
        @SerializedName("email") @Expose var email : String? = null
        @SerializedName("mobile") @Expose var mobile : String? = null
        @SerializedName("dob") @Expose var dob : String? = null
        @SerializedName("profileImage") @Expose var profileImage : String? = null
        @SerializedName("attributeData") @Expose var attributeData : List<AttributeData>? = null
        @SerializedName("rockStrength") @Expose var rockStrength : List<RockStrength>? = null
        @SerializedName("sampleCollected") @Expose var sampleCollected : List<SampleCollected>? = null
        @SerializedName("typeOfFaults") @Expose var typeOfFaults : List<TypeOfFaults>? = null
        @SerializedName("typeOfGeologicalStructures") @Expose
        var typeOfGeologicalStructures : List<TypeOfGeologicalStructures>? = null
        @SerializedName("waterCondition") @Expose var waterCondition : List<WaterCondition>? = null
        @SerializedName("weatheringData") @Expose var weatheringData : List<WeatheringData>? = null

        class AttributeData {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("name") @Expose var name : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
            @SerializedName("nos") @Expose var nos : List<Nos>? = null

            class Nos {
                @SerializedName("id") @Expose var id : Int? = null
                @SerializedName("attributeId") @Expose var attributeId : Int? = null
                @SerializedName("name") @Expose var name : String? = null
                @SerializedName("created_at") @Expose var createdAt : String? = null
                @SerializedName("updated_at") @Expose var updatedAt : String? = null
            }
        }

        class RockStrength {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("name") @Expose var name : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
        }

        class SampleCollected {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("name") @Expose var name : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
        }

        class TypeOfFaults {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("name") @Expose var name : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
        }

        class TypeOfGeologicalStructures {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("name") @Expose var name : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
        }

        class WaterCondition {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("name") @Expose var name : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
        }

        class WeatheringData {
            @SerializedName("id") @Expose var id : Int? = null
            @SerializedName("name") @Expose var name : String? = null
            @SerializedName("created_at") @Expose var createdAt : String? = null
            @SerializedName("updated_at") @Expose var updatedAt : String? = null
        }
    }
}