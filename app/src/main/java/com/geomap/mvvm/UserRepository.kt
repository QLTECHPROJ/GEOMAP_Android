package com.geomap.mvvm

import com.geomap.utils.RetrofitService

class UserRepository constructor(private val retrofitService : RetrofitService,
    private val userId : String) {

    fun postLoginData() = retrofitService.postLoginData(userId,"","","","")


    fun getUserDetails() = retrofitService.getUserDetails(userId)
}