package com.geomap.mapReportModule.activities.underGroundModule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormThirdStepBinding
import com.geomap.mapReportModule.models.SuccessModel
import com.geomap.mapReportModule.models.UnderGroundInsertModel
import com.geomap.utils.APIClientProfile
import com.geomap.utils.CONSTANTS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit.RetrofitError

class UnderGroundFormThirdStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundFormThirdStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var ugDataModel = UnderGroundInsertModel()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_under_ground_form_third_step)
        ctx = this@UnderGroundFormThirdStepActivity
        act = this@UnderGroundFormThirdStepActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            val gson = Gson()
            val data = intent.getStringExtra("ugData")
            val type1 = object : TypeToken<UnderGroundInsertModel>() {}.type
            ugDataModel = gson.fromJson(data, type1)
        }

        val gson = Gson()
        Log.e("UGData", gson.toJson(ugDataModel).toString())
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
                userId, ugDataModel.name, ugDataModel.comment, ugDataModel.attribute,
                ugDataModel.ugDate,
                ugDataModel.mapSerialNo,
                ugDataModel.shift, ugDataModel.mappedBy, ugDataModel.scale, ugDataModel.location,
                ugDataModel.venieLoad,
                ugDataModel.xCordinate, ugDataModel.yCordinate, ugDataModel.zCordinate,
                ugDataModel.leftImage, ugDataModel.roofImage,
                ugDataModel.rightImage, ugDataModel.faceImage,
                object : retrofit.Callback<SuccessModel> {
                    override fun success(model : SuccessModel,
                        response : retrofit.client.Response) {
                        if (model.responseCode.equals(
                                ctx.getString(R.string.ResponseCodesuccess))) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            showToast(model.responseMessage, act)
                            finish()
                        } else if (model.responseCode.equals(
                                ctx.getString(R.string.ResponseCodefail))) {
                            showToast(model.responseMessage, act)
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