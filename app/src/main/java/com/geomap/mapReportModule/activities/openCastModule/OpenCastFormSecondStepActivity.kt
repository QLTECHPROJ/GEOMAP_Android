package com.geomap.mapReportModule.activities.openCastModule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.allDisable
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastFormSecondStepBinding
import com.geomap.mapReportModule.models.OpenCastInsertModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OpenCastFormSecondStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastFormSecondStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private  var ocData = OpenCastInsertModel()
    var desc : String? = ""

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            desc = binding.edtDescription.text.toString()

            if (desc.equals("")) {
                allDisable(binding.btnSubmit)
            } else {
                binding.btnSubmit.isEnabled = true
                binding.btnSubmit.setBackgroundResource(R.drawable.enable_button)
            }
        }

        override fun afterTextChanged(s : Editable) {}
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_form_second_step)
        ctx = this@OpenCastFormSecondStepActivity
        act = this@OpenCastFormSecondStepActivity

        if(intent.extras != null){
            var gson = Gson()
            var data = intent.getStringExtra("ocData")
            val type1 = object : TypeToken<OpenCastInsertModel>() {}.type
            ocData = gson.fromJson(data, type1)
        }
        Log.e("OcData",ocData.minesSiteName.toString())
        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.edtDescription.addTextChangedListener(userTextWatcher)

        binding.btnSubmit.setOnClickListener {
            finish()
        }

    }

    override fun onBackPressed() {
        finish()
    }
}