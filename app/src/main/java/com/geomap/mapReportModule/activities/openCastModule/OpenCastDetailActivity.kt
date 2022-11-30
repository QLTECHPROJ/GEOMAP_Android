package com.geomap.mapReportModule.activities.openCastModule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastDetailBinding
import com.geomap.mapReportModule.models.OpenCastDetailsModel
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.utils.RetrofitService

class OpenCastDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var id : String? = null
    private var userId : String? = ""
    private lateinit var viewModel : AllViewModel
    private val retrofitService = RetrofitService.getInstance()

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
            callViewPdfActivity(act, "1", "oc", id)
        }
    }

    fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            viewModel = ViewModelProvider(this, UserModelFactory(
                UserRepository(retrofitService)))[AllViewModel::class.java]
            viewModel.getOpenCastDetails(id.toString())
            viewModel.getOpenCastDetails.observe(this) {
                hideProgressBar(binding.progressBar,
                    binding.progressBarHolder, act)
                when {
                    it?.ResponseCode == getString(R.string.ResponseCodesuccess) -> {
                        binding.llMainLayout.visibility = View.VISIBLE
                        binding.btnViewPdf.visibility = View.VISIBLE
                        val ocDetail =
                            OpenCastDetailsModel(it.ResponseCode, it.ResponseData,
                                it.ResponseMessage, it.ResponseStatus)
                        binding.ocDetail = ocDetail
                    }
                    it.ResponseCode == act.getString(R.string.ResponseCodefail) -> {
                        showToast(it.ResponseMessage, act)
                    }
                    it.ResponseCode == act.getString(R.string.ResponseCodeDeleted) -> {
                        callDelete403(act, it.ResponseMessage)
                    }
                }
            }
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}