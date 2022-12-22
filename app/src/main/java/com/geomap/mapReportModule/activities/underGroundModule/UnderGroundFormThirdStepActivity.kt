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
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageButton
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
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.flagUG
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.ugmr
import com.geomap.mapReportModule.models.AttributeDataModel
import com.geomap.mapReportModule.models.SuccessModel
import com.geomap.mapReportModule.models.UnderGroundInsertModel
import com.geomap.roomDataBase.UnderGroundMappingReport
import com.geomap.utils.APIClientProfile
import com.geomap.utils.CONSTANTS
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
    private lateinit var currPaint : ImageButton
    var ugDataModel = UnderGroundInsertModel()
    var i = 0
    private val requestExternalStorage = 1
    private val permissionsStorage = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
            if (intent.hasExtra("ugData")) {
                val gson = Gson()
                val data = intent.getStringExtra("ugData")
                val type1 = object : TypeToken<UnderGroundInsertModel>() {}.type
                ugDataModel = gson.fromJson(data, type1)
            }
            if (intent.hasExtra("attributeData")) {

                val gson = Gson()
                val data = intent.getStringExtra("attributeData")
                val type1 = object : TypeToken<ArrayList<AttributeDataModel>>() {}.type
                attributeDataModelList = gson.fromJson(data, type1)
            }
            intent.extras!!.clear()
        }

//        if (flagUG == "1" || flagUG == "2") {
            signRoofBitMap = ugmr.roofImage
            signLeftBitMap = ugmr.leftImage
            signRightBitMap = ugmr.rightImage
            signFaceBitMap = ugmr.faceImage
            if (signRoofBitMap != null) {
                if (binding.drawing.drawingCache != null) {
                    binding.drawing.startNew()
                    binding.drawing.background = getDrawable(R.drawable.grid_bg)
                }
                val d : Drawable = BitmapDrawable(resources!!, signRoofBitMap)
                binding.drawing.background = d
            } else {
                callDisable("0")
            }
