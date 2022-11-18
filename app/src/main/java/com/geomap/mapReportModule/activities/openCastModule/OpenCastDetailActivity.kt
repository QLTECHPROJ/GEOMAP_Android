package com.geomap.mapReportModule.activities.openCastModule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastDetailBinding
import com.geomap.mapReportModule.models.OpenCastDetailsModel
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenCastDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var id : String? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_detail)
        ctx = this@OpenCastDetailActivity
        act = this@OpenCastDetailActivity

        if (intent.extras != null) {
            id = intent.extras?.getString("id")
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        postData()

        binding.btnViewPdf.setOnClickListener {
            callViewPdfActivity(act, "1")
        }
    }

    fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            RetrofitService.getInstance()
                .getOpenCastDetails(id)
                .enqueue(object : Callback<OpenCastDetailsModel> {
                    override fun onResponse(call : Call<OpenCastDetailsModel>,
                        response : Response<OpenCastDetailsModel>) {
                        try {
                            hideProgressBar(binding.progressBar,
                                binding.progressBarHolder, act)
                            val model : OpenCastDetailsModel? = response.body()!!
                            when (model!!.ResponseCode) {
                                getString(R.string.ResponseCodesuccess) -> {
                                    binding.llMainLayout.visibility = View.VISIBLE
                                    binding.btnViewPdf.visibility = View.VISIBLE
                                    val ocDetail =
                                        OpenCastDetailsModel(model.ResponseCode, model.ResponseData,
                                            model.ResponseMessage, model.ResponseStatus)
                                    binding.ocDetail = ocDetail
                                }
                                getString(R.string.ResponseCodefail) -> {
                                    showToast(model.ResponseMessage, act)
                                }
                                getString(R.string.ResponseCodeDeleted) -> {
                                    callDelete403(act, model.ResponseMessage)
                                }
                            }
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call : Call<OpenCastDetailsModel>, t : Throwable) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder,
                            act)
                    }
                })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}