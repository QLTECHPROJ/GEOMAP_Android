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

class UnderGroundFormSevenStepActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnderGroundFormThirdStepBinding
    private lateinit var ctx: Context
    private lateinit var act: Activity
    private var userId: String? = null
    private var signRoof: TypedFile? = null
    private var signLeft: TypedFile? = null
    private var signRight: TypedFile? = null
    private var signFace: TypedFile? = null
    private var signRoofBitMap: Bitmap? = null
    private var signLeftBitMap: Bitmap? = null
    private var signRightBitMap: Bitmap? = null
    private var signFaceBitMap: Bitmap? = null
    private lateinit var currPaint: ImageButton
    var ugDataModel = UnderGroundInsertModel()
    var i = 0
    private val requestExternalStorage = 1
    private val permissionsStorage = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_under_ground_form_third_step)
        ctx = this@UnderGroundFormSevenStepActivity
        act = this@UnderGroundFormSevenStepActivity
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
        if (flagUG == "1" || flagUG == "2") {
            signFaceBitMap = ugmr.faceImage
            if (signFaceBitMap != null) {
                callEnable(signFaceBitMap!!, "roof")
            } else {
                callDisable("0")
            }
        }

        initViewSandVars()
        binding.drawing.isDrawingCacheEnabled = true
        val gson = Gson()
        Log.e("UGData", gson.toJson(ugDataModel).toString())
        binding.btnClear.isEnabled = true
        binding.btnClear.setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
        binding.btnClear.setBackgroundResource(R.drawable.border_enable_button)
        binding.tvName.text = getString(R.string.face)
        binding.btnNext.text = getString(R.string.submit)

        binding.btnClear.setOnClickListener {
            if (binding.drawing.drawingCache != null) {
                binding.drawing.destroyDrawingCache()
                binding.drawing.startNew()
                binding.drawing.background = getDrawable(R.drawable.grid_bg)
            }
        }


        binding.btnNext.setOnClickListener { //            if (binding.btnClear.isEnabled) {
            saveImage(binding.drawing.drawingCache) //            }
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

    private fun postUndergroundInsert() {
        var datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        var photo = File(getAlbumStorageDir("Pictures"),
            String.format(datetime + "signLeftBitMap.jpg", System.currentTimeMillis()))
        saveBitmapToJPG(ugmr.leftImage!!, photo)
        scanMediaFile(photo)
        signLeft = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)
        datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        photo = File(getAlbumStorageDir("Pictures"),
            String.format(datetime + "signRightBitMap.jpg", System.currentTimeMillis()))
        saveBitmapToJPG(ugmr.rightImage!!, photo)
        scanMediaFile(photo)
        signRight = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)
        datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        photo = File(getAlbumStorageDir("Pictures"),
            String.format(datetime + "signFaceBitMap.jpg", System.currentTimeMillis()))
        saveBitmapToJPG(signFaceBitMap!!, photo)
        scanMediaFile(photo)
        signFace = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)
        datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        photo = File(getAlbumStorageDir("Pictures"),
            String.format(datetime + "signRoofBitMap.jpg", System.currentTimeMillis()))
        saveBitmapToJPG(ugmr.roofImage!!, photo)
        scanMediaFile(photo)
        signRoof = TypedFile(CONSTANTS.MULTIPART_FORMAT, photo)

        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            APIClientProfile.apiService!!.postUndergroundInsert(userId, ugDataModel.name,
                ugDataModel.comment, attributeDataModelList, ugDataModel.ugDate,
                ugDataModel.mapSerialNo, ugDataModel.shift, ugDataModel.mappedBy, ugDataModel.scale,
                ugDataModel.location, ugDataModel.venieLoad, ugDataModel.xCordinate,
                ugDataModel.yCordinate, ugDataModel.zCordinate, signRoof, signLeft, signRight,
                signFace, object : retrofit.Callback<SuccessModel> {
                    override fun success(model: SuccessModel, response: retrofit.client.Response) {
                        when (model.ResponseCode) {
                            ctx.getString(R.string.ResponseCodesuccess) -> {
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
                            ctx.getString(R.string.ResponseCodefail) -> {
                                showToast(model.ResponseCode, act)
                            }
                            ctx.getString(R.string.ResponseCodeDeleted) -> {
                                callDelete403(act, model.ResponseCode)
                            }
                        }
                    }

                    override fun failure(e: RetrofitError) {
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
            binding.drawing.startNew()
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

    private fun callDisable(flag: String) {
        if (flag == "1") {
            binding.drawing.startNew()
        }
        binding.drawing.background = getDrawable(R.drawable.grid_bg)
    }

    private fun callEnable(signBitMap: Bitmap, bitmapString: String) {
        if (binding.drawing.drawingCache != null) {
            binding.drawing.startNew()
        }
        binding.drawing.background = getDrawable(R.drawable.grid_bg)
        val d: Drawable = BitmapDrawable(resources!!, signBitMap)
        binding.drawing.background = d
        Log.e(bitmapString, "$i")
    }

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        ctx.sendBroadcast(mediaScanIntent)
    }

    private fun getAlbumStorageDir(albumName: String?): File {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            albumName)
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created")
        }
        return file
    }

    private fun saveImage(image: Bitmap) {
        signFaceBitMap = image
        ugmr.faceImage = signFaceBitMap
        postUndergroundInsert()
    }

    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
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
                    ctx,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    ctx,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act, permissionsStorage, requestExternalStorage)
            }
        }
    }

    private fun callFilesPermission() {
        val building = AlertDialog.Builder(ctx)
        building.setMessage("""To upload image allow ${
            ctx.getString(R.string.app_name)
        } access to your device's files. 
Tap Setting > permission, and turn "Files and media" on.""")
        building.setCancelable(true)
        building.setPositiveButton(
            ctx.getString(R.string.Settings)) { dialogs: DialogInterface, _: Int ->
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
            dialogs.dismiss()
        }
        building.setNegativeButton(ctx.getString(
            R.string.not_now)) { dialogs: DialogInterface, _: Int -> dialogs.dismiss() }
        val alert11 = building.create()
        alert11.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        alert11.show()
        alert11.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(
            ContextCompat.getColor(ctx, R.color.primary_theme))
        alert11.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(
            ContextCompat.getColor(ctx, R.color.primary_theme))
    }

    override fun onBackPressed() {
        if (binding.drawing.drawingCache != null) {
            signFaceBitMap = binding.drawing.drawingCache
            ugmr.faceImage = signFaceBitMap
        }
        finish()
    }

    fun paintClicked(view: View) {
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
