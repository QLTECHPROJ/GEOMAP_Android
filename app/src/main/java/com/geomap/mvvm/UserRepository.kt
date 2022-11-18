package com.geomap.mvvm

import com.geomap.utils.RetrofitService

class UserRepository constructor(private val retrofitService : RetrofitService) {

    fun postLoginData(userId : String) = retrofitService.postLoginData(userId, "", "", "", "")

    fun getUserDetails(userId : String) = retrofitService.getUserDetails(userId)

    fun getURViewAllListing(userId : String) = retrofitService.getURViewAllListing(userId)

    fun getORViewAllListing(userId : String) = retrofitService.getORViewAllListing(userId)

    fun getUnderGroundDetails(id : String) = retrofitService.getUnderGroundDetails(id)

    fun getOpenCastDetails(id : String) = retrofitService.getOpenCastDetails(id)

    fun faqLists() = retrofitService.faqLists
}