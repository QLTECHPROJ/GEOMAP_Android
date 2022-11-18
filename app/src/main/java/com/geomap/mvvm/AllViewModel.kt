package com.geomap.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geomap.faqModule.models.FaqListModel
import com.geomap.mapReportModule.models.DashboardViewAllModel
import com.geomap.mapReportModule.models.OpenCastDetailsModel
import com.geomap.mapReportModule.models.UnderGroundDetailsModel
import com.geomap.userModule.models.UserCommonDataModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllViewModel constructor(private val repository : UserRepository) : ViewModel() {
    val userDetails = MutableLiveData<UserCommonDataModel>()
    val faqLists = MutableLiveData<FaqListModel>()
    val getURViewAllListing = MutableLiveData<DashboardViewAllModel>()
    val getORViewAllListing = MutableLiveData<DashboardViewAllModel>()
    val getUnderGroundDetails = MutableLiveData<UnderGroundDetailsModel>()
    val getOpenCastDetails = MutableLiveData<OpenCastDetailsModel>()
    val errorMessage = MutableLiveData<String>()

    fun getUserDetails(userId : String) {
        val response = repository.getUserDetails(userId)
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

    fun faqLists() {
        val response = repository.faqLists()
        response.enqueue(object : Callback<FaqListModel> {
            override fun onResponse(call : Call<FaqListModel>,
                response : Response<FaqListModel>) {
                faqLists.postValue(response.body())
            }

            override fun onFailure(call : Call<FaqListModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getURViewAllListing(userId : String) {
        val response = repository.getURViewAllListing(userId)
        response.enqueue(object : Callback<DashboardViewAllModel> {
            override fun onResponse(call : Call<DashboardViewAllModel>,
                response : Response<DashboardViewAllModel>) {
                getURViewAllListing.postValue(response.body())
            }

            override fun onFailure(call : Call<DashboardViewAllModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getORViewAllListing(userId : String) {
        val response = repository.getORViewAllListing(userId)
        response.enqueue(object : Callback<DashboardViewAllModel> {
            override fun onResponse(call : Call<DashboardViewAllModel>,
                response : Response<DashboardViewAllModel>) {
                getORViewAllListing.postValue(response.body())
            }

            override fun onFailure(call : Call<DashboardViewAllModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getUnderGroundDetails(id : String) {
        val response = repository.getUnderGroundDetails(id)
        response.enqueue(object : Callback<UnderGroundDetailsModel> {
            override fun onResponse(call : Call<UnderGroundDetailsModel>,
                response : Response<UnderGroundDetailsModel>) {
                getUnderGroundDetails.postValue(response.body())
            }

            override fun onFailure(call : Call<UnderGroundDetailsModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getOpenCastDetails(id : String) {
        val response = repository.getOpenCastDetails(id)
        response.enqueue(object : Callback<OpenCastDetailsModel> {
            override fun onResponse(call : Call<OpenCastDetailsModel>,
                response : Response<OpenCastDetailsModel>) {
                getOpenCastDetails.postValue(response.body())
            }

            override fun onFailure(call : Call<OpenCastDetailsModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}