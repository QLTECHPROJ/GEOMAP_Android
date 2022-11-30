package com.geomap.mapReportModule.activities.openCastModule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastDetailBinding
import com.geomap.databinding.ActivityOpenCastDetailDraftBinding
import com.geomap.mapReportModule.models.AttributeDataModel
import com.geomap.mapReportModule.models.OpenCastDetailsModel
import com.geomap.roomDataBase.OpenCastMappingReport
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class OpenCastDetailDraftActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastDetailDraftBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var report : String? = null
    var ocReportData = OpenCastMappingReport()
    val gson = Gson()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_detail_draft)
        ctx = this@OpenCastDetailDraftActivity
        act = this@OpenCastDetailDraftActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null){
            report = intent.getStringExtra("report")
            val type1 = object : TypeToken<OpenCastMappingReport>() {}.type
            ocReportData = gson.fromJson(report, type1)
            binding.ocDetail = ocReportData
            binding.llMainLayout.visibility = View.VISIBLE
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}