package com.geomap.mapReportModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.callOpenCastFormFirstStepActivity
import com.geomap.GeoMapApp.callUnderGroundFormFirstStepActivity
import com.geomap.R
import com.geomap.databinding.ActivityAddReportBinding

class AddReportActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddReportBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_report)
        ctx = this@AddReportActivity
        act = this@AddReportActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnAddUnderGroundsReport.setOnClickListener {
            callUnderGroundFormFirstStepActivity(act, "0")
        }

        binding.btnAddOpenCastReport.setOnClickListener {
            callOpenCastFormFirstStepActivity(act, "0")
        }
    }

    override fun onBackPressed() {
        finish()
    }
}