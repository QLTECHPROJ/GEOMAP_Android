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
import com.geomap.GeoMapApp.allDisable
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormSecondStepBinding
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.attributeDataModelList
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
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0
    private var birthYear = 0
    private var ageYear : Int = 0
    private var ageMonth : Int = 0
    private var ageDate : Int = 0
    var mapSerialNo : String? = ""
    var name : String? = ""
    var shift : String? = ""
    var mappedBy : String? = ""
    var scale : String? = ""
    var location : String? = ""
    var veinLoad : String? = ""
    var xCoordinate : String? = ""
    var yCoordinate : String? = ""
    var zCoordinate : String? = ""
    var comment : String? = ""
    var ugDataModel = UnderGroundInsertModel()
    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            mapSerialNo = binding.etMapSerialNo.text.toString()
            name = binding.etName.text.toString()
            mappedBy = binding.etMappedBy.text.toString()
            scale = binding.etScale.text.toString()
            location = binding.etLocation.text.toString()
            veinLoad = binding.etVeinLoad.text.toString()
            xCoordinate = binding.etXCoordinate.text.toString()
            yCoordinate = binding.etYCoordinate.text.toString()
            zCoordinate = binding.etZCoordinate.text.toString()
            comment = binding.etComment.text.toString()

            when {
                mapSerialNo.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }
                name.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }
                mappedBy.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }
                scale.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }
                location.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }
                veinLoad.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }
                xCoordinate.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }
                yCoordinate.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }
                zCoordinate.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }
                comment.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }
                else -> {
                    binding.btnNextStep.isEnabled = true
                    binding.btnNextStep.setBackgroundResource(R.drawable.enable_button)
                }
            }
        }

        override fun afterTextChanged(s : Editable) {}
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_under_ground_form_second_step)
        ctx = this@UnderGroundFormSecondStepActivity
        act = this@UnderGroundFormSecondStepActivity
        if (intent.extras != null) {
            val gson = Gson()
            val data = intent.getStringExtra("attributeData")
            val type1 = object : TypeToken<ArrayList<AttributeDataModel>>() {}.type
            attributeDataModelList = gson.fromJson(data, type1)
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.etMapSerialNo.addTextChangedListener(userTextWatcher)
        binding.etName.addTextChangedListener(userTextWatcher)
        binding.etMappedBy.addTextChangedListener(userTextWatcher)
        binding.etScale.addTextChangedListener(userTextWatcher)
        binding.etLocation.addTextChangedListener(userTextWatcher)
        binding.etVeinLoad.addTextChangedListener(userTextWatcher)
        binding.etXCoordinate.addTextChangedListener(userTextWatcher)
        binding.etYCoordinate.addTextChangedListener(userTextWatcher)
        binding.etZCoordinate.addTextChangedListener(userTextWatcher)
        binding.etComment.addTextChangedListener(userTextWatcher)

        shift = getString(R.string.night_shift)
        binding.tvUGDate.text = SimpleDateFormat(CONSTANTS.DATE_MONTH_YEAR_FORMAT).format(Date())

        binding.rbRadioGroup.setOnCheckedChangeListener { radioGroup : RadioGroup, id : Int ->
            shift = radioGroup.findViewById<AppCompatRadioButton>(id).text.toString()
        }

        binding.btnNextStep.setOnClickListener {
            val gson = Gson()
            ugDataModel =
                UnderGroundInsertModel(attributeDataModelList, binding.etName.text.toString(),
                    binding.etComment.text.toString(), binding.tvUGDate.text.toString(),
                    binding.etMapSerialNo.text.toString(), shift,
                    binding.etMappedBy.text.toString(),
                    binding.etScale.text.toString(), binding.etLocation.text.toString(),
                    binding.etVeinLoad.text.toString(), binding.etXCoordinate.text.toString(),
                    binding.etYCoordinate.text.toString(), binding.etZCoordinate.text.toString(),
                    null, null, null, null)
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