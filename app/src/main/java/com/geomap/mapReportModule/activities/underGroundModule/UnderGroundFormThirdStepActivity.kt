package com.geomap.mapReportModule.activities.underGroundModule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormThirdStepBinding
import com.geomap.mapReportModule.models.SuccessModel
import com.geomap.utils.APIClientProfile
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import retrofit.RetrofitError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnderGroundFormThirdStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundFormThirdStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_under_ground_form_third_step)
        ctx = this@UnderGroundFormThirdStepActivity
        act = this@UnderGroundFormThirdStepActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {
            postUndergroundInsert()
        }
    }

    private fun postUndergroundInsert() {
        if (isNetworkConnected(ctx)) {
//            addJpgSignatureToGallery(binding.signPad.signatureBitmap)
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            APIClientProfile.apiService!!.postUndergroundInsert(
                userId, "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", ""
                ,
                object : retrofit.Callback<SuccessModel> {
                    override fun success(model : SuccessModel,
                        response : retrofit.client.Response) {
                        if (model.responseCode.equals(
                                ctx.getString(R.string.ResponseCodesuccess))) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            showToast(model.responseMessage, act)
                            finish()
                        } else if (model.responseCode.equals(
                                ctx.getString(R.string.ResponseCodeDeleted))) {
                            callDelete403(act, model.responseMessage)
                        }
                    }

                    override fun failure(e : RetrofitError) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        showToast(e.message, act)
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