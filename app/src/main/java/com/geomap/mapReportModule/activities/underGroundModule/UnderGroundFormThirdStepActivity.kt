package com.geomap.mapReportModule.activities.underGroundModule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormThirdStepBinding

class UnderGroundFormThirdStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundFormThirdStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_under_ground_form_third_step)
        ctx = this@UnderGroundFormThirdStepActivity
        act = this@UnderGroundFormThirdStepActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}