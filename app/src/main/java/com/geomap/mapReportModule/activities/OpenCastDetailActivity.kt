package com.geomap.mapReportModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastDetailBinding
import com.geomap.mapReportModule.models.OpenCastDetailsModel
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenCastDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_detail)
        ctx = this@OpenCastDetailActivity
        act = this@OpenCastDetailActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        postData()

        binding.btnViewPdf.setOnClickListener {
            callViewPdfActivity(act, "1")
        }
    }

    private fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            RetrofitService.getInstance()
                .getOpenCastDetails
                .enqueue(object : Callback<OpenCastDetailsModel> {
                    override fun onResponse(call : Call<OpenCastDetailsModel>,
                        response : Response<OpenCastDetailsModel>) {
                        try {
                            hideProgressBar(binding.progressBar,
                                binding.progressBarHolder, act)
                            val model : OpenCastDetailsModel? = response.body()!!
                            when (model!!.responseCode) {
                                getString(R.string.ResponseCodesuccess) -> {

                                }
                                getString(R.string.ResponseCodefail) -> {
                                    showToast(model.responseMessage, act)
                                }
                                getString(R.string.ResponseCodeDeleted) -> {
                                    callDelete403(act, model.responseMessage)
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