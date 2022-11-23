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
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.geomap.DataBaseFunctions
import com.geomap.DataBaseFunctions.Companion.deleteOCReport
import com.geomap.DataBaseFunctions.Companion.deleteUGReport
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivitySyncDataBinding
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity
import com.geomap.mapReportModule.models.*
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.roomDataBase.GeoMapDatabase
import com.geomap.roomDataBase.OpenCastMappingReport
import com.geomap.roomDataBase.UnderGroundMappingReport
import com.geomap.utils.APIClientProfile
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.google.gson.Gson
import retrofit.RetrofitError
import retrofit.mime.TypedFile
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SyncDataActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySyncDataBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private lateinit var ugList : java.util.ArrayList<UnderGroundMappingReport>
    private lateinit var ocList : java.util.ArrayList<OpenCastMappingReport>
    private var userId = ""
    private var gson = Gson()

    var ugModelList = java.util.ArrayList<SyncDataUgModel>()
    var ocModelList = java.util.ArrayList<SyncDataOcModel>()
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
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            getData()
        }
    }

    private fun getData() {

        DB = getDataBase(ctx)
        DB.taskDao().geAllUnderGroundMappingReportASC().observe(ctx as LifecycleOwner){lists ->
            ugList  = lists as java.util.ArrayList<UnderGroundMappingReport>
            setugObjArray(ugList)
            Log.e("List UnderGroundMappingReport", "true" + DataBaseFunctions.gson.toJson(ugList).toString())
        }
    }
    private fun getAlbumStorageDir(albumName : String?) : File {
        val file = File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), albumName)
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created")
        }
        return file
    }
    private fun setugObjArray(ugList: ArrayList<UnderGroundMappingReport>) {

        if(ugList.isNotEmpty()) {
            for (i in ugList.indices) {
                var sdu = SyncDataUgModel()
                sdu.userId = userId
                sdu.name = ugList[i].name
                sdu.comment = ugList[i].comment
                sdu.ugDate = ugList[i].ugDate
                sdu.mapSerialNo = ugList[i].mapSerialNo
                sdu.shift = ugList[i].shift
                sdu.mappedBy = ugList[i].mappedBy
                sdu.scale = ugList[i].scale
                sdu.location = ugList[i].locations
                sdu.venieLoad = ugList[i].veinOrLoad
                sdu.xCordinate = ugList[i].xCordinate
                sdu.yCordinate = ugList[i].yCordinate
                sdu.zCordinate = ugList[i].zCordinate
                sdu.attribute = ugList[i].attributes
                var datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                    Date())

                var photo = File(getAlbumStorageDir("Pictures"),
                    String.format(datetime + "roofImage.jpg", System.currentTimeMillis()))
                saveBitmapToJPG(ugList[i].roofImage!!, photo)
                scanMediaFile(photo)

                sdu.roofImage = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                    Date())
                photo = File(getAlbumStorageDir("Pictures"),
                    String.format(datetime + "leftImage.jpg", System.currentTimeMillis()))
                saveBitmapToJPG(ugList[i].leftImage!!, photo)
                scanMediaFile(photo)

                sdu.leftImage = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                    Date())
                photo = File(getAlbumStorageDir("Pictures"),
                    String.format(datetime + "rightImage.jpg", System.currentTimeMillis()))
                saveBitmapToJPG(ugList[i].rightImage!!, photo)
                scanMediaFile(photo)

                sdu.rightImage = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)
                datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                    Date())
                photo = File(getAlbumStorageDir("Pictures"),
                    String.format(datetime + "faceImage.jpg", System.currentTimeMillis()))
                saveBitmapToJPG(ugList[i].faceImage!!, photo)
                scanMediaFile(photo)
                sdu.faceImage = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                ugModelList.add(sdu)
                if(i == ugList.size - 1){
                    callOcMethod()
                }
            }
        }else{
            callOcMethod()
        }
    }

    private fun callOcMethod() {

        DB.taskDao().geAllOpenCastMappingReportASC().observe(ctx as LifecycleOwner){lists ->
            ocList = lists as java.util.ArrayList<OpenCastMappingReport>
            Log.e("List OpenCastMappingReport", "true" + DataBaseFunctions.gson.toJson(ocList).toString())
            setocObjArray(ocList)
        }
    }

    private fun setocObjArray(ocList: java.util.ArrayList<OpenCastMappingReport>) {
        if(ocList.isNotEmpty()) {

            for (i in ocList.indices) {
                var sdu = SyncDataOcModel()
                sdu.userId = userId
                sdu.minesSiteName = ocList[i].minesSiteName
                sdu.mappingSheetNo = ocList[i].mappingSheetNo
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

                var datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                    Date())
                var photo = File(getAlbumStorageDir("Pictures"),
                    String.format(datetime + "image.jpg", System.currentTimeMillis()))
                saveBitmapToJPG(ocList[i].image!!, photo)
                scanMediaFile(photo)

                sdu.image  = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                    Date())
                photo = File(getAlbumStorageDir("Pictures"),
                    String.format(datetime + "geologistSign.jpg", System.currentTimeMillis()))
                saveBitmapToJPG(ocList[i].geologistSign!!, photo)
                scanMediaFile(photo)

                sdu.geologistSign = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                    Date())
                photo = File(getAlbumStorageDir("Pictures"),
                    String.format(datetime + "ClientSign.jpg", System.currentTimeMillis()))
                saveBitmapToJPG(ocList[i].clientsGeologistSign!!, photo)
                scanMediaFile(photo)

                sdu.clientsGeologistSign = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                ocModelList.add(sdu)
                if(i == ocList.size - 1){
                    callPostData()
                }
            }
        }else{
            callPostData()
        }
    }
    @Throws(IOException::class) fun saveBitmapToJPG(bitmap : Bitmap, photo : File?) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream : OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    private fun scanMediaFile(photo : File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        ctx.sendBroadcast(mediaScanIntent)
    }
    private fun callPostData() {
        Log.e("SyncData UG Report", gson.toJson(ugModelList).toString())
        Log.e("SyncData OC Report",   gson.toJson(ocModelList).toString())

        if(ocModelList.isNotEmpty() || ugModelList.isNotEmpty() ) {
            postData(0)
        }else if(ocModelList.isEmpty() && ugModelList.isEmpty()){
            showToast("Data is not available in Your Draft",act)
        }
    }

    private fun postData(i:Int) {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            APIClientProfile.apiService!!.postUndergroundInsert(
                userId, ugModelList[i].name, ugModelList[i].comment,
                UnderGroundFormFirstStepActivity.attributeDataModelList,
                ugModelList[i].ugDate,
                ugModelList[i].mapSerialNo,
                ugModelList[i].shift, ugModelList[i].mappedBy, ugModelList[i].scale, ugModelList[i].location,
                ugModelList[i].venieLoad,
                ugModelList[i].xCordinate, ugModelList[i].yCordinate, ugModelList[i].zCordinate,
                ugModelList[i].roofImage, ugModelList[i].leftImage,
                ugModelList[i].rightImage, ugModelList[i].faceImage,
                object : retrofit.Callback<SuccessModel> {
                    override fun success(model : SuccessModel,
                        response : retrofit.client.Response) {
                        when (model.ResponseCode) {
                            ctx.getString(R.string.ResponseCodesuccess) -> {
                                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                                showToast(model.ResponseMessage, act)
                              ugModelList.removeAt(i)
                                if(ugModelList.isNotEmpty()){
                                    postData(0)
                                }else{
                                    deleteDB("0")

                                }
                            }
                            ctx.getString(R.string.ResponseCodefail) -> {
                                showToast(model.ResponseCode, act)
                            }
                            ctx.getString(
                                R.string.ResponseCodeDeleted) -> {
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

        /*if (isNetworkConnected(ctx)) {
            RetrofitService.getInstance().postSyncDataInsert(gson.toJson(ugModelList),gson.toJson(ocModelList)).enqueue(object : Callback<SuccessModel> {
                override fun onResponse(call : Call<SuccessModel>,
                    response : Response<SuccessModel>) {
                    try {
                        val model : SuccessModel = response.body()!!
                        when (model.ResponseCode) {
                            getString(R.string.ResponseCodesuccess) -> {
                                showToast(model.ResponseMessage, act)
                                deleteDB()
                                finish()
                            }
                            getString(R.string.ResponseCodefail) -> {
                                showToast(model.ResponseMessage, act)
                            }
                            getString(R.string.ResponseCodeDeleted) -> {
                                callDelete403(act, model.ResponseMessage)
                            }
                        }
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call : Call<SuccessModel>, t : Throwable) {

                }
            })
        } else {
        }*/
    }

    private fun postOcData(i: Int) {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            APIClientProfile.apiService!!.postOpenCastInsert(
                userId,
                ocModelList[i].minesSiteName,
                ocModelList[i].mappingSheetNo,
                ocModelList[i].pitName,
                ocModelList[i].pitLoaction,
                ocModelList[i].shiftInchargeName,
                ocModelList[i].geologistName,
                ocModelList[i].faceLocation,
                ocModelList[i].faceLength,
                ocModelList[i].faceArea,
                ocModelList[i].faceRockType,
                ocModelList[i].benchRl,
                ocModelList[i].benchHeightWidth,
                ocModelList[i].benchAngle,
                ocModelList[i].thicknessOfOre,
                ocModelList[i].thicknessOfOverburdan,
                ocModelList[i].thicknessOfInterburden,
                ocModelList[i].observedGradeOfOre,
                ocModelList[i].actualGradeOfOre,
                ocModelList[i].sampleColledted,
                ocModelList[i].weathring,
                ocModelList[i].rockStregth,
                ocModelList[i].waterCondition,
                ocModelList[i].typeOfGeologist,
                ocModelList[i].typeOfFaults,
                ocModelList[i].notes,
                ocModelList[i].shift,
                ocModelList[i].ocDate,
                ocModelList[i].dipDirectionAndAngle,
                ocModelList[i].image,
                ocModelList[i].geologistSign,
                ocModelList[i].clientsGeologistSign,
                object : retrofit.Callback<SuccessModel> {
                    override fun success(model : SuccessModel,
                        response : retrofit.client.Response) {
                        if (model.ResponseCode == ctx.getString(R.string.ResponseCodesuccess)) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            when (model.ResponseCode) {
                                ctx.getString(R.string.ResponseCodesuccess) -> {
                                    showToast(model.ResponseMessage, act)
                                    ocModelList.removeAt(i)
                                    if(ocModelList.isNotEmpty()){
                                        postOcData(0)
                                    }else{
                                        deleteDB("1")
                                    }
                                }
                                ctx.getString(
                                    R.string.ResponseCodefail) -> {
                                    showToast(model.ResponseMessage, act)
                                }
                                ctx.getString(
                                    R.string.ResponseCodeDeleted) -> {
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
        }else{
            showToast(getString(R.string.no_server_found), act)
        }
    }

    private fun deleteDB(flag :String?) {
        if(flag == "1"){
            deleteOCReport(ctx)
            finish()
        }else {
            deleteUGReport(ctx)
            if(ocModelList.isNotEmpty()){
                postOcData(0)
            }else{
                finish()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}