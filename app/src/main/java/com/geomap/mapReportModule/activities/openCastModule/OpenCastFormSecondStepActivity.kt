package com.geomap.mapReportModule.activities.openCastModule

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.geomap.DataBaseFunctions.Companion.saveOCReport
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastFormSecondStepBinding
import com.geomap.mapReportModule.activities.DashboardActivity
import com.geomap.mapReportModule.models.OpenCastInsertModel
import com.geomap.mapReportModule.models.SuccessModel
import com.geomap.roomDataBase.OpenCastMappingReport
import com.geomap.utils.APIClientProfile
import com.geomap.utils.CONSTANTS
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit.RetrofitError
import retrofit.mime.TypedFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class OpenCastFormSecondStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastFormSecondStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var ocDataModel = OpenCastInsertModel()
    private var userId : String? = null
    private var signCheck : String? = null
    private var sign : TypedFile? = null
    private var geologistSign : TypedFile? = null
    private var clientsGeologistSign : TypedFile? = null
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_form_second_step)
        ctx = this@OpenCastFormSecondStepActivity
        act = this@OpenCastFormSecondStepActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        binding.llMainLayout.visibility = View.VISIBLE
        binding.btnSubmit.visibility = View.VISIBLE

        if (intent.extras != null) {
            val gson = Gson()
            val data = intent.getStringExtra("ocData")
            val type1 = object : TypeToken<OpenCastInsertModel>() {}.type
            ocDataModel = gson.fromJson(data, type1)

            val photogeologistSign = File(getAlbumStorageDir("Pictures"),
                String.format("geologistSign.jpg", System.currentTimeMillis()))
            saveBitmapToJPG(ocDataModel.geologistSignBitMap!!, photogeologistSign)
            scanMediaFile(photogeologistSign)
            geologistSign = TypedFile(CONSTANTS.MULTIPART_FORMAT, photogeologistSign)

            val photoclientsGeologistSign = File(getAlbumStorageDir("Pictures"),
                String.format("clientsGeologistSign.jpg", System.currentTimeMillis()))
            saveBitmapToJPG(ocDataModel.clientsGeologistSignBitMap!!, photoclientsGeologistSign)
            scanMediaFile(photoclientsGeologistSign)
            clientsGeologistSign = TypedFile(CONSTANTS.MULTIPART_FORMAT, photoclientsGeologistSign)
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSignPadClear.setOnClickListener {
            binding.signPad.clear()
        }

        binding.signPad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                verifyStoragePermissions()
            }

            override fun onSigned() {
                binding.btnSignPadClear.isEnabled = true
                binding.btnSignPadClear.setTextColor(
                    ContextCompat.getColor(ctx, R.color.primary_theme))
                binding.btnSignPadClear.setBackgroundResource(R.drawable.border_enable_button)
                signCheck = "1"
            }

            override fun onClear() {
                binding.btnSignPadClear.isEnabled = false
                binding.btnSignPadClear.setTextColor(
                    ContextCompat.getColor(ctx, R.color.primary_theme))
                binding.btnSignPadClear.setBackgroundResource(R.drawable.border_enable_button)
                signCheck = ""
            }
        })

        binding.btnSubmit.setOnClickListener {
            postData()
        }
    }

    private fun postData() {
        if (signCheck == "") {
            showToast(getString(R.string.pls_add_geologist_sign), act)
        } else {
            postOpenCastInsert()
        }
    }

    private fun postOpenCastInsert() {
        val photoImage = File(getAlbumStorageDir("Pictures"),
            String.format("clientsGeologistSign.jpg", System.currentTimeMillis()))
        saveBitmapToJPG(binding.signPad.signatureBitmap, photoImage)
        scanMediaFile(photoImage)
        sign = TypedFile(CONSTANTS.MULTIPART_FORMAT, photoImage)
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            APIClientProfile.apiService!!.postOpenCastInsert(
                userId,
                ocDataModel.minesSiteName,
                ocDataModel.sheetNo,
                ocDataModel.pitName,
                ocDataModel.pitLocation,
                ocDataModel.shiftInchargeName,
                ocDataModel.geologistName,
                ocDataModel.faceLocation,
                ocDataModel.faceLengthM,
                ocDataModel.faceAreaM2,
                ocDataModel.faceRockTypes,
                ocDataModel.benchRL,
                ocDataModel.benchHeightWidth,
                ocDataModel.benchAngle,
                ocDataModel.thicknessOfOre,
                ocDataModel.thinessOfOverburden,
                ocDataModel.thicknessOfInterburden,
                ocDataModel.observedGradeOfOre,
                ocDataModel.actualGradeOfOre,
                ocDataModel.sampleCollected,
                ocDataModel.weathering,
                ocDataModel.rockStregth,
                ocDataModel.waterCondition,
                ocDataModel.typeOfGeologicalStructures,
                ocDataModel.typeOfFaults,
                ocDataModel.notes,
                ocDataModel.shift,
                ocDataModel.ocDate,
                ocDataModel.dipDirectionAngle,
                sign,
                geologistSign,
                clientsGeologistSign,
                object : retrofit.Callback<SuccessModel> {
                    override fun success(model : SuccessModel,
                        response : retrofit.client.Response) {
                        if (model.ResponseCode == ctx.getString(R.string.ResponseCodesuccess)) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            when (model.ResponseCode) {
                                ctx.getString(R.string.ResponseCodesuccess) -> {
                                    showToast(model.ResponseMessage, act)
                                    val i = Intent(ctx, DashboardActivity::class.java)
                                    i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(i)
                                    finishAffinity()
                                    ocDataModel = OpenCastInsertModel()
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
        } else {
            val obj = OpenCastMappingReport()
            obj.iD = 0
            obj.userId = userId
            obj.ocDate = ocDataModel.ocDate
            obj.mappingSheetNo = ocDataModel.sheetNo
            obj.minesSiteName = ocDataModel.minesSiteName
            obj.pitName = ocDataModel.pitName
            obj.pitLocation = ocDataModel.pitLocation
            obj.shiftInChargeName = ocDataModel.shiftInchargeName
            obj.geologistName = ocDataModel.geologistName
            obj.shift = ocDataModel.shift
            obj.faceLocation = ocDataModel.faceLocation
            obj.faceLength = ocDataModel.faceLengthM
            obj.faceArea = ocDataModel.faceAreaM2
            obj.faceRockTypes = ocDataModel.faceRockTypes
            obj.benchRL = ocDataModel.benchRL
            obj.benchHeightWidth = ocDataModel.benchHeightWidth
            obj.benchAngle = ocDataModel.benchAngle
            obj.dipDirectionAngle = ocDataModel.dipDirectionAngle
            obj.thicknessOfOre = ocDataModel.thicknessOfOre
            obj.thicknessOfOverburden = ocDataModel.thinessOfOverburden
            obj.thicknessOfInterBurden = ocDataModel.thicknessOfInterburden
            obj.observedGradeOfOre = ocDataModel.observedGradeOfOre
            obj.sampleCollected = ocDataModel.sampleCollected
            obj.actualGradOfOre = ocDataModel.actualGradeOfOre
            obj.weathering = ocDataModel.weathering
            obj.rockStrength = ocDataModel.rockStregth
            obj.waterCondition = ocDataModel.waterCondition
            obj.typeOfGeologicalStructures = ocDataModel.typeOfGeologicalStructures
            obj.typeOfFaults = ocDataModel.typeOfFaults
            obj.notes = ocDataModel.notes
            obj.geologistSign = ocDataModel.geologistSignBitMap!!
            obj.clientsGeologistSign = ocDataModel.clientsGeologistSignBitMap!!
            obj.image = binding.signPad.signatureBitmap
            saveOCReport(obj, ctx)
            binding.signPad.clear()
            showToast(getString(R.string.opencast_saved), act)
            val i = Intent(ctx, DashboardActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(i)
            finishAffinity()
        }
    }

    override fun onRequestPermissionsResult(requestCode : Int,
        permissions : Array<String?>, grantResults : IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isEmpty()
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.e("onRequestPermissionsResult", "Cannot write images to external storage")
                }
            }
        }
    }

    private fun scanMediaFile(photo : File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        ctx.sendBroadcast(mediaScanIntent)
    }

    private fun getAlbumStorageDir(albumName : String?) : File {
        val file = File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), albumName)
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created")
        }
        return file
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

    fun verifyStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                callFilesPermission()
            }
        } else {
            if (ActivityCompat.checkSelfPermission(ctx,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    ctx, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    ctx, Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    act,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun callFilesPermission() {
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

    override fun onBackPressed() {
        finish()
    }
}