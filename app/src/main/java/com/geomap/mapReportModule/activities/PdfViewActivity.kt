package com.geomap.mapReportModule.activities

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.geomap.GeoMapApp.*
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
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var reportType : String? = ""
    private lateinit var binding : ActivityPdfViewBinding
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

        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.builtInZoomControls = false
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true;
        binding.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        binding.webView.clearCache(true)

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.webView.reload()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.llDownload.setOnClickListener {
            downloadTask(
                downloadUrl, "GEO_MAP_${
                    SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(Date())
                }"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        postData()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun downloadTask(downloadUrl : String?, title : String) {
        val req = DownloadManager.Request(Uri.parse(downloadUrl))
        req.setTitle(title)
        req.allowScanningByMediaScanner()
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title.pdf")
        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        req.setMimeType("application/pdf")
        req.allowScanningByMediaScanner()
        req.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI
        )
        manager.enqueue(req)
    }

    fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            viewModel = ViewModelProvider(
                this, UserModelFactory(
                    UserRepository(retrofitService)
                )
            )[AllViewModel::class.java]
            viewModel.getPdfView(userId.toString(), id.toString(), reportType.toString())
            viewModel.getPdfView.observe(this) {
                hideProgressBar(
                    binding.progressBar, binding.progressBarHolder,
                    act
                )
                when {
                    it?.ResponseCode == getString(R.string.ResponseCodesuccess) -> {
                        downloadUrl = it.ResponseData.pdfLink
                        shouldOverrideUrlLoading(binding.webView, it.ResponseData.pdfLink)
                        binding.webView.reload()
                    }
                    it.ResponseCode == getString(R.string.ResponseCodefail) -> {
                        showToast(it.ResponseMessage, act)
                    }
                    it.ResponseCode == getString(R.string.ResponseCodeDeleted) -> {
                        callDelete403(act, it.ResponseMessage)
                    }
                }
            }
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    private fun shouldOverrideUrlLoading(webView : WebView, url : String) : Boolean {
        if (url.endsWith(".pdf") || url.endsWith(".doc") || url.endsWith(".docx")) {
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
            return true
        }
        return false
    }
}