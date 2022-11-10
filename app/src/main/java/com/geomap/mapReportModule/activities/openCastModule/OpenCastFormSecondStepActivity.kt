package com.geomap.mapReportModule.activities.openCastModule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastFormSecondStepBinding
import com.geomap.mapReportModule.models.OpenCastInsertModel
import com.geomap.mapReportModule.models.SuccessModel
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenCastFormSecondStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastFormSecondStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var ocDataModel = OpenCastInsertModel()
    var desc : String? = ""
    private var userId : String? = null

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

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            val gson = Gson()
            val data = intent.getStringExtra("ocData")
            val type1 = object : TypeToken<OpenCastInsertModel>() {}.type
            ocDataModel = gson.fromJson(data, type1)
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.edtDescription.addTextChangedListener(userTextWatcher)

        binding.btnSubmit.setOnClickListener {
            postOpenCastInsert()
        }

    }

    private fun postOpenCastInsert() {
        if (isNetworkConnected(ctx)) {
            RetrofitService.getInstance().postOpenCastInsert(
                userId, ocDataModel.minesSiteName, ocDataModel.sheetNo, ocDataModel.pitName,
                ocDataModel.pitLocation, ocDataModel.shiftInchargeName,
                ocDataModel.geologistName, ocDataModel.faceLocation, ocDataModel.faceLengthM,
                ocDataModel.faceAreaM2, ocDataModel.faceRockTypes, ocDataModel.benchRL,
                ocDataModel.benchHeightWidth, ocDataModel.benchAngle, ocDataModel.thicknessOfOre,
                ocDataModel.thinessOfOverburden,
                ocDataModel.thicknessOfInterburden, ocDataModel.observedGradeOfOre,
                ocDataModel.actualGradeOfOre,
                ocDataModel.sampleCollected, ocDataModel.weathering,
                ocDataModel.rockStregth, ocDataModel.waterCondition, ocDataModel.typeOfGeologist,
                ocDataModel.typeOfFaults,
                ocDataModel.notes, ocDataModel.shift, ocDataModel.ocDate,
                ocDataModel.dipDirectionAngle,
                "", "", ""
            ).enqueue(object : Callback<SuccessModel?> {
                override fun onResponse(call : Call<SuccessModel?>,
                    response : Response<SuccessModel?>) {
                    val model = response.body()
                    if (model!!.responseCode.equals(
                            getString(R.string.ResponseCodesuccess))) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder,
                            act)
                        showToast(model.responseMessage, act)
                        finish()
                    }
                }

                override fun onFailure(call : Call<SuccessModel?>, t : Throwable) {
                    hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
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