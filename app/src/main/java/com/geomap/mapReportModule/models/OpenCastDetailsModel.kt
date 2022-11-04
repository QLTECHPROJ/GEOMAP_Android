package com.geomap.mapReportModule.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OpenCastDetailsModel {
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

        @SerializedName("map_serial_no") @Expose
        var mapSerialNo : String? = null

        @SerializedName("pit_number") @Expose
        var pitNumber : String? = null

        @SerializedName("pit_loaction") @Expose
        var pitLoaction : String? = null

        @SerializedName("shift_incharge_name") @Expose
        var shiftInchargeName : String? = null

        @SerializedName("geologist_name") @Expose
        var geologistName : String? = null

        @SerializedName("mapping_parameter") @Expose
        var mappingParameter : String? = null

        @SerializedName("face_location") @Expose
        var faceLocation : String? = null

        @SerializedName("face_length") @Expose
        var faceLength : String? = null

        @SerializedName("face_area") @Expose
        var faceArea : String? = null

        @SerializedName("face_rock_type") @Expose
        var faceRockType : String? = null

        @SerializedName("bench_rl") @Expose
        var benchRl : String? = null

        @SerializedName("bench_height_width") @Expose
        var benchHeightWidth : String? = null

        @SerializedName("bench_angle") @Expose
        var benchAngle : String? = null

        @SerializedName("thickness_of_ore") @Expose
        var thicknessOfOre : String? = null

        @SerializedName("thickness_of_overburdan") @Expose
        var thicknessOfOverburdan : String? = null

        @SerializedName("thickness_of_interburn") @Expose
        var thicknessOfInterburn : String? = null

        @SerializedName("observed_grade") @Expose
        var observedGrade : String? = null

        @SerializedName("sample_colledted") @Expose
        var sampleColledted : String? = null

        @SerializedName("actual_grade") @Expose
        var actualGrade : String? = null

        @SerializedName("weathring") @Expose
        var weathring : String? = null

        @SerializedName("rock_stregth") @Expose
        var rockStregth : String? = null

        @SerializedName("water_condition") @Expose
        var waterCondition : String? = null

        @SerializedName("type_of_geologist") @Expose
        var typeOfGeologist : String? = null

        @SerializedName("type_of_faults") @Expose
        var typeOfFaults : String? = null

        @SerializedName("description") @Expose
        var description : String? = null

        @SerializedName("location") @Expose
        var location : String? = null

        @SerializedName("country") @Expose
        var country : String? = null

        @SerializedName("created_at") @Expose
        var createdAt : String? = null

        @SerializedName("updated_at") @Expose
        var updatedAt : String? = null

        @SerializedName("open_castattribute") @Expose
        var openCastattribute : List<OpenCastattribute>? = null

        class OpenCastattribute {
            @SerializedName("id") @Expose
            var id : Int? = null

            @SerializedName("openCastId") @Expose
            var openCastId : Int? = null

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