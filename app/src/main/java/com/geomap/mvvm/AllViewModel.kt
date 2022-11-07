package com.geomap.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geomap.userModule.models.UserCommonDataModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllViewModel constructor(private val repository : UserRepository) : ViewModel() {

    val postLoginData = MutableLiveData<UserCommonDataModel>()
    val userDetails = MutableLiveData<UserCommonDataModel>()
    val errorMessage = MutableLiveData<String>()

    fun getUserDetails() {
        val response = repository.getUserDetails()
        response.enqueue(object : Callback<UserCommonDataModel> {
            override fun onResponse(call : Call<UserCommonDataModel>,
                response : Response<UserCommonDataModel>) {
                userDetails.postValue(response.body())
            }

            override fun onFailure(call : Call<UserCommonDataModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun postLoginData() {
        val response = repository.postLoginData()
        response.enqueue(object : Callback<UserCommonDataModel> {
            override fun onResponse(call : Call<UserCommonDataModel>, response : Response<UserCommonDataModel>) {
                postLoginData.postValue(response.body())
            }

            override fun onFailure(call : Call<UserCommonDataModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}