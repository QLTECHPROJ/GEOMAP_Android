package com.geomap.mapReportModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityViewPdfBinding
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService

class ViewPdfActivity : AppCompatActivity() {
    private lateinit var binding : ActivityViewPdfBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var reportType : String? = null
    private var id : String? = null
    private lateinit var viewModel : AllViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_pdf)
        ctx = this@ViewPdfActivity
        act = this@ViewPdfActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            id = intent.extras?.getString("id")
            reportType = intent.extras?.getString("reportType")
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        if (reportType.equals("ug")) {
            binding.tvHeaderTitle.text = getString(R.string.underground_report)
        } else if (reportType.equals("oc")) {
            binding.tvHeaderTitle.text = getString(R.string.opencast_report)
        }
        postData()
    }

    override fun onBackPressed() {
        finish()
    }

    fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            viewModel = ViewModelProvider(this, UserModelFactory(
                UserRepository(retrofitService)))[AllViewModel::class.java]
            viewModel.getPdfView(userId.toString(), id.toString(), reportType.toString())
            viewModel.getPdfView.observe(this) {
                when {
                    it?.ResponseCode == getString(R.string.ResponseCodesuccess) -> {
                        binding.webView.webViewClient = WebViewClient()
                        binding.webView.settings.javaScriptEnabled = true
                        binding.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
                        binding.webView.settings.setSupportZoom(false)
                        binding.webView.settings.pluginState = WebSettings.PluginState.ON
                        binding.webView.settings.displayZoomControls = false
                        binding.webView.webViewClient = CustomWebViewClient()
                        binding.webView.loadUrl(
                            "https://docs.google.com/gview?embedded=true&url=${it.ResponseData.pdfLink}")

                        binding.webView.webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view : WebView, progress : Int) {
                                binding.progressBar.progress = progress
                                if (progress == 100) {
                                    hideProgressBar(binding.progressBar, binding.progressBarHolder,
                                        act)
                                }
                            }
                        }
                        binding.webView.requestFocus()
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

    inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view : WebView, url : String) : Boolean {
            view.loadUrl(url)
            return true
        }
    }
}