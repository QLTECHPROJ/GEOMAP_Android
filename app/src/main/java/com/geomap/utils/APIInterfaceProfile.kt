package com.geomap.utils

import com.geomap.mapReportModule.models.AttributeDataModel
import com.geomap.mapReportModule.models.SuccessModel
import com.geomap.userModule.models.ProfileUpdateModel
import retrofit.Callback
import retrofit.http.Multipart
import retrofit.http.POST
import retrofit.http.Part
import retrofit.mime.TypedFile
import retrofit2.http.Field
import java.util.ArrayList

interface APIInterfaceProfile {
    @Multipart @POST("/profile_update") fun getProfileUpdate(
        @Part("userId") userId : String?,
        @Part("name") name : String?,
        @Part("email") email : String?,
        @Part("dob") dob : String?,
        @Part("mobile") mobile : String?,
        @Part("profileimage") profileimage : TypedFile?,
        addProfileModelCallback : Callback<ProfileUpdateModel>?)

    @Multipart @POST("/open_cast_insert") fun postOpenCastInsert(
        @Part("userId") userId : String?,
        @Part("minesSiteName") minesSiteName : String?,
        @Part("mappingSheetNo") mappingSheetNo : String?,
        @Part("pitName") pitName : String?,
        @Part("pitLoaction") pitLoaction : String?,
        @Part("shiftInchargeName") shiftInchargeName : String?,
        @Part("geologistName") geologistName : String?,
        @Part("faceLocation") faceLocation : String?,
        @Part("faceLength") faceLength : String?,
        @Part("faceArea") faceArea : String?,
        @Part("faceRockType") faceRockType : String?,
        @Part("benchRl") benchRl : String?,
        @Part("benchHeightWidth") benchHeightWidth : String?,
        @Part("benchAngle") benchAngle : String?,
        @Part("thicknessOfOre") thicknessOfOre : String?,
        @Part("thicknessOfOverburdan") thicknessOfOverburdan : String?,
        @Part("thicknessOfInterburden") thicknessOfInterburden : String?,
        @Part("observedGradeOfOre") observedGradeOfOre : String?,
        @Part("actualGradeOfOre") actualGradeOfOre : String?,
        @Part("sampleColledted") sampleColledted : String?,
        @Part("weathring") weathring : String?,
        @Part("rockStregth") rockStregth : String?,
        @Part("waterCondition") waterCondition : String?,
        @Part("typeOfGeologist") typeOfGeologist : String?,
        @Part("typeOfFaults") typeOfFaults : String?,
        @Part("notes") notes : String?,
        @Part("shift") shift : String?,
        @Part("ocDate") ocDate : String?,
        @Part("dipDirectionAndAngle") dipDirectionAndAngle : String?,
        @Part("image") image : TypedFile?,
        @Part("geologistSign") geologistSign : TypedFile?,
        @Part("clientsGeologistSign") clientsGeologistSign : TypedFile?,
        modelCallback : Callback<SuccessModel>?)

    @Multipart @POST("underground_insert") fun postUndergroundInsert(
        @Field("userId") userId : String?,
        @Field("name") name : String?,
        @Field("comment") comment : String?,
        @Field("attribute") attribute : ArrayList<AttributeDataModel>?,
        @Field("ugDate") ugDate : String?,
        @Field("mapSerialNo") mapSerialNo : String?,
        @Field("shift") shift : String?,
        @Field("mappedBy") mappedBy : String?,
        @Field("scale") scale : String?,
        @Field("location") location : String?,
        @Field("venieLoad") venieLoad : String?,
        @Field("xCordinate") xCordinate : String?,
        @Field("yCordinate") yCordinate : String?,
        @Field("zCordinate") zCordinate : String?,
        @Field("leftImage") leftImage : TypedFile?,
        @Field("roofImage") roofImage : TypedFile?,
        @Field("rightImage") rightImage : TypedFile?,
        @Field("faceImage") faceImage : TypedFile?,
        modelCallback : Callback<SuccessModel>?)
}