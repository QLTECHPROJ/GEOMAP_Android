package com.geomap.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geomap.faqModule.models.FaqListModel
import com.geomap.mapReportModule.models.*
import com.geomap.userModule.models.ContactUsModel
import com.geomap.userModule.models.UserCommonDataModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllViewModel constructor(private val repository : UserRepository) : ViewModel() {
    val postLoginData = MutableLiveData<UserCommonDataModel>()
    val postForgotPassword = MutableLiveData<SuccessModel>()
    val postContactUs = MutableLiveData<ContactUsModel>()
    val postDeleteUser = MutableLiveData<SuccessModel>()
    val userDetails = MutableLiveData<UserCommonDataModel>()
    val faqLists = MutableLiveData<FaqListModel>()
    val getURViewAllListing = MutableLiveData<DashboardViewAllModel>()
    val getPdfView = MutableLiveData<PdfViewModel>()
    val getORViewAllListing = MutableLiveData<DashboardViewAllModel>()
    val getUnderGroundDetails = MutableLiveData<UnderGroundDetailsModel>()
    val getOpenCastDetails = MutableLiveData<OpenCastDetailsModel>()
    val errorMessage = MutableLiveData<String>()

    fun postLoginData(userName : String, password : String,
        deviceToken : String, deviceId : String, deviceType : String) {
        val response =
            repository.postLoginData(userName, password, deviceToken, deviceId, deviceType)
        response.enqueue(object : Callback<UserCommonDataModel> {
            override fun onResponse(call : Call<UserCommonDataModel>,
                response : Response<UserCommonDataModel>) {
                postLoginData.postValue(response.body())
            }

            override fun onFailure(call : Call<UserCommonDataModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun postForgotPassword(email : String) {
        val response =
            repository.postForgotPassword(email)
        response.enqueue(object : Callback<SuccessModel> {
            override fun onResponse(call : Call<SuccessModel>,
                response : Response<SuccessModel>) {
                postForgotPassword.postValue(response.body())
            }

            override fun onFailure(call : Call<SuccessModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun postDeleteUser(email : String) {
        val response =
            repository.postDeleteUser(email)
        response.enqueue(object : Callback<SuccessModel> {
            override fun onResponse(call : Call<SuccessModel>,
                response : Response<SuccessModel>) {
                postDeleteUser.postValue(response.body())
            }

            override fun onFailure(call : Call<SuccessModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

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

    fun postContactUs(userId : String, name : String, mobile : String, email : String,
        subject : String, message : String) {
        val response = repository.postContactUs(userId, name, mobile, email, subject, message)
        response.enqueue(object : Callback<ContactUsModel> {
            override fun onResponse(call : Call<ContactUsModel>,
                response : Response<ContactUsModel>) {
                postContactUs.postValue(response.body())
            }

            override fun onFailure(call : Call<ContactUsModel>, t : Throwable) {
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

    fun getUnderGroundDetails(mapSerialNo : String) {
        val response = repository.getUnderGroundDetails(mapSerialNo)
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

    fun getOpenCastDetails(mappingSheetNo : String) {
        val response = repository.getOpenCastDetails(mappingSheetNo)
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

    fun getPdfView(userId : String, id : String, reportType : String) {
        val response = repository.getPdfView(userId, id, reportType)
        response.enqueue(object : Callback<PdfViewModel> {
            override fun onResponse(call : Call<PdfViewModel>,
                response : Response<PdfViewModel>) {
                getPdfView.postValue(response.body())
            }

            override fun onFailure(call : Call<PdfViewModel>, t : Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}