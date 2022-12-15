package com.geomap.mapReportModule.activities.openCastModule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastDetailDraftBinding
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity
import com.geomap.roomDataBase.OpenCastMappingReport
import com.geomap.utils.CONSTANTS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

        if (intent.extras != null) {
            report = intent.getStringExtra("report")
            val type1 = object : TypeToken<OpenCastMappingReport>() {}.type
            ocReportData = gson.fromJson(report, type1)
            binding.ocDetail = ocReportData
            binding.llMainLayout.visibility = View.VISIBLE

            Glide.with(ctx).load(ocReportData.geologistSign)
                .thumbnail(0.10f).load(binding.imgGeologistSign).submit().get()
            Glide.with(ctx).load(ocReportData.clientsGeologistSign)
                .thumbnail(0.10f).load(binding.imgClientGeologistSign).submit().get()
            Glide.with(ctx).load(ocReportData.image)
                .thumbnail(0.10f).load(binding.image).submit().get()
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
        binding.llEdit.setOnClickListener {
            val i = Intent(act, OpenCastFormFirstStepActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            i.putExtra("flag","detailDraft")
            i.putExtra("data",gson.toJson(ocReportData))
            act.startActivity(i)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}