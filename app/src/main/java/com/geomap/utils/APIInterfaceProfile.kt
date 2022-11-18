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

    @Multipart @POST("/underground_insert") fun postUndergroundInsert(
        @Part("userId") userId : String?,
        @Part("name") name : String?,
        @Part("comment") comment : String?,
        @Part("attribute") attribute : ArrayList<AttributeDataModel>?,
        @Part("ugDate") ugDate : String?,
        @Part("mapSerialNo") mapSerialNo : String?,
        @Part("shift") shift : String?,
        @Part("mappedBy") mappedBy : String?,
        @Part("scale") scale : String?,
        @Part("location") location : String?,
        @Part("venieLoad") venieLoad : String?,
        @Part("xCordinate") xCordinate : String?,
        @Part("yCordinate") yCordinate : String?,
        @Part("zCordinate") zCordinate : String?,
        @Part("roofImage") roofImage : TypedFile?,
        @Part("leftImage") leftImage : TypedFile?,
        @Part("rightImage") rightImage : TypedFile?,
        @Part("faceImage") faceImage : TypedFile?,
        modelCallback : Callback<SuccessModel>?)

    @Multipart @POST("/sync_data") fun postSyncDataInsert(
        @Field("UndergroundReport") undergroundReport : String?,
        @Field("OpenCastReport") openCastReport : String?,
        modelCallback : Callback<SuccessModel>?)
}