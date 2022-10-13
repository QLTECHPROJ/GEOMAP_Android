package com.geomap.mapReportModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.allDisable
import com.geomap.GeoMapApp.callOpenCastFormSecondStepActivity
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastFormFirstStepBinding

class OpenCastFormFirstStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastFormFirstStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    var minesSiteName : String? = null
    var pitName : String? = null
    var pitLocation : String? = null
    var shiftInchargeName : String? = null
    var geologistName : String? = null
    var faceLocation : String? = null
    var faceLengthM : String? = null
    var faceAreaM2 : String? = null
    var faceRockTypes : String? = null
    var benchRL : String? = null
    var benchHeightWidth : String? = null
    var benchAngle : String? = null
    var dipDirectionAngle : String? = null
    var thicknessOfOre : String? = null
    var thinessOfOverburden : String? = null
    var thicknessOfInterburden : String? = null
    var observedGradeOfOre : String? = null
    var sampleCollected : String? = null
    var actualGradeOfOre : String? = null
    var weathering : String? = null
    var rockStrength : String? = null
    var waterCondition : String? = null
    var typeOfGeologicalStructures : String? = null
    var typeOfFaults : String? = null
    var notes : String? = null

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            minesSiteName = binding.etMinesSiteName.text.toString()
            pitName = binding.etPitName.text.toString()
            pitLocation = binding.etPitLocation.text.toString()
            shiftInchargeName = binding.etShiftInchargeName.text.toString()
            geologistName = binding.etGeologistName.text.toString()
            faceLocation = binding.etFaceLocation.text.toString()
            faceLengthM = binding.etFaceLengthM.text.toString()
            faceAreaM2 = binding.etFaceAreaM2.text.toString()
            faceRockTypes = binding.etFaceRockTypes.text.toString()
            benchRL = binding.etBenchRL.text.toString()
            benchHeightWidth = binding.etBenchHeightWidth.text.toString()
            benchAngle = binding.etBenchAngle.text.toString()
            dipDirectionAngle = binding.etDipDirectionAngle.text.toString()
            thicknessOfOre = binding.etThicknessOfOre.text.toString()
            thinessOfOverburden = binding.etThinessOfOverburden.text.toString()
            thicknessOfInterburden = binding.etThicknessOfInterburden.text.toString()
            observedGradeOfOre = binding.etObservedGradeOfOre.text.toString()
            sampleCollected = binding.etSampleCollected.text.toString()
            actualGradeOfOre = binding.etActualGradeOfOre.text.toString()
            weathering = binding.etWeathering.text.toString()
            rockStrength = binding.etRockStrength.text.toString()
            waterCondition = binding.etWaterCondition.text.toString()
            typeOfGeologicalStructures = binding.etTypeOfGeologicalStructures.text.toString()
            typeOfFaults = binding.etTypeOfFaults.text.toString()
            notes = binding.edtNotes.text.toString()

            when {
                minesSiteName.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                pitName.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                pitLocation.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                shiftInchargeName.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                geologistName.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                faceLocation.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                faceLengthM.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                faceAreaM2.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                faceRockTypes.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                benchRL.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                benchHeightWidth.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                benchAngle.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                dipDirectionAngle.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                thicknessOfOre.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                thinessOfOverburden.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                thicknessOfInterburden.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                observedGradeOfOre.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                sampleCollected.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                actualGradeOfOre.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                weathering.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                rockStrength.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                waterCondition.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                typeOfGeologicalStructures.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                typeOfFaults.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                notes.equals("") -> {
                    allDisable(binding.btnSubmit)
                }

                else -> {
                    binding.btnSubmit.isEnabled = true
                    binding.btnSubmit.setBackgroundResource(R.drawable.enable_button)
                }
            }
        }

        override fun afterTextChanged(s : Editable) {}
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_form_first_step)
        ctx = this@OpenCastFormFirstStepActivity
        act = this@OpenCastFormFirstStepActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.etMinesSiteName.addTextChangedListener(userTextWatcher)
        binding.etPitName.addTextChangedListener(userTextWatcher)
        binding.etPitLocation.addTextChangedListener(userTextWatcher)
        binding.etShiftInchargeName.addTextChangedListener(userTextWatcher)
        binding.etGeologistName.addTextChangedListener(userTextWatcher)
        binding.etFaceLocation.addTextChangedListener(userTextWatcher)
        binding.etFaceLengthM.addTextChangedListener(userTextWatcher)
        binding.etFaceAreaM2.addTextChangedListener(userTextWatcher)
        binding.etFaceRockTypes.addTextChangedListener(userTextWatcher)
        binding.etFaceLocation.addTextChangedListener(userTextWatcher)
        binding.etBenchRL.addTextChangedListener(userTextWatcher)
        binding.etBenchHeightWidth.addTextChangedListener(userTextWatcher)
        binding.etBenchAngle.addTextChangedListener(userTextWatcher)
        binding.etDipDirectionAngle.addTextChangedListener(userTextWatcher)
        binding.etThicknessOfOre.addTextChangedListener(userTextWatcher)
        binding.etThinessOfOverburden.addTextChangedListener(userTextWatcher)
        binding.etThicknessOfInterburden.addTextChangedListener(userTextWatcher)
        binding.etObservedGradeOfOre.addTextChangedListener(userTextWatcher)
        binding.etSampleCollected.addTextChangedListener(userTextWatcher)
        binding.etActualGradeOfOre.addTextChangedListener(userTextWatcher)
        binding.etWeathering.addTextChangedListener(userTextWatcher)
        binding.etRockStrength.addTextChangedListener(userTextWatcher)
        binding.etWaterCondition.addTextChangedListener(userTextWatcher)
        binding.etTypeOfGeologicalStructures.addTextChangedListener(userTextWatcher)
        binding.etTypeOfFaults.addTextChangedListener(userTextWatcher)
        binding.edtNotes.addTextChangedListener(userTextWatcher)

        binding.btnSubmit.setOnClickListener {
            callOpenCastFormSecondStepActivity(act, "0")
        }
    }

    override fun onBackPressed() {
        finish()
    }
}