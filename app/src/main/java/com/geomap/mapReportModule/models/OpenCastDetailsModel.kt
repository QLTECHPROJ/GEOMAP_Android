package com.geomap.mapReportModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OpenCastDetailsModel {
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
        @SerializedName("minesSiteName") @Expose var minesSiteName : String? = null
        @SerializedName("mappingSheetNo") @Expose var mappingSheetNo : String? = null
        @SerializedName("pitName") @Expose var pitName : String? = null
        @SerializedName("pitLoaction") @Expose var pitLoaction : String? = null
        @SerializedName("shiftInchargeName") @Expose var shiftInchargeName : String? = null
        @SerializedName("geologistName") @Expose var geologistName : String? = null
        @SerializedName("mappingParameter") @Expose var mappingParameter : String? = null
        @SerializedName("faceLocation") @Expose var faceLocation : String? = null
        @SerializedName("faceLength") @Expose var faceLength : String? = null
        @SerializedName("faceArea") @Expose var faceArea : String? = null
        @SerializedName("faceRockType") @Expose var faceRockType : String? = null
        @SerializedName("benchRl") @Expose var benchRl : String? = null
        @SerializedName("benchHeightWidth") @Expose var benchHeightWidth : String? = null
        @SerializedName("benchAngle") @Expose var benchAngle : String? = null
        @SerializedName("thicknessOfOre") @Expose var thicknessOfOre : String? = null
        @SerializedName("thicknessOfOverburdan") @Expose var thicknessOfOverburdan : String? = null
        @SerializedName("thicknessOfInterburden") @Expose var thicknessOfInterburden : String? =
            null
        @SerializedName("observedGradeOfOre") @Expose var observedGradeOfOre : String? = null
        @SerializedName("sampleColledted") @Expose var sampleColledted : String? = null
        @SerializedName("actualGradeOfOre") @Expose var actualGradeOfOre : String? = null
        @SerializedName("weathring") @Expose var weathring : String? = null
        @SerializedName("rockStregth") @Expose var rockStregth : String? = null
        @SerializedName("waterCondition") @Expose var waterCondition : String? = null
        @SerializedName("typeOfGeologist") @Expose var typeOfGeologist : String? = null
        @SerializedName("typeOfFaults") @Expose var typeOfFaults : String? = null
        @SerializedName("notes") @Expose var notes : String? = null
        @SerializedName("created_at") @Expose var createdAt : String? = null
        @SerializedName("updated_at") @Expose var updatedAt : String? = null
        @SerializedName("shift") @Expose var shift : String? = null
        @SerializedName("ocDate") @Expose var ocDate : String? = null
        @SerializedName("userId") @Expose var userId : String? = null
        @SerializedName("dipDirectionAndAngle") @Expose var dipDirectionAndAngle : String? = null
        @SerializedName("geologistSign") @Expose var geologistSign : String? = null
        @SerializedName("clientsGeologistSign") @Expose var clientsGeologistSign : String? = null
    }
}