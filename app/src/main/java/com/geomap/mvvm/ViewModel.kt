package com.geomap.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geomap.userModule.models.LoginModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel constructor(private val repository : UserRepository) : ViewModel() {

    val postLoginData = MutableLiveData<LoginModel>()
    val errorMessage = MutableLiveData<String>()

    fun postLoginData() {
        val response = repository.postLoginData()
        response.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call : Call<LoginModel>, response : Response<LoginModel>) {
                postLoginData.postValue(response.body())
            }

            override fun onFailure(call : Call<LoginModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}