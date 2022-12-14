package com.geomap.mapReportModule.activities.underGroundModule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.databinding.DataBindingUtil
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormSecondStepBinding
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.attributeDataModelList
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.flagUG
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.ugmr
import com.geomap.mapReportModule.models.AttributeDataModel
import com.geomap.mapReportModule.models.UnderGroundInsertModel
import com.geomap.utils.CONSTANTS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class UnderGroundFormSecondStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundFormSecondStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    var mapSerialNo : String? = ""
    var name : String? = ""
    var shift : String? = ""
    var mappedBy : String? = ""
    var scale : String? = ""
    var location : String? = ""
    var comment : String? = ""
    private var ugDataModel = UnderGroundInsertModel()
    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            ugmr.ugDate = binding.tvUGDate.text.toString()
            ugmr.name = binding.etName.text.toString()
            ugmr.comment = binding.etComment.text.toString()
            ugmr.shift = shift
            ugmr.mappedBy = binding.etMappedBy.text.toString()
            ugmr.scale = binding.etScale.text.toString()
            ugmr.location = binding.etLocation.text.toString()
            ugmr.veinOrLoad = binding.etVeinLoad.text.toString()
            ugmr.xCordinate = binding.etXCoordinate.text.toString()
            ugmr.yCordinate = binding.etYCoordinate.text.toString()
            ugmr.zCordinate = binding.etZCoordinate.text.toString()
        }

        override fun afterTextChanged(s : Editable) {}
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_under_ground_form_second_step
        )
        ctx = this@UnderGroundFormSecondStepActivity
        act = this@UnderGroundFormSecondStepActivity
        binding.btnNextStep.isEnabled = true
        binding.btnNextStep.setBackgroundResource(R.drawable.enable_button)
        if (intent.extras != null) {
            val gson = Gson()
            val data = intent.getStringExtra("attributeData")
            val type1 = object : TypeToken<ArrayList<AttributeDataModel>>() {}.type
            attributeDataModelList = gson.fromJson(data, type1)
        }
        binding.tvUGDate.text = ugmr.ugDate
        binding.etName.setText(ugmr.name)
        binding.etScale.setText(ugmr.scale)
        binding.etMappedBy.setText(ugmr.mappedBy)
        binding.etLocation.setText(ugmr.location)
        binding.etVeinLoad.setText(ugmr.veinOrLoad)
        binding.etXCoordinate.setText(ugmr.xCordinate)
        binding.etYCoordinate.setText(ugmr.yCordinate)
        binding.etZCoordinate.setText(ugmr.zCordinate)
        binding.etComment.setText(ugmr.comment)
        shift = ugmr.shift
        if (shift == getString(R.string.night_shift)) {
            binding.rbNightShift.isChecked = true
            binding.rbDayShift.isChecked = false
        } else {
            binding.rbNightShift.isChecked = false
            binding.rbDayShift.isChecked = true
        }
        binding.etName.addTextChangedListener(userTextWatcher)
        binding.etMappedBy.addTextChangedListener(userTextWatcher)
        binding.etScale.addTextChangedListener(userTextWatcher)
        binding.etLocation.addTextChangedListener(userTextWatcher)
        binding.etVeinLoad.addTextChangedListener(userTextWatcher)
        binding.etXCoordinate.addTextChangedListener(userTextWatcher)
        binding.etYCoordinate.addTextChangedListener(userTextWatcher)
        binding.etZCoordinate.addTextChangedListener(userTextWatcher)
        binding.etComment.addTextChangedListener(userTextWatcher)

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        if(flagUG == "0") {
            shift = getString(R.string.night_shift)
            binding.rbNightShift.isChecked = true
            binding.rbDayShift.isChecked = false
            binding.tvUGDate.text = SimpleDateFormat(CONSTANTS.DATE_MONTH_YEAR_FORMAT_z).format(
                Date())
        }
        binding.rbRadioGroup.setOnCheckedChangeListener { radioGroup : RadioGroup, id : Int ->
            shift = radioGroup.findViewById<AppCompatRadioButton>(id).text.toString()
        }

        binding.btnNextStep.setOnClickListener {
            val gson = Gson()
            val mapSerialNumber : String = if (flagUG == "1") {
                ugmr.mapSerialNo!!
            } else {
                ""
            }
            ugDataModel = UnderGroundInsertModel(
                attributeDataModelList,
                binding.etName.text.toString(), binding.etComment.text.toString(),
                binding.tvUGDate.text.toString(), mapSerialNumber, shift,
                binding.etMappedBy.text.toString(), binding.etScale.text.toString(),
                binding.etLocation.text.toString(), binding.etVeinLoad.text.toString(),
                binding.etXCoordinate.text.toString(), binding.etYCoordinate.text.toString(),
                binding.etZCoordinate.text.toString(), null, null, null, null
            )

            ugmr.ugDate = binding.tvUGDate.text.toString()
            ugmr.name = binding.etName.text.toString()
            ugmr.comment = binding.etComment.text.toString()
            ugmr.shift = shift
            ugmr.mappedBy = binding.etMappedBy.text.toString()
            ugmr.scale = binding.etScale.text.toString()
            ugmr.location = binding.etLocation.text.toString()
            ugmr.veinOrLoad = binding.etVeinLoad.text.toString()
            ugmr.xCordinate = binding.etXCoordinate.text.toString()
            ugmr.yCordinate = binding.etYCoordinate.text.toString()
            ugmr.zCordinate = binding.etZCoordinate.text.toString()
            val i = Intent(act, UnderGroundFormThirdStepActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            i.putExtra("ugData", gson.toJson(ugDataModel))
            i.putExtra("attributeData", gson.toJson(attributeDataModelList).toString())
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}