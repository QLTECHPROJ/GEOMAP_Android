package com.geomap.mapReportModule.activities.openCastModule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastDetailBinding
import com.geomap.mapReportModule.models.OpenCastDetailsModel
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.google.gson.Gson

class OpenCastDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var mappingSheetNo : String? = null
    private var userId : String? = null
    private lateinit var viewModel : AllViewModel
    private lateinit var model : OpenCastDetailsModel
    val gson = Gson()
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_detail)
        ctx = this@OpenCastDetailActivity
        act = this@OpenCastDetailActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            mappingSheetNo = intent.extras?.getString("id")
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
        binding.llEdit.setOnClickListener {
            val i = Intent(act, OpenCastFormFirstStepActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            i.putExtra("flag","detail")
            i.putExtra("data",gson.toJson(model))
            act.startActivity(i)
        }
        postData()

        binding.btnViewPdf.setOnClickListener {
            callViewPdfActivity(act, "1", "oc", mappingSheetNo)
        }
    }

    fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            viewModel = ViewModelProvider(this, UserModelFactory(
                UserRepository(retrofitService)))[AllViewModel::class.java]
            viewModel.getOpenCastDetails(mappingSheetNo.toString())
            viewModel.getOpenCastDetails.observe(this) {
                hideProgressBar(binding.progressBar,
                    binding.progressBarHolder, act)
                when {
                    it?.ResponseCode == getString(R.string.ResponseCodesuccess) -> {
                        model = it
                        binding.llMainLayout.visibility = View.VISIBLE
                        binding.btnViewPdf.visibility = View.VISIBLE
                        val ocDetail =
                            OpenCastDetailsModel(it.ResponseCode, it.ResponseData,
                                it.ResponseMessage, it.ResponseStatus)
                        binding.ocDetail = ocDetail

                        Glide.with(ctx).load(ocDetail.ResponseData.geologistSign)
                            .thumbnail(0.10f).into(binding.imgGeologistSign)
                        Glide.with(ctx).load(ocDetail.ResponseData.clientsGeologistSign)
                            .thumbnail(0.10f).into(binding.imgClientGeologistSign)
                        Glide.with(ctx).load(ocDetail.ResponseData.image)
                            .thumbnail(0.10f).into(binding.image)

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