package com.geomap.mapReportModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.R
import com.geomap.databinding.ActivitySyncDataBinding

class SyncDataActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySyncDataBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync_data)
        ctx = this@SyncDataActivity
        act = this@SyncDataActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnViewPdf.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}