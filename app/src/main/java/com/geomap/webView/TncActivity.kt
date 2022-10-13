package com.geomap.webView

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.R
import com.geomap.databinding.ActivityTncBinding
import com.geomap.utils.AppUtils
import com.geomap.utils.CONSTANTS

/* This is to T & C activity */
class TncActivity : AppCompatActivity() {
    lateinit var binding : ActivityTncBinding
    private var web : String? = null
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var tnc = AppUtils.tncs_url
    private var aboutUs = AppUtils.about_us
    private var privacyPolicy = AppUtils.privacy_policy_url
    private var chatSupport = AppUtils.chat_Support_url
    private var howReferWorks = AppUtils.how_refer_works_url
    private var numStarted = 0
    var stackStatus = 0
    var myBackPress = false

    @SuppressLint("SetJavaScriptEnabled") override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)/* This is the layout showing */
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tnc)
        act = this@TncActivity
        ctx = this@TncActivity

        if (intent != null) {
            web = intent.getStringExtra(CONSTANTS.Web)
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view : WebView, progress : Int) {
                binding.progressBar.progress = progress
                if (progress == 100) {
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            registerActivityLifecycleCallbacks(AppLifecycleCallback())
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.webViewClient = CustomWebViewClient()
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.allowFileAccessFromFileURLs = true
        binding.webView.settings.allowUniversalAccessFromFileURLs = true
        binding.webView.settings.domStorageEnabled = true
//        binding.webView.settings.setAppCacheEnabled(false)
        binding.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT

        binding.webView.requestFocus()

        when {
            web.equals(getString(R.string.about_us)) -> {
                binding.tvTitle.setText(R.string.about_us)
                binding.webView.loadUrl(aboutUs)
            }
            web.equals("Tnc") -> {
                binding.tvTitle.setText(R.string.t_n_csf)
                binding.webView.loadUrl(tnc)
            }
            web.equals("PrivacyPolicy") -> {
                binding.tvTitle.setText(R.string.privacy_policy)
                binding.webView.loadUrl(privacyPolicy)
            }
            web.equals("Chat") -> {
                binding.tvTitle.setText(R.string.chatSupport)

                binding.webView.visibility = View.GONE
                /*if (!coUserId.equals("")) {
                    try {
                        ZohoSalesIQ.registerVisitor(coUserId) //Visitor unique id
                    } catch (e: InvalidVisitorIDException) {
                        e.printStackTrace()
                    }
                    ZohoSalesIQ.Visitor.setName(name)
                    ZohoSalesIQ.Visitor.setEmail(email)
                    ZohoSalesIQ.Visitor.setContactNumber(mobile)
                }*/
                /*binding.webView.clearHistory()
                binding.webView.clearCache(true)
                val chatUrl: String = if (coUserId.equals("")) {
                    chatSupport
                } else {
                    "$chatSupport?id=$coUserId&email=$email&name=$name"
                }

                Log.e("chat Url", chatUrl)
                binding.webView.loadUrl(chatUrl)*/
            }
            web.equals("HowReferWorks") -> {
                binding.tvTitle.setText(R.string.how_refer_works)
                binding.webView.loadUrl(howReferWorks)
            }
        } /*else {
            binding.tvTitle.setText(Web);
            binding.webView.loadUrl(HowReferWorks);
        }*/
    }

    private inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view : WebView, url : String) : Boolean {
            view.loadUrl(url)
            return true
        }
    }

    override fun onBackPressed() {
        myBackPress = true
        finish()
    }

    internal inner class AppLifecycleCallback : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity : Activity, savedInstanceState : Bundle?) {}
        override fun onActivityStarted(activity : Activity) {
            if (numStarted == 0) {
                stackStatus = 1
                Log.e("APPLICATION", "APP IN FOREGROUND") //app went to foreground
            }
            numStarted++
        }

        override fun onActivityResumed(activity : Activity) {}
        override fun onActivityPaused(activity : Activity) {}
        override fun onActivityStopped(activity : Activity) {
            numStarted--
            if (numStarted == 0) {
                if (!myBackPress) {
                    Log.e("APPLICATION", "Back press false")
                    stackStatus = 2
                } else {
                    myBackPress = true
                    stackStatus = 1
                    Log.e("APPLICATION", "back press true ")
                }
                Log.e("APPLICATION", "App is in BACKGROUND") // app went to background
            }
        }

        override fun onActivitySaveInstanceState(activity : Activity, outState : Bundle) {}
        override fun onActivityDestroyed(activity : Activity) {
            if (numStarted == 0 && stackStatus == 2) {
                Log.e("Destroy", "Activity Destroyed")
                /*val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(notificationId)
                GlobalInitExoPlayer.relesePlayer(applicationContext)*/
            } else {
                Log.e("Destroy", "Activity go in main activity")
            }
        }
    }
}