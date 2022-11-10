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
import com.geomap.mapReportModule.models.OpenCastDetailsModel
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenCastDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_detail)
        ctx = this@OpenCastDetailActivity
        act = this@OpenCastDetailActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        postData()

        binding.btnViewPdf.setOnClickListener {
            callViewPdfActivity(act, "1")
        }
    }

    private fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            RetrofitService.getInstance()
                .getOpenCastDetails(userId)
                .enqueue(object : Callback<OpenCastDetailsModel> {
                    override fun onResponse(call : Call<OpenCastDetailsModel>,
                        response : Response<OpenCastDetailsModel>) {
                        try {
                            hideProgressBar(binding.progressBar,
                                binding.progressBarHolder, act)
                            val model : OpenCastDetailsModel? = response.body()!!
                            when (model!!.responseCode) {
                                getString(R.string.ResponseCodesuccess) -> {
                                    binding.llMainLayout.visibility = View.VISIBLE
                                    binding.btnViewPdf.visibility = View.VISIBLE
                                    model.responseData?.let {
                                        binding.tvMappingSheetNo.text = it.mappingSheetNo
                                        binding.tvDate.text = it.ocDate
                                        binding.tvMineSiteName.text = it.minesSiteName
                                        binding.tvPitName.text = it.pitName
                                        binding.tvPitLocation.text = it.pitLoaction
                                        binding.tvShiftInchargeName.text = it.shiftInchargeName
                                        binding.tvGeologistName.text = it.geologistName
                                        binding.tvMappingParameters.text = it.mappingParameter
                                        binding.tvFaceLocation.text = it.faceLocation
                                        binding.tvFaceLength.text = it.faceLength
                                        binding.tvFaceArea.text = it.faceArea
                                        binding.tvFaceRockTypes.text = it.faceRockType
                                        binding.tvBenchRL.text = it.benchRl
                                        binding.tvBenchHeightWidth.text = it.benchHeightWidth
                                        binding.tvBenchAngle.text = it.benchAngle
                                        binding.tvDipDirectionAngle.text = it.dipDirectionAndAngle
                                        binding.tvThicknessOfOre.text = it.thicknessOfOre
                                        binding.tvThinessOfOverburden.text = it.thicknessOfOverburdan
                                        binding.tvThicknessOfInterburden.text = it.thicknessOfInterburden
                                        binding.tvObservedGradeOfOre.text = it.observedGradeOfOre
                                        binding.tvSampleCollected.text = it.sampleColledted
                                        binding.tvActualGradeOfOre.text = it.actualGradeOfOre
                                        binding.tvWeathering.text = it.weathring
                                        binding.tvRockStrength.text = it.rockStregth
                                        binding.tvWaterCondition.text = it.waterCondition
                                        binding.tvTypeOfGeologicalStructures.text = it.typeOfGeologist
                                        binding.tvTypeOfFaults.text = it.typeOfFaults
                                    }
                                }
                                getString(R.string.ResponseCodefail) -> {
                                    showToast(model.responseMessage, act)
                                }
                                getString(R.string.ResponseCodeDeleted) -> {
                                    callDelete403(act, model.responseMessage)
                                }
                            }
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call : Call<OpenCastDetailsModel>, t : Throwable) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder,
                            act)
                    }
                })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}