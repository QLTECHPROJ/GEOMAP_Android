package com.geomap.userModule.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.BuildConfig
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivitySplashBinding
import com.geomap.mvvm.ViewModel
import com.geomap.userModule.models.VersionModel
import com.geomap.utils.AppSignatureHashHelper
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.google.firebase.FirebaseApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var timezoneName : String? = ""
    private var key : String? = ""
    private lateinit var viewModel : ViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        ctx = this@SplashActivity
        act = this@SplashActivity

        key = AppSignatureHashHelper(this).appSignatures[0]

        callFCMRegMethod(ctx)

        if (key.equals("")) {
            key = getKey(ctx)
        }

        /*viewModel = ViewModelProvider(this,
            UserModelFactory(UserRepository(retrofitService, "custId")))[ViewModel::class.java]

        viewModel.postLoginData()
        viewModel.postLoginData.observe(this) {
            when (it.ResponseCode) {
                getString(R.string.ResponseCodesuccess) -> {
                    callCheckData(it.ResponseData, act, applicationContext)
                }
                act.getString(R.string.ResponseCodefail) -> {
                    showToast(it.ResponseMessage, act)
                }
                act.getString(R.string.ResponseCodeDeleted) -> {
                    callDelete403(act, it.ResponseMessage)
                }
            }
        }*/
    }

    override fun onResume() {
        callSplashData()
        super.onResume()
    }

    private fun checkAppVersion() {
        callFCMRegMethod(act)
        val simpleDateFormat1 = SimpleDateFormat("hh:mm a")
        simpleDateFormat1.timeZone = TimeZone.getDefault()
        timezoneName = simpleDateFormat1.timeZone.id
        fcmId = getSharedPreferences(CONSTANTS.FCMToken, Context.MODE_PRIVATE).getString(
            CONSTANTS.Token, "").toString()
        val deviceId = Settings.Secure.getString(getContext().contentResolver,
            Settings.Secure.ANDROID_ID)
        val appURI = "https://play.google.com/store/apps/details?id=com.geomap"
        if (isNetworkConnected(ctx)) {
            RetrofitService.getInstance().getAppVersions("id",
                BuildConfig.VERSION_CODE.toString(), CONSTANTS.FLAG_ONE, fcmId,
                timezoneName/*"Asia/Kolkata"*/,
                deviceId).enqueue(object : Callback<VersionModel> {
                override fun onResponse(call : Call<VersionModel>,
                    response : Response<VersionModel>) {
                    val versionModel : VersionModel = response.body()!!
                    /*                             callFCMRegMethod(ctx)
                        FirebaseMessaging.getInstance().isAutoInitEnabled = true
                        val shared1 = activity.getSharedPreferences(CONSTANTREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
                        val editor1 = shared1.edit()
                        editor1.putString(CONSTANTS.PREFE_ACCESS_segmentKey, versionModel.ResponseData.segmentKey)
                        editor1.apply()
                        setAnalytics(versionModel.ResponseData.segmentKey, ctx)*/
                    when (versionModel.ResponseCode) {
                        getString(R.string.ResponseCodesuccess) -> {
                            val shared1 = getSharedPreferences(
                                CONSTANTS.PREFE_ACCESS_SplashData, Context.MODE_PRIVATE)
                            val editor1 = shared1.edit()
                            editor1.putString(CONSTANTS.supportTitle,
                                versionModel.ResponseData!!.supportTitle)
                            editor1.putString(CONSTANTS.supportText,
                                versionModel.ResponseData.supportText)
                            editor1.putString(CONSTANTS.supportEmail,
                                versionModel.ResponseData.supportEmail)
                            editor1.putString(CONSTANTS.countryID,
                                versionModel.ResponseData.countryID)
                            editor1.putString(CONSTANTS.countryCode,
                                "+" + versionModel.ResponseData.countryCode)
                            editor1.putString(CONSTANTS.countryShortName,
                                versionModel.ResponseData.countryShortName)
                            editor1.putInt(CONSTANTS.mobileMaxDigits,
                                versionModel.ResponseData.mobileMaxDigits.toInt())
                            editor1.putInt(CONSTANTS.mobileMinDigits,
                                versionModel.ResponseData.mobileMinDigits.toInt())
                            editor1.putInt(CONSTANTS.postCodeMaxDigits,
                                versionModel.ResponseData.postCodeMaxDigits.toInt())
                            editor1.putInt(CONSTANTS.postCodeMinDigits,
                                versionModel.ResponseData.postCodeMinDigits.toInt())
                            editor1.apply()
                            when (versionModel.ResponseData.IsForce) {
                                "0" -> {
                                    AlertDialog.Builder(ctx).setTitle(
                                        "Update Camp App").setCancelable(false).setMessage(
                                        "${
                                            ctx.getString(R.string.app_name)
                                        } recommends that you update to the latest version")
                                        .setPositiveButton(
                                            "UPDATE") { dialog : DialogInterface, _ : Int ->
                                            startActivity(Intent(Intent.ACTION_VIEW,
                                                Uri.parse(appURI)))
                                            dialog.cancel()
                                        }.setNegativeButton(
                                            "NOT NOW") { dialog : DialogInterface, _ : Int -> //                                        askBattyPermission()
                                            callSplashData()
                                            dialog.dismiss()
                                        }.create().show()
                                }
                                "1" -> {
                                    AlertDialog.Builder(ctx).setTitle(
                                        "Update Required").setCancelable(false).setMessage(
                                        "To keep using ${
                                            ctx.getString(R.string.app_name)
                                        }, download the latest version").setPositiveButton(
                                        "UPDATE") { _ : DialogInterface?, _ : Int ->
                                        startActivity(Intent(Intent.ACTION_VIEW,
                                            Uri.parse(appURI)))
                                    }.create().show()
                                }
                                "" -> { //                                    askBattyPermission()
                                    callSplashData()
                                }
                            }
                        }
                        getString(R.string.ResponseCodefail) -> {
                            showToast(versionModel.ResponseMessage, act)
                        }
                        getString(R.string.ResponseCodeDeleted) -> {
                            callDelete403(act, versionModel.ResponseMessage)
                        }
                    }
                }

                override fun onFailure(call : Call<VersionModel>, t : Throwable) {
                }
            })
        }
    }

    private fun callSplashData() {
        callFCMRegMethod(act)
        Handler(Looper.getMainLooper()).postDelayed({
            callSignActivity("", act)
        }, 1800)
    }
}