package com.geomap.mapReportModule.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.geomap.DataBaseFunctions
import com.geomap.DataBaseFunctions.Companion.deleteOCReport
import com.geomap.DataBaseFunctions.Companion.deleteUGReport
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivitySyncDataBinding
import com.geomap.mapReportModule.models.*
import com.geomap.roomDataBase.OpenCastMappingReport
import com.geomap.roomDataBase.UnderGroundMappingReport
import com.geomap.utils.APIClientProfile
import com.geomap.utils.CONSTANTS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit.RetrofitError
import retrofit.mime.TypedFile
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class SyncDataActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySyncDataBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private lateinit var ugList : ArrayList<UnderGroundMappingReport>
    private lateinit var ocList : ArrayList<OpenCastMappingReport>
    private var userId = ""
    private var gson = Gson()

    var ugModelList = ArrayList<SyncDataUgModel>()
    var ocModelList = ArrayList<SyncDataOcModel>()
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync_data)
        ctx = this@SyncDataActivity
        act = this@SyncDataActivity
        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")!!

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSyncData.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                showProgressBar(binding.progressBar, binding.progressBarHolder, act)
                getData()
            } else {
                showToast(getString(R.string.sync_data_internet_off), act)
            }
        }
    }

    private fun getData() {
        DB = getDataBase(ctx)
        DB.taskDao().geAllUnderGroundMappingReportASC(userId)
            .observe(ctx as LifecycleOwner) { lists ->
                ugList = lists as ArrayList<UnderGroundMappingReport>
                setugObjArray(ugList)
                Log.e(
                    "List UnderGroundMappingReport",
                    "true" + DataBaseFunctions.gson.toJson(ugList).toString()
                )
            }
    }

    private fun getAlbumStorageDir(albumName : String?) : File {
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created")
        }
        return file
    }

    private fun setugObjArray(ugList : ArrayList<UnderGroundMappingReport>) {
        if (ugList.isNotEmpty()) {
            for (i in ugList.indices) {
                val sdu = SyncDataUgModel()
                sdu.userId = userId
                sdu.name = ugList[i].name
                sdu.comment = ugList[i].comment
                sdu.ugDate = ugList[i].ugDate
                sdu.shift = ugList[i].shift
                sdu.mappedBy = ugList[i].mappedBy
                sdu.scale = ugList[i].scale
                sdu.location = ugList[i].location
                sdu.venieLoad = ugList[i].veinOrLoad
                sdu.xCordinate = ugList[i].xCordinate
                sdu.yCordinate = ugList[i].yCordinate
                sdu.zCordinate = ugList[i].zCordinate
                sdu.attribute = ugList[i].attributes

                if(ugList[i].roofImage!= null) {
                    sdu.roofImage = getImage("RoofImage", ugList[i].roofImage!!)
                }
                if(ugList[i].leftImage!= null) {
                    sdu.leftImage = getImage("LeftImage", ugList[i].leftImage!!)
                }
                if(ugList[i].rightImage!= null) {
                    sdu.rightImage = getImage("RightImage", ugList[i].rightImage!!)
                }
                if(ugList[i].faceImage!= null) {
                    sdu.faceImage = getImage("FaceImage", ugList[i].faceImage!!)
                }
                ugModelList.add(sdu)
                if (i == ugList.size - 1) {
                    callOcMethod()
                }
            }
        } else {
            callOcMethod()
        }
    }

    private fun callOcMethod() {
        DB.taskDao().geAllOpenCastMappingReportASC(userId).observe(ctx as LifecycleOwner) { lists ->
            ocList = lists as ArrayList<OpenCastMappingReport>
            Log.e(
                "List OpenCastMappingReport",
                "true" + DataBaseFunctions.gson.toJson(ocList).toString()
            )
            setocObjArray(ocList)
        }
    }

    private fun setocObjArray(ocList : ArrayList<OpenCastMappingReport>) {
        if (ocList.isNotEmpty()) {
            for (i in ocList.indices) {
                val sdu = SyncDataOcModel()
                sdu.userId = userId
                sdu.minesSiteName = ocList[i].minesSiteName
                sdu.pitName = ocList[i].pitName
                sdu.pitLoaction = ocList[i].pitLocation
                sdu.shiftInchargeName = ocList[i].shiftInChargeName
                sdu.geologistName = ocList[i].geologistName
                sdu.faceLocation = ocList[i].faceLocation
                sdu.faceLength = ocList[i].faceLength
                sdu.faceArea = ocList[i].faceArea
                sdu.faceRockType = ocList[i].faceRockTypes
                sdu.benchRl = ocList[i].benchRL
                sdu.benchHeightWidth = ocList[i].benchHeightWidth
                sdu.benchAngle = ocList[i].benchAngle
                sdu.thicknessOfOre = ocList[i].thicknessOfOre
                sdu.thicknessOfOverburdan = ocList[i].thicknessOfOverburden
                sdu.thicknessOfInterburden = ocList[i].thicknessOfInterBurden
                sdu.observedGradeOfOre = ocList[i].observedGradeOfOre
                sdu.actualGradeOfOre = ocList[i].actualGradOfOre
                sdu.sampleColledted = ocList[i].sampleCollected
                sdu.weathring = ocList[i].weathering
                sdu.rockStregth = ocList[i].rockStrength
                sdu.waterCondition = ocList[i].waterCondition
                sdu.typeOfGeologist = ocList[i].typeOfGeologicalStructures
                sdu.typeOfFaults = ocList[i].typeOfFaults
                sdu.notes = ocList[i].notes
                sdu.shift = ocList[i].shift
                sdu.ocDate = ocList[i].ocDate
                sdu.dipDirectionAndAngle = ocList[i].dipDirectionAngle

                if(ocList[i].image != null) {
                    sdu.image = getImage("image", ocList[i].image!!)
                }
                if(ocList[i].geologistSign != null) {
                    sdu.geologistSign = getImage("geologistSign", ocList[i].geologistSign!!)
                }
                if(ocList[i].clientsGeologistSign != null) {
                    sdu.clientsGeologistSign = getImage("clientsGeologistSign", ocList[i].clientsGeologistSign!!)
                }
                ocModelList.add(sdu)
                if (i == ocList.size - 1) {
                    callPostData()
                }
            }
        } else {
            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
            callPostData()
        }
    }

    private fun getImage(name : String, bitmap : Bitmap) : TypedFile? {
        var image : TypedFile? = null
        try {
            val file = File(cacheDir, "$name.jpg")
            val os : OutputStream = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.close()
            image = TypedFile(CONSTANTS.MULTIPART_FORMAT, file)
        } catch (_ : Exception) {
        }
        return image
    }

    private fun callPostData() {
        if (ugModelList.isNotEmpty()) {
            postData(0)
        } else if (ugModelList.isEmpty() && ocModelList.isNotEmpty()) {
            postOcData(0)
        } else if (ocModelList.isEmpty() && ugModelList.isEmpty()) {
            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
            showToast("Data is not available in Your Draft", act)
        }
    }

    private fun postData(i : Int) {
        if (isNetworkConnected(ctx)) {
            if (ugModelList.isNotEmpty()) {
                val attributeDataList : ArrayList<AttributeDataModel>
                val type1 = object : TypeToken<ArrayList<AttributeDataModel>>() {}.type
                attributeDataList = gson.fromJson(ugModelList[i].attribute, type1)

                showProgressBar(binding.progressBar, binding.progressBarHolder, act)
                APIClientProfile.apiService!!.postUndergroundInsert(ugModelList[i].userId,
                    ugModelList[i].name, ugModelList[i].comment, attributeDataList,
                    ugModelList[i].ugDate, "", ugModelList[i].shift,
                    ugModelList[i].mappedBy, ugModelList[i].scale, ugModelList[i].location,
                    ugModelList[i].venieLoad, ugModelList[i].xCordinate, ugModelList[i].yCordinate,
                    ugModelList[i].zCordinate, ugModelList[i].roofImage, ugModelList[i].leftImage,
                    ugModelList[i].rightImage, ugModelList[i].faceImage,
                    object : retrofit.Callback<SuccessModel> {
                        override fun success(
                            model : SuccessModel,
                            response : retrofit.client.Response
                        ) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            when (model.ResponseCode) {
                                ctx.getString(R.string.ResponseCodesuccess) -> {
                                    ugModelList.removeAt(0)
                                    if (ugModelList.isNotEmpty()) {
                                        postData(0)
                                    } else {
                                        deleteDB("0")
                                    }
                                }
                                ctx.getString(R.string.ResponseCodefail) -> {
                                    showToast(model.ResponseCode, act)
                                }
                                ctx.getString(R.string.ResponseCodeDeleted) -> {
                                    callDelete403(act, model.ResponseCode)
                                }
                            }
                        }

                        override fun failure(e : RetrofitError) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            showToast(e.message, act)
                        }
                    })
            } else {
                if (ocModelList.isNotEmpty()) {
                    postOcData(0)
                } else {
                    deleteDB("0")
                }
            }
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
        /*   if (isNetworkConnected(ctx)) {
                showProgressBar(binding.progressBar, binding.progressBarHolder, act)
                APIClientProfile.apiService!!.postSyncDataInsert(ugModelList,ocModelList,
                object : retrofit.Callback<SuccessModel> {
                    override fun success(model : SuccessModel,
                        response : retrofit.client.Response) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        when (model.ResponseCode) {
                            ctx.getString(R.string.ResponseCodesuccess) -> {
                                showToast(model.ResponseMessage, act)
                                deleteDB()
                                finish()
                            }
                            ctx.getString(R.string.ResponseCodefail) -> {
                                showToast(model.ResponseMessage, act)
                            }
                            ctx.getString(R.string.ResponseCodeDeleted) -> {
                                callDelete403(act, model.ResponseMessage)
                            }
                        }
                    }

                    override fun failure(e : RetrofitError) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        showToast(e.message, act)
                    }
                })
            } else {
                showToast(getString(R.string.no_server_found), act)
            }*/
    }

    private fun postOcData(i : Int) {
        if (isNetworkConnected(ctx)) {
            if (ocModelList.isNotEmpty()) {
                showProgressBar(binding.progressBar, binding.progressBarHolder, act)
                APIClientProfile.apiService!!.postOpenCastInsert(ocModelList[i].userId,
                    ocModelList[i].minesSiteName, "",
                    ocModelList[i].pitName, ocModelList[i].pitLoaction,
                    ocModelList[i].shiftInchargeName, ocModelList[i].geologistName,
                    ocModelList[i].faceLocation, ocModelList[i].faceLength, ocModelList[i].faceArea,
                    ocModelList[i].faceRockType, ocModelList[i].benchRl,
                    ocModelList[i].benchHeightWidth, ocModelList[i].benchAngle,
                    ocModelList[i].thicknessOfOre, ocModelList[i].thicknessOfOverburdan,
                    ocModelList[i].thicknessOfInterburden, ocModelList[i].observedGradeOfOre,
                    ocModelList[i].actualGradeOfOre, ocModelList[i].sampleColledted,
                    ocModelList[i].weathring, ocModelList[i].rockStregth,
                    ocModelList[i].waterCondition, ocModelList[i].typeOfGeologist,
                    ocModelList[i].typeOfFaults, ocModelList[i].notes, ocModelList[i].shift,
                    ocModelList[i].ocDate, ocModelList[i].dipDirectionAndAngle,
                    ocModelList[i].image, ocModelList[i].geologistSign,
                    ocModelList[i].clientsGeologistSign, object : retrofit.Callback<SuccessModel> {
                        override fun success(
                            model : SuccessModel,
                            response : retrofit.client.Response
                        ) {
                            if (model.ResponseCode == ctx.getString(R.string.ResponseCodesuccess)) {
                                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                                when (model.ResponseCode) {
                                    ctx.getString(R.string.ResponseCodesuccess) -> {
                                        ocModelList.removeAt(i)
                                        if (ocModelList.isNotEmpty()) {
                                            postOcData(0)
                                        } else {
                                            deleteDB("1")
                                        }
                                    }
                                    ctx.getString(R.string.ResponseCodefail) -> {
                                        showToast(model.ResponseMessage, act)
                                    }
                                    ctx.getString(R.string.ResponseCodeDeleted) -> {
                                        callDelete403(act, model.ResponseMessage)
                                    }
                                }
                            }
                        }

                        override fun failure(e : RetrofitError) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            showToast(e.message, act)
                        }
                    })
            } else {
                deleteDB("1")
            }
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    private fun deleteDB(flag : String?) {
        if (flag == "1") {
            deleteOCReport(ctx, userId)
            showToast(getString(R.string.all_data_has_been_synchronised), act)
            finish()
        } else {
            deleteUGReport(ctx, userId)
            if (ocModelList.isNotEmpty()) {
                postOcData(0)
            } else {
                showToast(getString(R.string.all_data_has_been_synchronised), act)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}