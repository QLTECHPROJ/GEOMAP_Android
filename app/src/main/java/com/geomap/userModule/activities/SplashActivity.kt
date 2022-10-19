package com.geomap.userModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.callSignActivity
import com.geomap.R
import com.geomap.databinding.ActivitySplashBinding
import com.geomap.mvvm.ViewModel
import com.geomap.utils.RetrofitService

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private lateinit var viewModel : ViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        ctx = this@SplashActivity
        act = this@SplashActivity

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

    private fun callSplashData() {
        Handler(Looper.getMainLooper()).postDelayed({
            callSignActivity("", act)
        }, 1800)
    }
}