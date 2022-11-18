package com.geomap.mapReportModule.activities.openCastModule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastDetailBinding
import com.geomap.mapReportModule.models.AttributeDataModel
import com.geomap.mapReportModule.models.OpenCastDetailsModel
import com.geomap.roomDataBase.OpenCastMappingReport
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class OpenCastDetailDraftActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var report : String? = null
    var ocReportData = OpenCastMappingReport()
    val gson = Gson()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_detail)
        ctx = this@OpenCastDetailDraftActivity
        act = this@OpenCastDetailDraftActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null){
            report = intent.getStringExtra("report")
            val type1 = object : TypeToken<OpenCastMappingReport>() {}.type
            ocReportData = gson.fromJson(report, type1)
            postData()
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
 
        binding.btnViewPdf.setOnClickListener {
            callViewPdfActivity(act, "1")
        }
    }

    private fun postData() {
         
        binding.llMainLayout.visibility = View.VISIBLE
        //binding.btnViewPdf.visibility = View.VISIBLE
        binding.tvMappingSheetNo.text = ocReportData.mappingSheetNo
        binding.tvDate.text = ocReportData.ocDate
        binding.tvMineSiteName.text = ocReportData.minesSiteName
        binding.tvPitName.text = ocReportData.pitName
        binding.tvPitLocation.text = ocReportData.pitLocation
        binding.tvShiftInchargeName.text = ocReportData.shiftInChargeName
        binding.tvGeologistName.text = ocReportData.geologistName
        binding.tvFaceLocation.text = ocReportData.faceLocation
        binding.tvFaceLength.text = ocReportData.faceLength
        binding.tvFaceArea.text = ocReportData.faceArea
        binding.tvFaceRockTypes.text = ocReportData.faceRockTypes
        binding.tvBenchRL.text = ocReportData.benchRL
        binding.tvBenchHeightWidth.text = ocReportData.benchHeightWidth
        binding.tvBenchAngle.text = ocReportData.benchAngle
        binding.tvDipDirectionAngle.text = ocReportData.dipDirectionAngle
        binding.tvThicknessOfOre.text = ocReportData.thicknessOfOre
        binding.tvThinessOfOverburden.text = ocReportData.thicknessOfOverburden
        binding.tvThicknessOfInterburden.text = ocReportData.thicknessOfInterBurden
        binding.tvObservedGradeOfOre.text = ocReportData.observedGradeOfOre
        binding.tvSampleCollected.text = ocReportData.sampleCollected
        binding.tvActualGradeOfOre.text = ocReportData.actualGradOfOre
        binding.tvWeathering.text = ocReportData.weathering
        binding.tvRockStrength.text = ocReportData.rockStrength
        binding.tvWaterCondition.text = ocReportData.waterCondition
        binding.tvTypeOfGeologicalStructures.text = ocReportData.typeOfGeologicalStructures
        binding.tvTypeOfFaults.text = ocReportData.typeOfFaults
    }

    override fun onBackPressed() {
        finish()
    }
}