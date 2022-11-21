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
import retrofit.RetrofitError
import retrofit.mime.TypedFile
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SyncDataActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySyncDataBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private lateinit var ugList : java.util.ArrayList<UnderGroundMappingReport>
    private lateinit var ocList : java.util.ArrayList<OpenCastMappingReport>
    private var setUg = false
    private var setOc = false
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
        DB.taskDao().geAllUnderGroundMappingReport1().observe(ctx as LifecycleOwner){lists ->
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

                var photo = File(getAlbumStorageDir("Pictures"),
                    String.format("geologistSign.jpg", System.currentTimeMillis()))
                var newBitmap = Bitmap.createBitmap(ugList[i].roofImage!!.width,
                    ugList[i].roofImage!!.height, Bitmap.Config.ARGB_8888)
                var canvas = Canvas(newBitmap)
                canvas.drawColor(Color.WHITE)
                canvas.drawBitmap(ugList[i].roofImage!!, 0f, 0f, null)
                var stream: OutputStream = FileOutputStream(photo)
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                stream.close()
                var mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                var contentUri = Uri.fromFile(photo)
                mediaScanIntent.data = contentUri
                ctx.sendBroadcast(mediaScanIntent)

                sdu.roofImage = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                photo = File(getAlbumStorageDir("Pictures"),
                    String.format("geologistSign.jpg", System.currentTimeMillis()))
                newBitmap = Bitmap.createBitmap(ugList[i].leftImage!!.width,
                    ugList[i].leftImage!!.height, Bitmap.Config.ARGB_8888)
                canvas = Canvas(newBitmap)
                canvas.drawColor(Color.WHITE)
                canvas.drawBitmap(ugList[i].leftImage!!, 0f, 0f, null)
                stream = FileOutputStream(photo)
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                stream.close()
                mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                contentUri = Uri.fromFile(photo)
                mediaScanIntent.data = contentUri
                ctx.sendBroadcast(mediaScanIntent)

                sdu.leftImage = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                photo = File(getAlbumStorageDir("Pictures"),
                    String.format("geologistSign.jpg", System.currentTimeMillis()))
                newBitmap = Bitmap.createBitmap(ugList[i].rightImage!!.width,
                    ugList[i].rightImage!!.height, Bitmap.Config.ARGB_8888)
                canvas = Canvas(newBitmap)
                canvas.drawColor(Color.WHITE)
                canvas.drawBitmap(ugList[i].rightImage!!, 0f, 0f, null)
                stream = FileOutputStream(photo)
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                stream.close()
                mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                contentUri = Uri.fromFile(photo)
                mediaScanIntent.data = contentUri
                ctx.sendBroadcast(mediaScanIntent)

                sdu.rightImage = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                photo = File(getAlbumStorageDir("Pictures"),
                    String.format("geologistSign.jpg", System.currentTimeMillis()))
                newBitmap = Bitmap.createBitmap(ugList[i].faceImage!!.width,
                    ugList[i].faceImage!!.height, Bitmap.Config.ARGB_8888)
                canvas = Canvas(newBitmap)
                canvas.drawColor(Color.WHITE)
                canvas.drawBitmap(ugList[i].faceImage!!, 0f, 0f, null)
                stream = FileOutputStream(photo)
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                stream.close()
                mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                contentUri = Uri.fromFile(photo)
                mediaScanIntent.data = contentUri
                ctx.sendBroadcast(mediaScanIntent)
                sdu.faceImage = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)
                ugModelList.add(sdu)
                if(i < ugList.size - 1){
                    callOcMethod()
                }
            }
        }else{
            callOcMethod()
        }
    }

    private fun callOcMethod() {
        setUg = true

        DB.taskDao().geAllOpenCastMappingReport1().observe(ctx as LifecycleOwner){lists ->
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

                var photo = File(getAlbumStorageDir("Pictures"),
                    String.format("geologistSign.jpg", System.currentTimeMillis()))
                var newBitmap = Bitmap.createBitmap(ocList[i].image!!.width,
                    ocList[i].image!!.height, Bitmap.Config.ARGB_8888)
                var canvas = Canvas(newBitmap)
                canvas.drawColor(Color.WHITE)
                canvas.drawBitmap(ocList[i].image!!, 0f, 0f, null)
                var stream: OutputStream = FileOutputStream(photo)
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                stream.close()
                var mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                var contentUri = Uri.fromFile(photo)
                mediaScanIntent.data = contentUri
                ctx.sendBroadcast(mediaScanIntent)

                sdu.image = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                photo = File(getAlbumStorageDir("Pictures"),
                    String.format("geologistSign.jpg", System.currentTimeMillis()))
                newBitmap = Bitmap.createBitmap(ocList[i].geologistSign!!.width,
                    ocList[i].geologistSign!!.height, Bitmap.Config.ARGB_8888)
                canvas = Canvas(newBitmap)
                canvas.drawColor(Color.WHITE)
                canvas.drawBitmap(ocList[i].geologistSign!!, 0f, 0f, null)
                stream = FileOutputStream(photo)
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                stream.close()
                mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                contentUri = Uri.fromFile(photo)
                mediaScanIntent.data = contentUri
                ctx.sendBroadcast(mediaScanIntent)

                sdu.geologistSign = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                photo = File(getAlbumStorageDir("Pictures"),
                    String.format("geologistSign.jpg", System.currentTimeMillis()))
                newBitmap = Bitmap.createBitmap(ocList[i].clientsGeologistSign!!.width,
                    ocList[i].clientsGeologistSign!!.height, Bitmap.Config.ARGB_8888)
                canvas = Canvas(newBitmap)
                canvas.drawColor(Color.WHITE)
                canvas.drawBitmap(ocList[i].clientsGeologistSign!!, 0f, 0f, null)
                stream = FileOutputStream(photo)
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                stream.close()
                mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                contentUri = Uri.fromFile(photo)
                mediaScanIntent.data = contentUri
                ctx.sendBroadcast(mediaScanIntent)

                sdu.clientsGeologistSign = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

                ocModelList.add(sdu)
                if(i < ocList.size - 1){
                    callPostData()
                }
            }
        }else{
            callPostData()
        }
    }

    private fun callPostData() {

        setOc = true
        Log.e("List UG MappingReport", "true" + gson.toJson(ugModelList).toString())
        Log.e("List OpenCast MappingReport", "true" + gson.toJson(ocModelList).toString())
        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
        if(ocModelList.isNotEmpty() || ugModelList.isNotEmpty() ) {
            postData()
        }
    }

    private fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            APIClientProfile.apiService!!.postSyncDataInsert(ugModelList,ocModelList,
            object : retrofit.Callback<SuccessModel> {
                override fun success(model : SuccessModel,
                    response : retrofit.client.Response) {
                    hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                    if (model.ResponseCode.equals(
                            ctx.getString(R.string.ResponseCodesuccess))) {
                        showToast(model.ResponseMessage, act)
                        deleteDB()
                        finish()
                    } else if (model.ResponseCode.equals(
                            ctx.getString(R.string.ResponseCodefail))) {
                        showToast(model.ResponseMessage, act)
                    } else if (model.ResponseCode.equals(
                            ctx.getString(R.string.ResponseCodeDeleted))) {
                        callDelete403(act, model.ResponseMessage)
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
    }

    private fun deleteDB() {
        deleteUGReport(ctx)
        deleteOCReport(ctx)
    }

    override fun onBackPressed() {
        finish()
    }
}