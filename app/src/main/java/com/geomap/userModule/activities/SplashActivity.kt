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
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.geomap.BuildConfig
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivitySplashBinding
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.roomDataBase.GeoMapDatabase
import com.geomap.userModule.models.VersionModel
import com.geomap.utils.AppSignatureHashHelper
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
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
    private var userId : String? = ""
    private var key : String? = ""
    private lateinit var viewModel : AllViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        ctx = this@SplashActivity
        act = this@SplashActivity

        DB = Room.databaseBuilder(ctx, GeoMapDatabase::class.java, "GeoMap_database").build()
        callFCMRegMethod(ctx)
        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")
        key = AppSignatureHashHelper(this).appSignatures[0]

        callFCMRegMethod(ctx)

        if (key.equals("")) {
            key = getKey(ctx)
        }
    }

    override fun onResume() {
        checkAppVersion()
        super.onResume()
    }

    private fun checkAppVersion() {
        if (fcmId.equals("")) {
            if (TextUtils.isEmpty(fcmId)) {
                Log.e("Token called", ctx.toString())
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task : Task<String?> ->
                    if (!task.isSuccessful) {
                        return@addOnCompleteListener
                    }
                    val token = task.result
                    fcmId = token
                    val editor = getContext().getSharedPreferences(CONSTANTS.FCMToken,
                        MODE_PRIVATE).edit()
                    editor.putString(CONSTANTS.Token, token) //Friend
                    editor.apply()
                }
            }
        }
        val sharedPreferences2 = ctx.getSharedPreferences(CONSTANTS.FCMToken, MODE_PRIVATE)
        fcmId = sharedPreferences2.getString(CONSTANTS.Token, "")

        Log.e("Token", fcmId)
        val simpleDateFormat1 = SimpleDateFormat("hh:mm a")
        simpleDateFormat1.timeZone = TimeZone.getDefault()
        timezoneName = simpleDateFormat1.timeZone.id
        fcmId = getSharedPreferences(CONSTANTS.FCMToken, Context.MODE_PRIVATE).getString(
            CONSTANTS.Token, "").toString()

        if (fcmId == "") {
            callFCMRegMethod(act)
        }

        val deviceId = Settings.Secure.getString(getContext().contentResolver,
            Settings.Secure.ANDROID_ID)
        Log.e("deviceId", deviceId)
        Log.e("Token", fcmId)
        val appURI = "https://play.google.com/store/apps/details?id=com.geomap"
        if (isNetworkConnected(ctx)) {
            RetrofitService.getInstance().getAppVersions(/*"1", */
                BuildConfig.VERSION_CODE.toString(), CONSTANTS.FLAG_ONE, fcmId,
                deviceId).enqueue(object : Callback<VersionModel> {
                override fun onResponse(call : Call<VersionModel>,
                    response : Response<VersionModel>) {
                    try {
                        val versionModel : VersionModel = response.body()!!
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
                                editor1.putString(CONSTANTS.isForce,
                                    versionModel.ResponseData.IsForce)
                                editor1.apply()
                                when (versionModel.ResponseData.IsForce) {
                                    "0" -> {
                                        AlertDialog.Builder(ctx).setTitle(
                                            "Update Geo Map App").setCancelable(
                                            false).setMessage(
                                            "Geo Map App recommends that you update to the latest version")
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
                                            "To keep using Geo Map App, download the latest version")
                                            .setCancelable(
                                                false).setPositiveButton(
                                            "UPDATE") { _ : DialogInterface?, _ : Int ->
                                            startActivity(Intent(Intent.ACTION_VIEW,
                                                Uri.parse(appURI)))
                                        }.create().show()
                                    }
                                    "" -> {
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
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call : Call<VersionModel>, t : Throwable) {

                }
            })
        } else {
            callSplashData()
        }
    }

    private fun callSplashData() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (userId != "") {
                if (isNetworkConnected(ctx)) {
                    viewModel = ViewModelProvider(this, UserModelFactory(
                        UserRepository(retrofitService,
                            userId.toString())))[AllViewModel::class.java]

                    viewModel.getUserDetails()
                    viewModel.userDetails.observe(this) {
                        when {
                            it?.responseCode == getString(R.string.ResponseCodesuccess) -> {
                                saveLoginData(it.responseData, ctx, "1", act)
                            }
                            it.responseCode == act.getString(R.string.ResponseCodefail) -> {
                                showToast(it.responseMessage, act)
                            }
                            it.responseCode == act.getString(R.string.ResponseCodeDeleted) -> {
                                callDelete403(act, it.responseMessage)
                            }
                        }
                    }
                } else {
                    callDashboardActivity(act, "0")
                }
            } else {
                callSignActivity("", act)
            }
        }, 1600)
    }
}