package com.geomap.mapReportModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.allDisable
import com.geomap.GeoMapApp.callUnderGroundFormSecondStepActivity
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormFirstStepBinding

class UnderGroundFormFirstStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundFormFirstStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    var desc : String? = ""

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            desc = binding.edtDescription.text.toString()

            if (desc.equals("")) {
                allDisable(binding.btnNextStep)
            } else {
                binding.btnNextStep.isEnabled = true
                binding.btnNextStep.setBackgroundResource(R.drawable.enable_button)
            }
        }

        override fun afterTextChanged(s : Editable) {}
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_under_ground_form_first_step)
        ctx = this@UnderGroundFormFirstStepActivity
        act = this@UnderGroundFormFirstStepActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.edtDescription.addTextChangedListener(userTextWatcher)

        binding.tvAddAttributes.setOnClickListener {
            binding.llMainLayout.visibility = View.VISIBLE
        }

        binding.btnNextStep.setOnClickListener {
            callUnderGroundFormSecondStepActivity(act, "0")
        }
    }

    override fun onBackPressed() {
        finish()
    }
}