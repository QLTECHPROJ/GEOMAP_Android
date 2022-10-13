package com.geomap.mapReportModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.callViewPdfActivity
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundDetailBinding

class UnderGroundDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_under_ground_detail)
        ctx = this@UnderGroundDetailActivity
        act = this@UnderGroundDetailActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnViewPdf.setOnClickListener {
            callViewPdfActivity(act, "1")
        }
    }

    override fun onBackPressed() {
        finish()
    }
}