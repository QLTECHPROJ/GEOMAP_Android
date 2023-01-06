package com.geomap.mapReportModule.activities.openCastModule

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geomap.DataBaseFunctions
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastFormFirstStepBinding
import com.geomap.databinding.CommonPopupLayoutBinding
import com.geomap.mapReportModule.models.CommonPopupListModel
import com.geomap.mapReportModule.models.OpenCastDetailsModel
import com.geomap.mapReportModule.models.OpenCastInsertModel
import com.geomap.roomDataBase.*
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class OpenCastFormFirstStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastFormFirstStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    var mappingSheetNo : String? = null
    var minesSiteName : String? = null
    var shift : String? = null
    private val modelList = CommonPopupListModel()
    private var popupAdapter : PopupAdapter? = null
    private var ocDataModel = OpenCastInsertModel()
    private lateinit var ocDetailsModel : OpenCastDetailsModel
    val gson = Gson()
    private val requestExternalStorage = 1
    private val permissionsStorage = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_form_first_step)
        ctx = this@OpenCastFormFirstStepActivity
        act = this@OpenCastFormFirstStepActivity

        binding.llMainLayout.visibility = View.VISIBLE
        binding.btnSubmit.visibility = View.VISIBLE

        if (intent.extras != null) {
            if (intent.getStringExtra("flag") == "detail") {
                flagOC = "1"
                val data = intent.getStringExtra("data")
                val type1 = object : TypeToken<OpenCastDetailsModel>() {}.type
                ocDetailsModel = gson.fromJson(data, type1)
                binding.etMinesSiteName.setText(ocDetailsModel.ResponseData.minesSiteName)
                binding.tvOCDate.text = ocDetailsModel.ResponseData.ocDate
                binding.etPitName.setText(ocDetailsModel.ResponseData.pitName)
                binding.etPitLocation.setText(ocDetailsModel.ResponseData.pitLoaction)
                binding.etShiftInchargeName.setText(ocDetailsModel.ResponseData.shiftInchargeName)
                binding.etGeologistName.setText(ocDetailsModel.ResponseData.geologistName)
                binding.etFaceLocation.setText(ocDetailsModel.ResponseData.faceLocation)
                binding.etFaceLengthM.setText(ocDetailsModel.ResponseData.faceLength)
                binding.etFaceAreaM2.setText(ocDetailsModel.ResponseData.faceArea)
                binding.etFaceRockTypes.setText(ocDetailsModel.ResponseData.faceRockType)
                binding.etBenchRL.setText(ocDetailsModel.ResponseData.benchRl)
                binding.etBenchHeightWidth.setText(ocDetailsModel.ResponseData.benchHeightWidth)
                binding.etBenchAngle.setText(ocDetailsModel.ResponseData.benchAngle)
                binding.etDipDirectionAngle.setText(ocDetailsModel.ResponseData.dipDirectionAndAngle)
                binding.etThicknessOfOre.setText(ocDetailsModel.ResponseData.thicknessOfOre)
                binding.etThinessOfOverburden.setText(ocDetailsModel.ResponseData.thicknessOfOverburdan)
                binding.etThicknessOfInterburden.setText(ocDetailsModel.ResponseData.thicknessOfInterburden)
                binding.etObservedGradeOfOre.setText(ocDetailsModel.ResponseData.observedGradeOfOre)
                binding.etActualGradeOfOre.setText(ocDetailsModel.ResponseData.actualGradeOfOre)
                sampleCollected = ocDetailsModel.ResponseData.sampleColledted
                if (sampleCollected != null) {
                    binding.tvSampleCollected.text = sampleCollected
                } else {
                    sampleCollected = ""
                }
                weathering = ocDetailsModel.ResponseData.weathring
                if (weathering != null) {
                    binding.tvWeathering.text = weathering
                } else {
                    weathering = ""
                }
                rockStrength = ocDetailsModel.ResponseData.rockStregth
                if (rockStrength != null) {
                    binding.tvRockStrength.text = rockStrength
                } else {
                    rockStrength = ""
                }
                waterCondition = ocDetailsModel.ResponseData.waterCondition
                if (waterCondition != null) {
                    binding.tvWaterCondition.text = waterCondition
                } else {
                    waterCondition = ""
                }
                typeOfGeologicalStructures = ocDetailsModel.ResponseData.typeOfGeologist
                if (typeOfGeologicalStructures != null) {
                    binding.tvTypeOfGeologicalStructures.text = typeOfGeologicalStructures
                } else {
                    typeOfGeologicalStructures = ""
                }
                typeOfFaults = ocDetailsModel.ResponseData.typeOfFaults
                if (typeOfFaults != null) {
                    binding.tvTypeOfFaults.text = typeOfFaults
                } else {
                    typeOfFaults = ""
                }
                binding.etNotes.setText(ocDetailsModel.ResponseData.notes)
                shift = ocDetailsModel.ResponseData.shift
                if (shift == getString(R.string.night_shift)) {
                    binding.rbNightShift.isSelected = true
                    binding.rbDayShift.isSelected = false
                } else {
                    binding.rbNightShift.isSelected = false
                    binding.rbDayShift.isSelected = true
                }
                if (ocDetailsModel.ResponseData.image != "") {
                    GeoMapDatabase.databaseWriteExecutor.execute {
                        try {
                            img = Glide.with(ctx).asBitmap().load(
                                ocDetailsModel.ResponseData.image
                            ).submit().get()
                        } catch (_ : Exception) {
                        }
                    }
                }
                if (ocDetailsModel.ResponseData.geologistSign != "") {
                    GeoMapDatabase.databaseWriteExecutor.execute {
                        try {
                            binding.geologistSignPad.signatureBitmap = Glide.with(ctx).asBitmap()
                                    .load(
                                        ocDetailsModel.ResponseData.geologistSign
                                    ).submit().get()
                        } catch (_ : Exception) {
                        }
                    }
                }
                if (ocDetailsModel.ResponseData.clientsGeologistSign != "") {
                    GeoMapDatabase.databaseWriteExecutor.execute {
                        try {
                            binding.geologistClientSignPad.signatureBitmap = Glide.with(
                                ctx
                            ).asBitmap().load(
                                ocDetailsModel.ResponseData.clientsGeologistSign
                            ).submit().get()
                        } catch (_ : Exception) {
                        }
                    }
                }

            } else if (intent.getStringExtra("flag") == "detailDraft") {
                flagOC = "2"
                val data = intent.getStringExtra("data")
                val type1 = object : TypeToken<OpenCastMappingReport>() {}.type
                ocmr = gson.fromJson(data, type1)
                binding.etMinesSiteName.setText(ocmr.minesSiteName)
                binding.tvOCDate.text = ocmr.ocDate
                binding.etPitName.setText(ocmr.pitName)
                binding.etPitLocation.setText(ocmr.pitLocation)
                binding.etShiftInchargeName.setText(ocmr.shiftInChargeName)
                binding.etGeologistName.setText(ocmr.geologistName)
                binding.etFaceLocation.setText(ocmr.faceLocation)
                binding.etFaceLengthM.setText(ocmr.faceLength)
                binding.etFaceAreaM2.setText(ocmr.faceArea)
                binding.etFaceRockTypes.setText(ocmr.faceRockTypes)
                binding.etBenchRL.setText(ocmr.benchRL)
                binding.etBenchHeightWidth.setText(ocmr.benchHeightWidth)
                binding.etBenchAngle.setText(ocmr.benchAngle)
                binding.etDipDirectionAngle.setText(ocmr.dipDirectionAngle)
                binding.etThicknessOfOre.setText(ocmr.thicknessOfOre)
                binding.etThinessOfOverburden.setText(ocmr.thicknessOfOverburden)
                binding.etThicknessOfInterburden.setText(ocmr.thicknessOfInterBurden)
                binding.etObservedGradeOfOre.setText(ocmr.observedGradeOfOre)
                binding.etActualGradeOfOre.setText(ocmr.actualGradOfOre)
                sampleCollected = ocmr.sampleCollected
                if (sampleCollected != "") {
                    binding.tvSampleCollected.text = sampleCollected
                } else {
                    sampleCollected = ""
                }
                weathering = ocmr.weathering
                if (weathering != "") {
                    binding.tvWeathering.text = weathering
                } else {
                    weathering = ""
                }
                rockStrength = ocmr.rockStrength
                if (rockStrength != "") {
                    binding.tvRockStrength.text = rockStrength
                } else {
                    rockStrength = ""
                }
                waterCondition = ocmr.waterCondition
                if (waterCondition != "") {
                    binding.tvWaterCondition.text = waterCondition
                } else {
                    waterCondition = ""
                }
                typeOfGeologicalStructures = ocmr.typeOfGeologicalStructures
                if (typeOfGeologicalStructures != "") {
                    binding.tvTypeOfGeologicalStructures.text = typeOfGeologicalStructures
                } else {
                    typeOfGeologicalStructures = ""
                }
                typeOfFaults = ocmr.typeOfFaults
                if (typeOfFaults != "") {
                    binding.tvTypeOfFaults.text = typeOfFaults
                } else {
                    typeOfFaults = ""
                }
                binding.etNotes.setText(ocmr.notes)
                shift = ocmr.shift
                if (ocmr.image != null) {
                    GeoMapDatabase.databaseWriteExecutor.execute {
                        try {
                            img = Glide.with(ctx).asBitmap().load(
                                ocmr.image
                            ).submit().get()
                        } catch (_ : Exception) {
                        }
                    }
                }
                if (shift == getString(R.string.night_shift)) {
                    binding.rbNightShift.isSelected = true
                    binding.rbDayShift.isSelected = false
                } else {
                    binding.rbNightShift.isSelected = false
                    binding.rbDayShift.isSelected = true
                }
                if (ocmr.geologistSign != null) {
                    binding.geologistSignPad.signatureBitmap = ocmr.geologistSign
                    geoSign = true
                }
                if (ocmr.clientsGeologistSign != null) {
                    binding.geologistClientSignPad.signatureBitmap = ocmr.clientsGeologistSign
                    clientsGeoSign = true
                }
            }
        }
        verifyStoragePermissions()

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        if(flagOC == "0") {
            binding.tvOCDate.text = SimpleDateFormat(CONSTANTS.SERVER_TIME_FORMAT).format(Date())
            binding.etGeologistName.setText(shared.getString(CONSTANTS.name, ""))
        }
        shift = getString(R.string.night_shift)

        binding.rbRadioGroup.setOnCheckedChangeListener { radioGroup : RadioGroup, id : Int ->
            shift = radioGroup.findViewById<AppCompatRadioButton>(id).text.toString()
        }

        binding.cvSampleCollected.setOnClickListener {
            callPopupList(
                getString(R.string.choose_your_sample_collected),
                getString(R.string.pls_select_your_sample_collected), "1"
            )
        }

        binding.cvWeathering.setOnClickListener {
            callPopupList(
                getString(R.string.choose_your_weathering),
                getString(R.string.pls_select_your_weathering), "2"
            )
        }

        binding.cvRockStrength.setOnClickListener {
            callPopupList(
                getString(R.string.choose_your_rock_strength),
                getString(R.string.pls_select_your_rock_strength), "3"
            )
        }

        binding.cvWaterCondition.setOnClickListener {
            callPopupList(
                getString(R.string.choose_your_water_condition),
                getString(R.string.pls_select_your_water_condition), "4"
            )
        }

        binding.cvTypeOfGeologicalStructures.setOnClickListener {
            callPopupList(
                getString(R.string.choose_your_type_of_geological_structures),
                getString(R.string.pls_select_your_type_of_geological_structures), "5"
            )
        }

        binding.cvTypeOfFaults.setOnClickListener {
            callPopupList(
                getString(R.string.choose_your_Type_of_faults),
                getString(R.string.pls_select_your_Type_of_faults), "6"
            )
        }


        binding.btnGeologistSignPadClear.setOnClickListener {
            geoSign = false
            binding.geologistSignPad.clear()
        }

        binding.btnGeologistClientSignPadClear.setOnClickListener {
            clientsGeoSign = false
            binding.geologistClientSignPad.clear()
        }

        binding.geologistSignPad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                geologistSignEdited = true
                verifyStoragePermissions()
                Log.e("Signature", "onStartSigning")
            }

            override fun onSigned() {
                geoSign = true
                binding.btnGeologistSignPadClear.isEnabled = true
                Log.e("Signature", "onSigned")
            }

            override fun onClear() {
                geoSign = false
                geologistSignEdited = false
                binding.btnGeologistSignPadClear.isEnabled = false
                Log.e("Signature", "onClear")
            }
        })

        binding.geologistClientSignPad.setOnSignedListener(object :
            SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                clientsGeologistSignEdited = true
                verifyStoragePermissions()
            }

            override fun onSigned() {
                clientsGeoSign = true
                binding.btnGeologistClientSignPadClear.isEnabled = true
            }

            override fun onClear() {
                clientsGeoSign = false
                clientsGeologistSignEdited = false
                binding.btnGeologistClientSignPadClear.isEnabled = false
            }
        })

        binding.btnSubmit.setOnClickListener {
            postOpenCastInsert()
        }
    }

    private fun postOpenCastInsert() {
        var sheetNo = ""
        sheetNo = if (flagOC == "1") {
            ocDetailsModel.ResponseData.mappingSheetNo
        } else {
            ""
        }
        val gson = Gson()
        val geologistSignPad : Bitmap? = if (geologistSignEdited) {
            binding.geologistSignPad.signatureBitmap
        } else if (geoSign) {
            binding.geologistSignPad.signatureBitmap
        } else {
            null
        }

        val geologistClientSignPad : Bitmap? = if (clientsGeologistSignEdited) {
            binding.geologistClientSignPad.signatureBitmap
        } else if (clientsGeoSign) {
            binding.geologistClientSignPad.signatureBitmap
        } else {
            null
        }

        ocDataModel = OpenCastInsertModel(
            binding.etMinesSiteName.text.toString(),
            sheetNo, binding.tvOCDate.text.toString(),
            binding.etPitName.text.toString(), binding.etPitLocation.text.toString(),
            binding.etShiftInchargeName.text.toString(),
            binding.etGeologistName.text.toString(), binding.etFaceLocation.text.toString(),
            binding.etFaceLengthM.text.toString(), binding.etFaceAreaM2.text.toString(),
            binding.etFaceRockTypes.text.toString(), binding.etBenchRL.text.toString(),
            binding.etBenchHeightWidth.text.toString(), binding.etBenchAngle.text.toString(),
            binding.etDipDirectionAngle.text.toString(),
            binding.etThicknessOfOre.text.toString(),
            binding.etThinessOfOverburden.text.toString(),
            binding.etThicknessOfInterburden.text.toString(),
            binding.etObservedGradeOfOre.text.toString(),
            binding.etActualGradeOfOre.text.toString(),
            sampleCollected, weathering,
            rockStrength, waterCondition,
            typeOfGeologicalStructures,
            typeOfFaults, shift, binding.etNotes.text.toString(),
            geologistSignPad, geologistClientSignPad
        )
        val i = Intent(ctx, OpenCastFormSecondStepActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        i.putExtra("ocData", gson.toJson(ocDataModel))
        startActivity(i)
    }

    private fun callPopupList(title : String, searchHint : String, keyS : String) {
        val dialog = Dialog(ctx)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.common_list_layout)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tvTitle : TextView = dialog.findViewById(R.id.tvTilte)
        val rvList : RecyclerView = dialog.findViewById(R.id.rvList)
        val searchView : SearchView = dialog.findViewById(R.id.searchView)
        val tvFound : TextView = dialog.findViewById(R.id.tvFound)
        val pb : ProgressBar = dialog.findViewById(R.id.progressBar)
        val pbh : FrameLayout = dialog.findViewById(R.id.progressBarHolder)
        tvTitle.text = title
        rvList.visibility = View.VISIBLE
        rvList.layoutManager = LinearLayoutManager(ctx)
        dialog.setOnKeyListener { _ : DialogInterface?, keyCode : Int, _ : KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
            }
            false
        }
        searchView.onActionViewExpanded()
        val searchEditText : EditText = searchView.findViewById(
            androidx.appcompat.R.id.search_src_text
        )
        searchEditText.setTextColor(ContextCompat.getColor(ctx, R.color.light_black))
        searchEditText.setHintTextColor(ContextCompat.getColor(ctx, R.color.light_black))
        searchView.clearFocus()
        searchEditText.hint = searchHint

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(search : String) : Boolean {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                return false
            }

            override fun onQueryTextChange(search : String) : Boolean {
                try {
                    when (keyS) {
                        "1" -> {
                            popupAdapter?.filter?.filter(search)
                        }
                        "2" -> {
                            popupAdapter?.filter?.filter(search)
                        }
                        "3" -> {
                            popupAdapter?.filter?.filter(search)
                        }
                        "4" -> {
                            popupAdapter?.filter?.filter(search)
                        }
                        "5" -> {
                            popupAdapter?.filter?.filter(search)
                        }
                        "6" -> {
                            popupAdapter?.filter?.filter(search)
                        }
                    }
                } catch (e : java.lang.Exception) {
                    e.printStackTrace()
                }
                return false
            }
        })
        prepareAttributesListing(dialog, rvList, tvFound, pb, pbh, searchView, keyS)
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
    }

    private fun prepareAttributesListing(
            dialog : Dialog, rvList : RecyclerView,
            tvFound : TextView,
            progressBar : ProgressBar, progressBarHolder : FrameLayout, searchView : SearchView,
            keyS : String
    ) {
        if (isNetworkConnected(ctx)) {
            showProgressBar(progressBar, progressBarHolder, act)
            searchView.isEnabled = false
            searchView.isClickable = false
            var service : Call<CommonPopupListModel>? = null
            when (keyS) {
                "1" -> {
                    service = RetrofitService.getInstance().getSampleCollectedsList
                }
                "2" -> {
                    service = RetrofitService.getInstance().getWeatheringDataList
                }
                "3" -> {
                    service = RetrofitService.getInstance().getRockStrengthDataList
                }
                "4" -> {
                    service = RetrofitService.getInstance().getWaterConditionDataList
                }
                "5" -> {
                    service = RetrofitService.getInstance().getTypeOfGeologicalStructuresList
                }
                "6" -> {
                    service = RetrofitService.getInstance().getTypeOfFaultList
                }
            }

            service?.enqueue(object :
                Callback<CommonPopupListModel> {
                override fun onResponse(
                        call : Call<CommonPopupListModel>,
                        response : Response<CommonPopupListModel>
                ) {
                    val model = response.body()
                    hideProgressBar(progressBar, progressBarHolder, act)
                    when {
                        model!!.responseCode!! == getString(R.string.ResponseCodesuccess) -> {
                            searchView.isEnabled = true
                            searchView.isClickable = true
                            rvList.layoutManager = LinearLayoutManager(ctx)
                            popupAdapter =
                                    PopupAdapter(
                                        dialog, binding,
                                        model.responseData!!, rvList, tvFound, keyS, ctx
                                    )
                            rvList.adapter = popupAdapter
                        }
                        model.responseCode!! == getString(R.string.ResponseCodefail) -> {
                            showToast(model.responseMessage, act)
                        }
                        model.responseCode!! == getString(R.string.ResponseCodeDeleted) -> {
                            callDelete403(act, model.responseMessage)
                        }
                    }

                }

                override fun onFailure(call : Call<CommonPopupListModel>, t : Throwable) {
                    hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                }
            })
        } else {
            DB = getDataBase(ctx)

            modelList.responseData = ArrayList<CommonPopupListModel.ResponseData>()
            when (keyS) {
                "1" -> {
                    var list : ArrayList<SampleCollected>
                    GeoMapDatabase.databaseWriteExecutor.execute {
                        list = DB?.taskDao()?.geAllSampleCollected() as ArrayList<SampleCollected>
                        Log.e(
                            "List SampleCollected",
                            "true" + DataBaseFunctions.gson.toJson(list).toString()
                        )
                        callSampleCollectedAdapter(list, searchView, rvList, tvFound, keyS, dialog)
                    }
                }
                "2" -> {
                    var list : ArrayList<WeatheringData>
                    GeoMapDatabase.databaseWriteExecutor.execute {
                        list = DB?.taskDao()?.geAllWeatheringData() as ArrayList<WeatheringData>
                        Log.e(
                            "List WeatheringData",
                            "true" + DataBaseFunctions.gson.toJson(list).toString()
                        )

                        callWeatheringDataAdapter(list, searchView, rvList, tvFound, keyS, dialog)
                    }
                }
                "3" -> {
                    var list : ArrayList<RockStrength>
                    GeoMapDatabase.databaseWriteExecutor.execute {
                        list = DB?.taskDao()?.geAllRockStrength() as ArrayList<RockStrength>
                        Log.e(
                            "List RockStrength",
                            "true" + DataBaseFunctions.gson.toJson(list).toString()
                        )
                        callRockStrengthAdapter(list, searchView, rvList, tvFound, keyS, dialog)
                    }
                }
                "4" -> {
                    var list : ArrayList<WaterCondition>
                    GeoMapDatabase.databaseWriteExecutor.execute {
                        list = DB?.taskDao()?.geAllWaterCondition() as ArrayList<WaterCondition>
                        Log.e(
                            "List WaterCondition",
                            "true" + DataBaseFunctions.gson.toJson(list).toString()
                        )
                        callWaterConditionAdapter(list, searchView, rvList, tvFound, keyS, dialog)
                    }
                }
                "5" -> {
                    var list : ArrayList<TypeOfGeologicalStructures>
                    GeoMapDatabase.databaseWriteExecutor.execute {
                        list = DB?.taskDao()
                                ?.geAllTypeOfGeologicalStructures() as ArrayList<TypeOfGeologicalStructures>
                        Log.e(
                            "List TypeOfGeologicalStructures",
                            "true" + DataBaseFunctions.gson.toJson(list).toString()
                        )
                        callTypeOfGeologicalStructuresAdapter(
                            list, searchView, rvList, tvFound,
                            keyS, dialog
                        )
                    }
                }
                "6" -> {
                    var list : ArrayList<TypeOfFaults>
                    GeoMapDatabase.databaseWriteExecutor.execute {
                        list = DB?.taskDao()?.geAllTypeOfFaults() as ArrayList<TypeOfFaults>
                        Log.e(
                            "List TypeOfFaults",
                            "true" + DataBaseFunctions.gson.toJson(list).toString()
                        )
                        callTypeOfFaultsAdapter(list, searchView, rvList, tvFound, keyS, dialog)
                    }
                }
            }
        }
    }

    private fun callTypeOfFaultsAdapter(
            list : ArrayList<TypeOfFaults>, searchView : SearchView,
            rvList : RecyclerView, tvFound : TextView, keyS : String, dialog : Dialog
    ) {
        for (i in list.indices) {
            val obj = CommonPopupListModel.ResponseData()
            obj.id = list[i].iD.toString()
            obj.name = list[i].name
            obj.createdAt = list[i].createDate
            obj.updatedAt = list[i].updateDate
            Log.e("saveSampleCollected", "true")

            modelList.responseData!!.add(obj)
            if (i == list.size - 1) {
                searchView.isEnabled = true
                searchView.isClickable = true
                rvList.layoutManager = LinearLayoutManager(ctx)
                popupAdapter = PopupAdapter(
                    dialog, binding, modelList.responseData!!, rvList,
                    tvFound, keyS, ctx
                )
                rvList.adapter = popupAdapter
            }
        }
    }

    private fun callTypeOfGeologicalStructuresAdapter(
            list : ArrayList<TypeOfGeologicalStructures>,
            searchView : SearchView, rvList : RecyclerView, tvFound : TextView,
            keyS : String, dialog : Dialog
    ) {
        searchView.isEnabled = true
        searchView.isClickable = true
        rvList.layoutManager = LinearLayoutManager(ctx)
        popupAdapter =
                PopupAdapter(
                    dialog, binding,
                    modelList.responseData!!, rvList, tvFound, keyS, ctx
                )
        rvList.adapter = popupAdapter
        for (i in list.indices) {
            val obj = CommonPopupListModel.ResponseData()
            obj.id = list[i].iD.toString()
            obj.name = list[i].name
            obj.createdAt = list[i].createDate
            obj.updatedAt = list[i].updateDate
            Log.e("saveSampleCollected", "true")

            modelList.responseData!!.add(obj)
            if (i == list.size - 1) {
                searchView.isEnabled = true
                searchView.isClickable = true
                rvList.layoutManager = LinearLayoutManager(ctx)
                popupAdapter = PopupAdapter(
                    dialog, binding, modelList.responseData!!, rvList,
                    tvFound, keyS, ctx
                )
                rvList.adapter = popupAdapter
            }
        }
    }

    private fun callWaterConditionAdapter(
            list : ArrayList<WaterCondition>, searchView : SearchView,
            rvList : RecyclerView, tvFound : TextView, keyS : String, dialog : Dialog
    ) {
        for (i in list.indices) {
            val obj = CommonPopupListModel.ResponseData()
            obj.id = list[i].iD.toString()
            obj.name = list[i].name
            obj.createdAt = list[i].createDate
            obj.updatedAt = list[i].updateDate
            Log.e("saveSampleCollected", "true")

            modelList.responseData!!.add(obj)
            if (i == list.size - 1) {
                searchView.isEnabled = true
                searchView.isClickable = true
                rvList.layoutManager = LinearLayoutManager(ctx)
                popupAdapter = PopupAdapter(
                    dialog, binding, modelList.responseData!!, rvList,
                    tvFound, keyS, ctx
                )
                rvList.adapter = popupAdapter
            }
        }
    }

    private fun callRockStrengthAdapter(
            list : ArrayList<RockStrength>, searchView : SearchView,
            rvList : RecyclerView, tvFound : TextView, keyS : String, dialog : Dialog
    ) {
        for (i in list.indices) {
            val obj = CommonPopupListModel.ResponseData()
            obj.id = list[i].iD.toString()
            obj.name = list[i].name
            obj.createdAt = list[i].createDate
            obj.updatedAt = list[i].updateDate
            Log.e("saveSampleCollected", "true")

            modelList.responseData!!.add(obj)
            if (i == list.size - 1) {
                searchView.isEnabled = true
                searchView.isClickable = true
                rvList.layoutManager = LinearLayoutManager(ctx)
                popupAdapter = PopupAdapter(
                    dialog, binding, modelList.responseData!!, rvList,
                    tvFound, keyS, ctx
                )
                rvList.adapter = popupAdapter
            }
        }
    }

    private fun callWeatheringDataAdapter(
            list : ArrayList<WeatheringData>, searchView : SearchView,
            rvList : RecyclerView, tvFound : TextView, keyS : String, dialog : Dialog
    ) {

        for (i in list.indices) {
            val obj = CommonPopupListModel.ResponseData()
            obj.id = list[i].iD.toString()
            obj.name = list[i].name
            obj.createdAt = list[i].createDate
            obj.updatedAt = list[i].updateDate
            Log.e("saveSampleCollected", "true")

            modelList.responseData!!.add(obj)
            if (i == list.size - 1) {
                searchView.isEnabled = true
                searchView.isClickable = true
                rvList.layoutManager = LinearLayoutManager(ctx)
                popupAdapter = PopupAdapter(
                    dialog, binding, modelList.responseData!!, rvList,
                    tvFound, keyS, ctx
                )
                rvList.adapter = popupAdapter
            }
        }
    }

    private fun callSampleCollectedAdapter(
            list : ArrayList<SampleCollected>,
            searchView : SearchView,
            rvList : RecyclerView, tvFound : TextView, keys : String?, dialog : Dialog
    ) {
        for (i in list.indices) {
            val obj = CommonPopupListModel.ResponseData()
            obj.id = list[i].iD.toString()
            obj.name = list[i].name
            obj.createdAt = list[i].createDate
            obj.updatedAt = list[i].updateDate
            Log.e("saveSampleCollected", "true")
            modelList.responseData!!.add(obj)
            if (i == list.size - 1) {
                searchView.isEnabled = true
                searchView.isClickable = true
                rvList.layoutManager = LinearLayoutManager(ctx)
                popupAdapter = PopupAdapter(
                    dialog, binding, modelList.responseData!!, rvList,
                    tvFound, keys!!, ctx
                )
                rvList.adapter = popupAdapter
            }
        }
    }

    class PopupAdapter(
            private var dialog : Dialog,
            private var binding : ActivityOpenCastFormFirstStepBinding,
            private val modelList : ArrayList<CommonPopupListModel.ResponseData>,
            private var rvCountryList : RecyclerView,
            private var tvFound : TextView, private var keyS : String, private var ctx : Context
    ) :
        RecyclerView.Adapter<PopupAdapter.MyViewHolder>(),
        Filterable {
        private var listFilterData : List<CommonPopupListModel.ResponseData> = modelList
        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : CommonPopupLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.common_popup_layout, parent, false
            )
            return MyViewHolder(v)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
            val mData : CommonPopupListModel.ResponseData = listFilterData[position]
            holder.bindingAdapter.tvName.text = mData.name
            holder.bindingAdapter.llMainLayout.setOnClickListener {
                when (keyS) {
                    "1" -> {
                        if (sampleCollected != mData.name) {
                            binding.tvHintSampleCollected.text = ""
                            binding.tvHintSampleCollected.text = ""
                        }
                        binding.tvSampleCollected.text = mData.name
                        binding.tvSampleCollected.setTextColor(
                            ContextCompat.getColor(ctx, R.color.light_black)
                        )
                        sampleCollected = mData.name
                        binding.tvHintSampleCollected.text = mData.name.toString()
                    }
                    "2" -> {
                        if (weathering != mData.name) {
                            binding.tvHintWeathering.text = ""
                            binding.tvHintWeathering.text = ""
                        }
                        binding.tvWeathering.text = mData.name
                        binding.tvWeathering.setTextColor(
                            ContextCompat.getColor(ctx, R.color.light_black)
                        )
                        weathering = mData.name
                        binding.tvHintWeathering.text = mData.name.toString()
                    }
                    "3" -> {
                        if (rockStrength != mData.name) {
                            binding.tvHintRockStrength.text = ""
                            binding.tvHintRockStrength.text = ""
                        }
                        binding.tvRockStrength.text = mData.name
                        binding.tvRockStrength.setTextColor(
                            ContextCompat.getColor(ctx, R.color.light_black)
                        )
                        rockStrength = mData.name
                        binding.tvHintRockStrength.text = mData.name.toString()
                    }
                    "4" -> {
                        if (waterCondition != mData.name) {
                            binding.tvHintWaterCondition.text = ""
                            binding.tvHintWaterCondition.text = ""
                        }
                        binding.tvWaterCondition.text = mData.name
                        binding.tvWaterCondition.setTextColor(
                            ContextCompat.getColor(ctx, R.color.light_black)
                        )
                        waterCondition = mData.name
                        binding.tvHintWaterCondition.text = mData.name.toString()
                    }
                    "5" -> {
                        if (typeOfGeologicalStructures != mData.name) {
                            binding.tvHintTypeOfGeologicalStructures.text = ""
                            binding.tvHintTypeOfGeologicalStructures.text = ""
                        }
                        binding.tvTypeOfGeologicalStructures.text = mData.name
                        binding.tvTypeOfGeologicalStructures.setTextColor(
                            ContextCompat.getColor(ctx, R.color.light_black)
                        )
                        typeOfGeologicalStructures = mData.name
                        binding.tvHintTypeOfGeologicalStructures.text = mData.name.toString()
                    }
                    "6" -> {
                        if (typeOfFaults != mData.name) {
                            binding.tvHintTypeOfFaults.text = ""
                            binding.tvHintTypeOfFaults.text = ""
                        }
                        binding.tvTypeOfFaults.text = mData.name
                        binding.tvTypeOfFaults.setTextColor(
                            ContextCompat.getColor(ctx, R.color.light_black)
                        )
                        typeOfFaults = mData.name
                        binding.tvHintTypeOfFaults.text = mData.name.toString()
                    }
                }
                dialog.dismiss()
            }
        }

        override fun getItemCount() : Int {
            return listFilterData.size
        }

        override fun getFilter() : Filter {
            return object : Filter() {
                override fun performFiltering(charSequence : CharSequence) : FilterResults {
                    val filterResults = FilterResults()
                    val charString = charSequence.toString()
                    listFilterData = if (charString.isEmpty()) {
                        modelList
                    } else {
                        val filteredList : MutableList<CommonPopupListModel.ResponseData> =
                                ArrayList<CommonPopupListModel.ResponseData>()
                        for (row in modelList) {
                            if (row.name!!.lowercase(Locale.getDefault()).contains(
                                    charString.lowercase(Locale.getDefault())
                                )
                            ) {
                                filteredList.add(row)
                            }
                        }
                        filteredList
                    }
                    filterResults.values = listFilterData
                    return filterResults
                }

                @SuppressLint("SetTextI18n")
                override fun publishResults(
                        charSequence : CharSequence,
                        filterResults : FilterResults
                ) {
                    if (listFilterData.isEmpty()) {
                        tvFound.visibility = View.VISIBLE
                        tvFound.text = "Sorry we are not available in this state yet"
                        rvCountryList.visibility = View.GONE
                    } else {
                        tvFound.visibility = View.GONE
                        rvCountryList.visibility = View.VISIBLE
                        listFilterData =
                                filterResults.values as List<CommonPopupListModel.ResponseData>
                        notifyDataSetChanged()
                    }
                }
            }
        }

        inner class MyViewHolder(
                var bindingAdapter : CommonPopupLayoutBinding
        ) : RecyclerView.ViewHolder(
            bindingAdapter.root
        )
    }

    override fun onRequestPermissionsResult(
            requestCode : Int,
            permissions : Array<String?>, grantResults : IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestExternalStorage -> {
                if (grantResults.isEmpty()
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.e("onRequestPermissionsResult", "Cannot write images to external storage")
                }
            }
        }
    }

    fun verifyStoragePermissions() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val building = AlertDialog.Builder(ctx)
                building.setMessage(
                    """To upload image allow ${
                        ctx.getString(
                            R.string.app_name
                        )
                    } access to your device's files. 
Tap Setting > permission, and turn "Files and media" on."""
                )
                building.setCancelable(true)
                building.setPositiveButton(
                    ctx.getString(R.string.Settings)
                ) { dialogs : DialogInterface, _ : Int ->
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                    dialogs.dismiss()
                }
                building.setNegativeButton(
                    ctx.getString(R.string.not_now)
                ) { dialogs : DialogInterface, _ : Int -> dialogs.dismiss() }
                val alert11 = building.create()
                alert11.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
                alert11.show()
                alert11.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
                alert11.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    ctx,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    ctx, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    ctx, Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    act,
                    permissionsStorage,
                    requestExternalStorage
                )
            }
        }
    }

    companion object {
        var sampleCollected : String? = ""
        var weathering : String? = ""
        var rockStrength : String? = ""
        var waterCondition : String? = ""
        var typeOfGeologicalStructures : String? = ""
        var typeOfFaults : String? = ""
        var flagOC = "0"
        var ocmr = OpenCastMappingReport()
        var img : Bitmap? = null
        var geoSign = false
        var clientsGeoSign = false
        var geologistSignEdited = false
        var clientsGeologistSignEdited = false
    }

    override fun onBackPressed() {
        flagOC = "0"
        ocmr = OpenCastMappingReport()
        img = null
        finish()
    }
}