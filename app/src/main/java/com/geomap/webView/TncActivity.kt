package com.geomap.webView

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
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

class TncActivity : AppCompatActivity() {
    lateinit var binding : ActivityTncBinding
    private var web : String? = null
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var aboutUs = AppUtils.about_us

    @SuppressLint("SetJavaScriptEnabled") override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.webViewClient = CustomWebViewClient()
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.allowFileAccessFromFileURLs = true
        binding.webView.settings.allowUniversalAccessFromFileURLs = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT

        binding.webView.requestFocus()

        when {
            web.equals(getString(R.string.about_us)) -> {
                binding.tvTitle.setText(R.string.about_us)
                binding.webView.loadUrl(aboutUs)
            }
        }
    }

    private inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view : WebView, url : String) : Boolean {
            view.loadUrl(url)
            return true
        }
    }

    override fun onBackPressed() {
        finish()
    }
}