package com.geomap.mvvm

import com.geomap.utils.RetrofitService

class UserRepository constructor(private val retrofitService : RetrofitService) {

    fun postLoginData(userName : String, password : String,
        deviceToken : String, deviceId : String, deviceType : String) =
        retrofitService.postLoginData(userName, password, deviceToken, deviceId, deviceType)

    fun postForgotPassword(email : String) = retrofitService.postForgotPassword(email)

    fun postContactUs(userId : String, name : String, mobile : String, email : String,
        subject : String, message : String) =
        retrofitService.postContactUs(userId, name, mobile, email, subject, message)

    fun postDeleteUser(userId : String) = retrofitService.postDeleteUser(userId)

    fun getUserDetails(userId : String) = retrofitService.getUserDetails(userId)

    fun getURViewAllListing(userId : String) = retrofitService.getURViewAllListing(userId)

    fun getORViewAllListing(userId : String) = retrofitService.getORViewAllListing(userId)

    fun getUnderGroundDetails(id : String) = retrofitService.getUnderGroundDetails(id)

    fun getOpenCastDetails(id : String) = retrofitService.getOpenCastDetails(id)

    fun getPdfView(userId : String, id : String, reportType : String) =
        retrofitService.getPdfView(userId, id, reportType)

    fun faqLists() = retrofitService.faqLists
}