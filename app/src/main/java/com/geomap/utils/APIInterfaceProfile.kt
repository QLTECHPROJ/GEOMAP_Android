package com.geomap.utils

import com.geomap.mapReportModule.models.SuccessModel
import retrofit.Callback
import retrofit.http.Multipart
import retrofit.http.POST
import retrofit.http.Part
import retrofit.mime.TypedFile

interface APIInterfaceProfile {
    @Multipart @POST("/profile_update") fun getProfileUpdate(
        @Part("id") id : String?,
        @Part("name") name : String?,
        @Part("email") email : String?,
        @Part("dob") dob : String?,
        @Part("mobile") mobile : String?,
        @Part("profileimage") profileimage : TypedFile?,
        addProfileModelCallback : Callback<SuccessModel>?)

}