//        }

        initViewSandVars()
        val gson = Gson()
        Log.e("UGData", gson.toJson(ugDataModel).toString())
        binding.btnClear.isEnabled = true
        binding.btnClear.setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
        binding.btnClear.setBackgroundResource(R.drawable.border_enable_button)
        binding.tvName.text = getString(R.string.roof)

        binding.btnClear.setOnClickListener {
            binding.drawing.startNew()
            binding.drawing.background = getDrawable(R.drawable.grid_bg)
        }

        binding.btnNext.setOnClickListener {
            binding.drawing.isDrawingCacheEnabled = true
            saveImage(binding.drawing.drawingCache)
            binding.drawing.isDrawingCacheEnabled = false
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initViewSandVars() {
        verifyStoragePermissions()
        currPaint = binding.paintColors.getChildAt(0) as ImageButton
        currPaint.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.paint_pressed))
    }

    private fun saveImage(image : Bitmap) {
        when (i) {
            0 -> {
                binding.tvName.text = getString(R.string.left)
                signRoofBitMap = image.copy(image.config, false)
                if (signLeftBitMap != null) {
                    callEnable(signLeftBitMap!!, "left")
                } else {
                    callDisable("1")
                }
                i++
            }
            1 -> {
                binding.tvName.text = getString(R.string.right)
                signLeftBitMap = image.copy(image.config, false)
                if (signRightBitMap != null) {
                    callEnable(signRightBitMap!!, "right")
                } else {
                    callDisable("1")
                }
                i++

            }
            2 -> {
                binding.btnNext.text = getString(R.string.submit)
                binding.tvName.text = getString(R.string.face)
                signRightBitMap = image.copy(image.config, false)
                if (signFaceBitMap != null) {
                    callEnable(signFaceBitMap!!, "face")
                } else {
                    callDisable("1")
                }
                i++
                binding.drawing.startNew()
            }
            3 -> {
                signFaceBitMap = image.copy(image.config, false)
                if (signFaceBitMap != null) {
                    callEnable(signFaceBitMap!!, "face")
                } else {
                    callDisable("1")
                }
                postUndergroundInsert()
            }
        }
    }

    private fun postUndergroundInsert() {
        val datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val roofImage = File(getAlbumStorageDir(),
            String.format(datetime + "RoofImage.jpg", System.currentTimeMillis()))
        saveBitmapToJPG(signRoofBitMap!!, roofImage)
        scanMediaFile(roofImage)
        signRoof = TypedFile(CONSTANTS.MULTIPART_FORMAT, roofImage)

        val leftImage = File(getAlbumStorageDir(),
            String.format(datetime + "LeftImage.jpg", System.currentTimeMillis()))
        saveBitmapToJPG(signLeftBitMap!!, leftImage)
        scanMediaFile(leftImage)
        signLeft = TypedFile(CONSTANTS.MULTIPART_FORMAT, leftImage)

        val rightImage = File(getAlbumStorageDir(),
            String.format(datetime + "RightImage.jpg", System.currentTimeMillis()))
        saveBitmapToJPG(signRightBitMap!!, rightImage)
        scanMediaFile(rightImage)
        signRight = TypedFile(CONSTANTS.MULTIPART_FORMAT, rightImage)

        val faceImage = File(getAlbumStorageDir(),
            String.format(datetime + "FaceImage.jpg", System.currentTimeMillis()))
        saveBitmapToJPG(signFaceBitMap!!, faceImage)
        scanMediaFile(faceImage)
        signFace = TypedFile(CONSTANTS.MULTIPART_FORMAT, faceImage)

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
                            getString(R.string.ResponseCodesuccess) -> {
                                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                                showToast(model.ResponseMessage, act)
                                binding.drawing.startNew()
                                val i = Intent(ctx, DashboardActivity::class.java)
                                i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                                startActivity(i)
                                finishAffinity()
                                ugDataModel = UnderGroundInsertModel()
                                attributeDataModelList = ArrayList<AttributeDataModel>()
                                ugmr = UnderGroundMappingReport()
                                flagUG = "0"
                            }
                            getString(R.string.ResponseCodefail) -> {
                                showToast(model.ResponseCode, act)
                            }
                            getString(
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
            obj.userId = userId
            obj.name = ugDataModel.name
            obj.comment = ugDataModel.comment
            obj.attributes = gson.toJson(attributeDataModelList)
            obj.ugDate = ugDataModel.ugDate
            obj.mapSerialNo = ""
            obj.shift = ugDataModel.shift
            obj.mappedBy = ugDataModel.mappedBy
            obj.scale = ugDataModel.scale
            obj.location = ugDataModel.location
            obj.veinOrLoad = ugDataModel.venieLoad
            obj.xCordinate = ugDataModel.xCordinate
            obj.yCordinate = ugDataModel.yCordinate
            obj.zCordinate = ugDataModel.zCordinate
            obj.roofImage = signRoofBitMap
            obj.leftImage = signLeftBitMap
            obj.rightImage = signRightBitMap
            obj.faceImage = signFaceBitMap
            if (flagUG == "2") {
                DataBaseFunctions.updateUGReport(obj, ctx)
                showToast(getString(R.string.underground_updated), act)
            } else {
                DataBaseFunctions.saveUGReport(obj, ctx)
                showToast(getString(R.string.underground_saved), act)
            }
            val i = Intent(ctx, DashboardActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(i)
            ugDataModel = UnderGroundInsertModel()
            attributeDataModelList = ArrayList<AttributeDataModel>()
            ugmr = UnderGroundMappingReport()
            flagUG = "0"
            finishAffinity()
        }
    }

    private fun callDisable(flag : String) {
        if (flag == "1") {
            binding.drawing.startNew()
        }
        binding.drawing.background = getDrawable(R.drawable.grid_bg)
    }

    private fun callEnable(signBitMap : Bitmap, bitmapString : String) {
        binding.drawing.startNew()
        binding.drawing.background = getDrawable(R.drawable.grid_bg)
        val d : Drawable = BitmapDrawable(resources!!, signBitMap)
        binding.drawing.background = d
        Log.e(bitmapString, "$i")
    }

    private fun scanMediaFile(photo : File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        ctx.sendBroadcast(mediaScanIntent)
    }

    private fun getAlbumStorageDir() : File {
        return File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), "Pictures")
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

    private fun verifyStoragePermissions() {
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
                    permissionsStorage,
                    requestExternalStorage
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
        binding.drawing.isDrawingCacheEnabled = true
        when (i) {
            0 -> {
                signRoofBitMap =
                    binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
                ugmr.roofImage = signRoofBitMap
                finish()
            }
            1 -> {
                signLeftBitMap =
                    binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
                ugmr.leftImage = signLeftBitMap
                callEnable(signRoofBitMap!!, "roof")
                binding.btnNext.text = getString(R.string.next)
                binding.tvName.text = getString(R.string.roof)
                i--
            }
            2 -> {
                signRightBitMap =
                    binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
                ugmr.rightImage = signRightBitMap
                callEnable(signLeftBitMap!!, "left")
                binding.btnNext.text = getString(R.string.next)
                binding.tvName.text = getString(R.string.left)
                i--
            }
            3 -> {
                signFaceBitMap =
                    binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
                ugmr.faceImage = signFaceBitMap
                callEnable(signRightBitMap!!, "right")
                binding.btnNext.text = getString(R.string.next)
                binding.tvName.text = getString(R.string.right)
                i--
            }
        }
        binding.drawing.isDrawingCacheEnabled = false
    }

    fun paintClicked(view : View) {
        binding.drawing.setErase(false)
        if (view !== currPaint) {
            val imgView = view as ImageButton
            binding.drawing.setColor(view.getTag().toString())
            imgView.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.paint_pressed))
            currPaint.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.paint))
            currPaint = view
        }
    }
}
