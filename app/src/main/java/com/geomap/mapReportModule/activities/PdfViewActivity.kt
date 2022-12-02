package com.geomap.mapReportModule.activities

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.geomap.GeoMapApp
import com.geomap.R
import com.geomap.databinding.ActivityPdfViewBinding
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import java.text.SimpleDateFormat
import java.util.*

class PdfViewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPdfViewBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var reportType : String? = ""
    private var downloadUrl : String? = ""
    private var id : String? = ""
    private lateinit var viewModel : AllViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pdf_view)
        ctx = this@PdfViewActivity
        act = this@PdfViewActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            id = intent.extras?.getString("id")
            reportType = intent.extras?.getString("reportType")
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.llDownload.setOnClickListener {
            DownlaodTask(downloadUrl, "GEO_MAP_${
                SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(Date())
            }")
        }

        postData()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun DownlaodTask(downloadUrl : String?, title : String) {
        val req = DownloadManager.Request(Uri.parse(downloadUrl))
        req.setTitle(title)
        req.allowScanningByMediaScanner()
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title.pdf")
        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        req.setMimeType("application/pdf")
        req.allowScanningByMediaScanner()
        req.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        manager.enqueue(req)
    }

    fun postData() {
        if (GeoMapApp.isNetworkConnected(ctx)) {
            GeoMapApp.showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            viewModel = ViewModelProvider(this, UserModelFactory(
                UserRepository(retrofitService)))[AllViewModel::class.java]
            viewModel.getPdfView(userId.toString(), id.toString(), reportType.toString())
            viewModel.getPdfView.observe(this) {
                when {
                    it?.ResponseCode == getString(R.string.ResponseCodesuccess) -> {
                        downloadUrl = it.ResponseData.pdfLink
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
                                    GeoMapApp.hideProgressBar(binding.progressBar,
                                        binding.progressBarHolder,
                                        act)
                                }
                            }
                        }
                        binding.webView.requestFocus()
                    }
                    it.ResponseCode == act.getString(R.string.ResponseCodefail) -> {
                        GeoMapApp.showToast(it.ResponseMessage, act)
                    }
                    it.ResponseCode == act.getString(R.string.ResponseCodeDeleted) -> {
                        GeoMapApp.callDelete403(act, it.ResponseMessage)
                    }
                }
            }
        } else {
            GeoMapApp.showToast(getString(R.string.no_server_found), act)
        }
    }

    inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view : WebView, url : String) : Boolean {
            view.loadUrl(url)
            return true
        }
    }
}