package com.geomap.mapReportModule.activities.openCastModule

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastFormSecondStepBinding
import com.geomap.mapReportModule.models.OpenCastInsertModel
import com.geomap.mapReportModule.models.SuccessModel
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

class OpenCastFormSecondStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastFormSecondStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var ocDataModel = OpenCastInsertModel()
    private var userId : String? = null
    private var signCheck : String? = null
    private var sign : TypedFile? = null
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE)

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
        }

        val gson = Gson()
        Log.e("OCData", gson.toJson(ocDataModel).toString())

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSignPadClear.setOnClickListener {
            binding.signPad.clear()
        }

        binding.signPad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                verifyStoragePermissions(act)
            }

            override fun onSigned() {
                binding.btnSignPadClear.isEnabled = true
                binding.btnSignPadClear.setBackgroundResource(R.drawable.enable_button)
                signCheck = "1"
            }

            override fun onClear() {
                binding.btnSignPadClear.isEnabled = false
                binding.btnSignPadClear.setBackgroundResource(R.drawable.disable_button)
                signCheck = ""
            }
        })

        binding.btnSubmit.setOnClickListener {
            if (signCheck == "") {
                showToast(getString(R.string.pls_add_geologist_sign), act)
            } else {
                postOpenCastInsert()
            }
        }
    }

    private fun postOpenCastInsert() {
        if (isNetworkConnected(ctx)) {
            addJpgSignatureToGallery(binding.signPad.signatureBitmap)
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            APIClientProfile.apiService!!.postOpenCastInsert(
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
                sign, ocDataModel.geologistSign, ocDataModel.clientsGeologistSign,
                object : retrofit.Callback<SuccessModel> {
                    override fun success(model : SuccessModel,
                        response : retrofit.client.Response) {
                        if (model.responseCode.equals(
                                ctx.getString(R.string.ResponseCodesuccess))) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            showToast(model.responseMessage, act)
                            finish()
                        } else if (model.responseCode.equals(
                                ctx.getString(R.string.ResponseCodeDeleted))) {
                            callDelete403(act, model.responseMessage)
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

    private fun addJpgSignatureToGallery(signature : Bitmap) : Boolean {
        var result = false
        try {
            val photo = File(getAlbumStorageDir("Pictures"),
                String.format("geologistSign.jpg", System.currentTimeMillis()))
            saveBitmapToJPG(signature, photo)
            scanMediaFile(photo)
            sign = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)
            Log.e("geologistSign", sign!!.toString())
            result = true
        } catch (e : IOException) {
            e.printStackTrace()
        }
        return result
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

    fun verifyStoragePermissions(activity : Activity?) {
        if (ActivityCompat.checkSelfPermission(ctx,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ctx, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                act,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )

        }
    }

    override fun onBackPressed() {
        finish()
    }
}