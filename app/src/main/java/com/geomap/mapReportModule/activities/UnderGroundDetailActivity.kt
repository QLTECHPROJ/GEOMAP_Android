package com.geomap.mapReportModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundDetailBinding
import com.geomap.mapReportModule.models.UnderGroundDetailsModel
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnderGroundDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_under_ground_detail)
        ctx = this@UnderGroundDetailActivity
        act = this@UnderGroundDetailActivity

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
                .getUnderGroundDetails
                .enqueue(object : Callback<UnderGroundDetailsModel> {
                    override fun onResponse(call : Call<UnderGroundDetailsModel>,
                        response : Response<UnderGroundDetailsModel>) {
                        try {
                            hideProgressBar(binding.progressBar,
                                binding.progressBarHolder, act)
                            val model : UnderGroundDetailsModel? = response.body()!!
                            when (model!!.responseCode) {
                                getString(R.string.ResponseCodesuccess) -> {
//                                    binding.tvSerialNo.text = model.responseData.
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

                    override fun onFailure(call : Call<UnderGroundDetailsModel>, t : Throwable) {
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