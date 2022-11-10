package com.geomap.mapReportModule.activities.underGroundModule

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.allDisable
import com.geomap.GeoMapApp.callUnderGroundFormThirdStepActivity
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormSecondStepBinding
import com.geomap.utils.CONSTANTS
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
    var shift : String? = ""
    var mappedBy : String? = ""
    var scale : String? = ""
    var location : String? = ""
    var veinLoad : String? = ""
    var xCoordinate : String? = ""
    var yCoordinate : String? = ""
    var zCoordinate : String? = ""

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            mapSerialNo = binding.etMapSerialNo.text.toString()
            shift = binding.etShift.text.toString()
            mappedBy = binding.etMappedBy.text.toString()
            scale = binding.etScale.text.toString()
            location = binding.etLocation.text.toString()
            veinLoad = binding.etVeinLoad.text.toString()
            xCoordinate = binding.etXCoordinate.text.toString()
            yCoordinate = binding.etYCoordinate.text.toString()
            zCoordinate = binding.etZCoordinate.text.toString()

            when {
                mapSerialNo.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnNextStep)
                }

                shift.equals("", ignoreCase = true) -> {
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
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_under_ground_form_second_step)
        ctx = this@UnderGroundFormSecondStepActivity
        act = this@UnderGroundFormSecondStepActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.etMapSerialNo.addTextChangedListener(userTextWatcher)
        binding.etShift.addTextChangedListener(userTextWatcher)
        binding.etMappedBy.addTextChangedListener(userTextWatcher)
        binding.etScale.addTextChangedListener(userTextWatcher)
        binding.etLocation.addTextChangedListener(userTextWatcher)
        binding.etVeinLoad.addTextChangedListener(userTextWatcher)
        binding.etXCoordinate.addTextChangedListener(userTextWatcher)
        binding.etYCoordinate.addTextChangedListener(userTextWatcher)
        binding.etZCoordinate.addTextChangedListener(userTextWatcher)

        binding.etDate.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setDate()
            }
        }

        binding.btnNextStep.setOnClickListener {
            callUnderGroundFormThirdStepActivity(act, "0")
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun setDate() {
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(this, R.style.DialogTheme,
            { view : DatePicker, year : Int, monthOfYear : Int, dayOfMonth : Int ->
                view.minDate = System.currentTimeMillis() - 1000
                val cal = Calendar.getInstance()
                cal.timeInMillis
                cal[year, monthOfYear] = dayOfMonth
                val date = cal.time
                val sdf = SimpleDateFormat(CONSTANTS.DATE_MONTH_YEAR_FORMAT)
                val strDate = sdf.format(date)
                binding.etDate.setText(strDate)
                ageYear = year
                ageMonth = monthOfYear
                ageDate = dayOfMonth
                birthYear = getAge(ageYear, ageMonth, ageDate)
                if (birthYear < 3) {
                    binding.etDate.isFocusable = true
                    binding.etDate.requestFocus()
                    binding.ltDate.isErrorEnabled = true
                    binding.ltDate.error = getString(R.string.check_dob)
                    binding.btnNextStep.isEnabled = false
                    binding.btnNextStep.isClickable = false
                    binding.btnNextStep.setBackgroundResource(R.drawable.disable_button)
                } else {
                    binding.ltDate.isErrorEnabled = false
                    binding.btnNextStep.isEnabled = true
                    binding.btnNextStep.isClickable = true
                    binding.btnNextStep.setBackgroundResource(R.drawable.enable_button)
                }
            }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    private fun getAge(year : Int, month : Int, day : Int) : Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age
    }

    override fun onBackPressed() {
        finish()
    }
}