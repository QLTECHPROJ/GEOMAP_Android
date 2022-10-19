package com.geomap.mvvm

import com.geomap.utils.RetrofitService

class UserRepository constructor(private val retrofitService : RetrofitService,
    private val custId : String) {

    fun postLoginData() = retrofitService.postLoginData(custId)
}