package com.geomap.mapReportModule.activities.underGroundModule

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.geomap.DataBaseFunctions
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormThirdStepBinding
import com.geomap.mapReportModule.activities.DashboardActivity
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.attributeDataModelList
import com.geomap.mapReportModule.models.AttributeDataModel
import com.geomap.mapReportModule.models.SuccessModel
import com.geomap.mapReportModule.models.UnderGroundInsertModel
import com.geomap.roomDataBase.UnderGroundMappingReport
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
import java.text.SimpleDateFormat
import java.util.*

class UnderGroundFormThirdStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundFormThirdStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var signRoofBitMap : Bitmap? = null
    private var signLeftBitMap : Bitmap? = null
    private var signRightBitMap : Bitmap? = null
    private var signFaceBitMap : Bitmap? = null
    private var signRoof : TypedFile? = null
    private var signLeft : TypedFile? = null
    private var signRight : TypedFile? = null
    private var signFace : TypedFile? = null
    var ugDataModel = UnderGroundInsertModel()
    var i = 0
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_under_ground_form_third_step)
        ctx = this@UnderGroundFormThirdStepActivity
        act = this@UnderGroundFormThirdStepActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            val gson = Gson()
            val data = intent.getStringExtra("ugData")
            val type1 = object : TypeToken<UnderGroundInsertModel>() {}.type
            ugDataModel = gson.fromJson(data, type1)
            intent.extras!!.clear()
        }

        if (intent.extras != null) {
            val gson = Gson()
            val data = intent.getStringExtra("attributeData")
            val type1 = object : TypeToken<ArrayList<AttributeDataModel>>() {}.type
            attributeDataModelList = gson.fromJson(data, type1)
        }

        val gson = Gson()
        Log.e("UGData", gson.toJson(ugDataModel).toString())

        binding.tvName.text = getString(R.string.roof)

        binding.btnClear.setOnClickListener {
            binding.signPad.clear()
        }

        binding.btnNext.setOnClickListener {
            if (binding.btnClear.isEnabled) {
                addJpgSignToGallery(binding.signPad.signatureBitmap)
            }
        }

        binding.signPad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                verifyStoragePermissions()
            }

            override fun onSigned() {
                binding.btnClear.isEnabled = true
                binding.btnClear.setBackgroundResource(R.drawable.border_enable_button)
            }

            override fun onClear() {
                binding.btnClear.isEnabled = false
                binding.btnClear.setBackgroundResource(R.drawable.disable_button)
            }
        })

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun postUndergroundInsert() {
        binding.signPad.clear()
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            APIClientProfile.apiService!!.postUndergroundInsert(
                userId, ugDataModel.name, ugDataModel.comment, attributeDataModelList,
                ugDataModel.ugDate,
                ugDataModel.mapSerialNo,
                ugDataModel.shift, ugDataModel.mappedBy, ugDataModel.scale, ugDataModel.location,
                ugDataModel.venieLoad,
                ugDataModel.xCordinate, ugDataModel.yCordinate, ugDataModel.zCordinate,
                signRoof, signLeft,
                signRight, signFace,
                object : retrofit.Callback<SuccessModel> {
                    override fun success(model : SuccessModel,
                        response : retrofit.client.Response) {
                        when (model.ResponseCode) {
                            ctx.getString(R.string.ResponseCodesuccess) -> {
                                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                                showToast(model.ResponseMessage, act)
                                val i = Intent(ctx, DashboardActivity::class.java)
                                i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                                startActivity(i)
                                finishAffinity()
                                ugDataModel = UnderGroundInsertModel()
                                attributeDataModelList = java.util.ArrayList<AttributeDataModel>()
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
            val obj = UnderGroundMappingReport()
            val gson = Gson()
            obj.iD = 0
            obj.userId = userId
            obj.name = ugDataModel.name
            obj.comment = ugDataModel.comment
            obj.attributes = gson.toJson(attributeDataModelList)
            obj.ugDate = ugDataModel.ugDate
            obj.mapSerialNo = ugDataModel.mapSerialNo
            obj.shift = ugDataModel.shift
            obj.mappedBy = ugDataModel.mappedBy
            obj.scale = ugDataModel.scale
            obj.locations = ugDataModel.location
            obj.veinOrLoad = ugDataModel.venieLoad
            obj.xCordinate = ugDataModel.xCordinate
            obj.yCordinate = ugDataModel.yCordinate
            obj.zCordinate = ugDataModel.zCordinate
            obj.roofImage = signRoofBitMap
            obj.leftImage = signLeftBitMap
            obj.rightImage = signRightBitMap
            obj.faceImage = signFaceBitMap
            DataBaseFunctions.saveUGReport(obj, ctx)
            showToast(getString(R.string.underground_saved), act)
            val i = Intent(ctx, DashboardActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(i)
            ugDataModel = UnderGroundInsertModel()
            attributeDataModelList = java.util.ArrayList<AttributeDataModel>()
            finishAffinity()
        }
    }

    private fun addJpgSignToGallery(signature : Bitmap) : Boolean {
        var result = false
        val datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        try {
            when (i) {
                0 -> {
                    val photo = File(getAlbumStorageDir("Pictures"),
                        String.format(datetime + "signRoof.jpg", System.currentTimeMillis()))
                    saveBitmapToJPG(signature, photo)
                    scanMediaFile(photo)
                    signRoof = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)
                    binding.tvName.text = getString(R.string.left)
                    signRoofBitMap = signature
                    if (signLeftBitMap != null) {
                        callEnable(signLeftBitMap!!, "left")
                    } else {
                        callDisable()
                    }
                    i++
                }
                1 -> {
                    val photo = File(getAlbumStorageDir("Pictures"),
                        String.format(datetime + "signLeft.jpg", System.currentTimeMillis()))
                    saveBitmapToJPG(signature, photo)
                    scanMediaFile(photo)
                    signLeft = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)
                    binding.tvName.text = getString(R.string.right)
                    signLeftBitMap = signature
                    if (signRightBitMap != null) {
                        callEnable(signRightBitMap!!, "right")
                    } else {
                        callDisable()
                    }
                    i++
                }
                2 -> {
                    val photo = File(getAlbumStorageDir("Pictures"),
                        String.format(datetime + "signRight.jpg", System.currentTimeMillis()))
                    saveBitmapToJPG(signature, photo)
                    scanMediaFile(photo)
                    binding.btnNext.text = getString(R.string.submit)
                    signRight = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)
                    binding.tvName.text = getString(R.string.face)
                    signRightBitMap = signature
                    if (signFaceBitMap != null) {
                        callEnable(signFaceBitMap!!, "face")
                    } else {
                        callDisable()
                    }
                    i++
                }
                3 -> {
                    val photo = File(getAlbumStorageDir("Pictures"),
                        String.format(datetime + "signFace.jpg", System.currentTimeMillis()))
                    saveBitmapToJPG(signature, photo)
                    scanMediaFile(photo)
                    signFace = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)
                    signFaceBitMap = signature
                    postUndergroundInsert()
                    Log.e("face", signFace!!.toString() + i)
                }
            }
            result = true
        } catch (e : IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun callDisable() {
        binding.signPad.clear()
        binding.btnClear.isEnabled = false
        binding.btnClear.setBackgroundResource(R.drawable.disable_button)
    }

    private fun callEnable(signBitMap : Bitmap, bitmapString : String) {
        binding.signPad.signatureBitmap = signBitMap
        Log.e(bitmapString, "$i")
        binding.btnClear.isEnabled = true
        binding.btnClear.setBackgroundResource(R.drawable.border_enable_button)
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
        when (i) {
            0 -> {
                finish()
            }
            1 -> {
                callEnable(signRoofBitMap!!, "Roof")
                binding.btnNext.text = getString(R.string.next)
                binding.tvName.text = getString(R.string.roof)
                i--
            }
            2 -> {
                callEnable(signLeftBitMap!!, "left")
                binding.btnNext.text = getString(R.string.next)
                binding.tvName.text = getString(R.string.left)
                i--
            }
            3 -> {
                callEnable(signRightBitMap!!, "right")
                binding.btnNext.text = getString(R.string.next)
                binding.tvName.text = getString(R.string.right)
                i--
            }
        }
    }
}
