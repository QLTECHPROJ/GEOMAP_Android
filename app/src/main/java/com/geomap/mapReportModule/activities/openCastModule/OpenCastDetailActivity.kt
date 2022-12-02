package com.geomap.mapReportModule.activities.openCastModule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import java.util.*

class OpenCastDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var id : String? = null
    private var userId : String? = null
    private lateinit var viewModel : AllViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_detail)
        ctx = this@OpenCastDetailActivity
        act = this@OpenCastDetailActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            id = intent.extras?.getString("id")
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        postData()

        binding.btnViewPdf.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                showProgressBar(binding.progressBar, binding.progressBarHolder, act)
                viewModel = ViewModelProvider(this, UserModelFactory(
                    UserRepository(retrofitService)))[AllViewModel::class.java]
                viewModel.getPdfView(userId.toString(), id.toString(), "oc")
                viewModel.getPdfView.observe(this) {
                    hideProgressBar(binding.progressBar,
                        binding.progressBarHolder, act)
                    when {
                        it?.ResponseCode == getString(R.string.ResponseCodesuccess) -> {
                          /*  val format =
                                "https://docs.google.com/viewerng/viewer?embedded=true&url=%s"
                            val fullPath : String =
                                java.lang.String.format(Locale.ENGLISH, format,
                                    it.ResponseData.pdfLink)*/
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.ResponseData.pdfLink))
                            startActivity(browserIntent)
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