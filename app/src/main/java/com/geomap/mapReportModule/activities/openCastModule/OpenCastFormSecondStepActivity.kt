package com.geomap.mapReportModule.activities.openCastModule

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.geomap.DataBaseFunctions.Companion.saveOCReport
import com.geomap.DataBaseFunctions.Companion.updateOCReport
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastFormSecondStepBinding
import com.geomap.mapReportModule.activities.DashboardActivity
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.clientsGeoSign
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.clientsGeologistSignEdited
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.flagOC
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.geoSign
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.geologistSignEdited
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.img
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.ocmr
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.rockStrength
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.sampleCollected
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.typeOfFaults
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.typeOfGeologicalStructures
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.waterCondition
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity.Companion.weathering
import com.geomap.mapReportModule.models.OpenCastInsertModel
import com.geomap.mapReportModule.models.SuccessModel
import com.geomap.roomDataBase.OpenCastMappingReport
import com.geomap.utils.APIClientProfile
import com.geomap.utils.CONSTANTS
import com.geomap.utils.Converter.convertedFormat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit.RetrofitError
import retrofit.mime.TypedFile
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class OpenCastFormSecondStepActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOpenCastFormSecondStepBinding
    private lateinit var ctx: Context
    private lateinit var act: Activity
    private var ocDataModel = OpenCastInsertModel()
    private var userId: String? = null
    private var sign: TypedFile? = null
    private var isImageFilled = false
    private var isImageEdited = false
    var titleImg = ""
    private val requestExternalStorage = 1
    private val permissionsStorage = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
    private lateinit var currPaint: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
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

        titleImg = "  "+ ocDataModel.ocDate + "_OC_Image"
        binding.tvImgTitle.text = titleImg
        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        initViewSandVars()

//        if (flagOC == "1" || flagOC == "2") {
        if (img != null) {
//            img = saveBitmapToJPGImg(img!!)
            val d: Drawable = BitmapDrawable(resources, img)
            binding.drawing.background = d
            binding.drawing.isFilled = true
        }else{
            var bitmap = BitmapFactory.decodeResource(resources!!, R.drawable.grid_bg_new)
            bitmap = saveBitmapToJPGImg(bitmap)
            val d : Drawable = BitmapDrawable(resources!!, bitmap)
            binding.drawing.background = d
        }

        binding.btnSignPadClear.setOnClickListener {
            binding.drawing.background = getDrawable(R.drawable.grid_bg_new)
            binding.drawing.startNew()
            var bitmap = BitmapFactory.decodeResource(resources!!, R.drawable.grid_bg_new)
            bitmap = saveBitmapToJPGImg(bitmap)
            val d : Drawable = BitmapDrawable(resources!!, bitmap)
            binding.drawing.background = d
        }

        binding.btnSubmit.setOnClickListener {
            verifyStoragePermissions()
            postData()
        }
    }

    private fun initViewSandVars() {
        verifyStoragePermissions()
        currPaint = binding.paintColors.getChildAt(0) as ImageButton
        currPaint.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.paint_pressed))
    }

    private fun postData() {
        postOpenCastInsert()
    }

    private fun postOpenCastInsert() {
        isImageFilled = binding.drawing.isFilled
        isImageEdited = binding.drawing.isEdited
        binding.drawing.isDrawingCacheEnabled = true

        geologistSign = if (ocDataModel.geologistSignBitMap != null && geologistSignEdited) {
            TypedFile(CONSTANTS.MULTIPART_FORMAT,
                saveBitmapToJPG("geologistSign", ocDataModel.geologistSignBitMap!!))
        } else if (ocDataModel.geologistSignBitMap != null && geoSign) {
            val file = File(cacheDir, "geologistSign.jpg")
            val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
            ocDataModel.geologistSignBitMap!!.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.close()
            TypedFile(CONSTANTS.MULTIPART_FORMAT, file)
        } else {
            null
        }

        clientsGeologistSign = if (ocDataModel.clientsGeologistSignBitMap != null && clientsGeologistSignEdited) {
            TypedFile(CONSTANTS.MULTIPART_FORMAT,
                saveBitmapToJPG("clientsGeologistSign", ocDataModel.clientsGeologistSignBitMap!!))
        } else if (ocDataModel.clientsGeologistSignBitMap != null && clientsGeoSign) {
            val file = File(cacheDir, "clientsGeologistSign.jpg")
            val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
            ocDataModel.clientsGeologistSignBitMap!!.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.close()
            TypedFile(CONSTANTS.MULTIPART_FORMAT, file)
        } else {
            null
        }

        if (isImageEdited) {
            val bitmap = binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config,
                false)
            sign = TypedFile(CONSTANTS.MULTIPART_FORMAT, saveBitmapToJPG("Image", bitmap))
        } else if (isImageFilled) {
            img = binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
            val file = File(cacheDir, "image.jpg")
            val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
            img!!.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.close()
            sign = TypedFile(CONSTANTS.MULTIPART_FORMAT, file)
            binding.drawing.isFilled = true
        }else{
            img = binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
            val file = File(cacheDir, "image.jpg")
            val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
            img!!.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.close()
            sign = TypedFile(CONSTANTS.MULTIPART_FORMAT, file)
            binding.drawing.isFilled = true
        }

        if (isNetworkConnected(ctx)) {
            if (flagOC == "0" || flagOC == "1") {
                showProgressBar(binding.progressBar, binding.progressBarHolder, act)
                APIClientProfile.apiService!!.postOpenCastInsert(userId, ocDataModel.minesSiteName,
                    ocDataModel.sheetNo, ocDataModel.pitName, ocDataModel.pitLocation,
                    ocDataModel.shiftInchargeName, ocDataModel.geologistName,
                    ocDataModel.faceLocation, ocDataModel.faceLengthM, ocDataModel.faceAreaM2,
                    ocDataModel.faceRockTypes, ocDataModel.benchRL, ocDataModel.benchHeightWidth,
                    ocDataModel.benchAngle, ocDataModel.thicknessOfOre,
                    ocDataModel.thinessOfOverburden, ocDataModel.thicknessOfInterburden,
                    ocDataModel.observedGradeOfOre, ocDataModel.actualGradeOfOre,
                    ocDataModel.sampleCollected, ocDataModel.weathering, ocDataModel.rockStregth,
                    ocDataModel.waterCondition, ocDataModel.typeOfGeologicalStructures,
                    ocDataModel.typeOfFaults, ocDataModel.notes, ocDataModel.shift,
                    convertedFormat(ocDataModel.ocDate,CONSTANTS.SERVER_TIME_FORMAT,CONSTANTS.DATE_MONTH_YEAR_FORMAT_z),
                    ocDataModel.dipDirectionAngle, sign, geologistSign,
                    clientsGeologistSign, object : retrofit.Callback<SuccessModel> {
                        override fun success(model: SuccessModel,
                            response: retrofit.client.Response) {
                            if (model.ResponseCode == getString(R.string.ResponseCodesuccess)) {
                                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                                when (model.ResponseCode) {
                                    getString(R.string.ResponseCodesuccess) -> {
                                        showToast(model.ResponseMessage, act)
                                        clearData()
                                        val i = Intent(ctx, DashboardActivity::class.java)
                                        i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        startActivity(i)
                                        finishAffinity()
                                    }
                                    getString(R.string.ResponseCodefail) -> {
                                        showToast(model.ResponseMessage, act)
                                    }
                                    getString(R.string.ResponseCodeDeleted) -> {
                                        callDelete403(act, model.ResponseMessage)
                                    }
                                }
                            }
                        }



                        override fun failure(e: RetrofitError) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            showToast(e.message, act)
                        }
                    })
            } else {
                callOfflineFunction()
            }
        } else {
            callOfflineFunction()
        }
    }
    private fun clearData() {
        flagOC = "0"
        ocmr = OpenCastMappingReport()
        img = null
        geologistSign = null
        clientsGeologistSign = null
        ocDataModel = OpenCastInsertModel()
        sampleCollected = ""
        weathering = ""
        rockStrength = ""
        typeOfFaults = ""
        waterCondition = ""
        typeOfGeologicalStructures = ""
        geoSign = false
        clientsGeoSign = false
        geologistSignEdited = false
        clientsGeologistSignEdited = false
    }
    private fun callOfflineFunction() {
        Log.e("obj.uid", "else true")
        val obj = OpenCastMappingReport()
        obj.userId = userId
        obj.ocDate = ocDataModel.ocDate
        obj.mappingSheetNo = ""
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
        if (ocDataModel.geologistSignBitMap != null) {
            obj.geologistSign = ocDataModel.geologistSignBitMap
        } else {
            obj.geologistSign = null
        }
        if (ocDataModel.clientsGeologistSignBitMap != null) {
            obj.clientsGeologistSign = ocDataModel.clientsGeologistSignBitMap
        } else {
            obj.clientsGeologistSign = null
        }
        obj.image = binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
        obj.uid = ocmr.uid
        Log.e("obj.uid", obj.uid.toString())
        if (flagOC == "2") {
            updateOCReport(obj, ctx)
            showToast(getString(R.string.opencast_updated), act)
        } else {
            saveOCReport(obj, ctx)
            showToast(getString(R.string.opencast_saved), act)
        }
        clearData()
        val i = Intent(ctx, DashboardActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(i)
        finishAffinity()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestExternalStorage -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.e("onRequestPermissionsResult", "Cannot write images to external storage")
                }
            }
        }
    }

    @Throws(IOException::class)
    fun saveBitmapToJPG(name: String, bitmap: Bitmap): File {
        val datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "Pictures")
        val imageFile = File(file, String.format("$name$datetime.jpg", System.currentTimeMillis()))
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(imageFile)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(imageFile)
        mediaScanIntent.data = contentUri
        sendBroadcast(mediaScanIntent)

        return imageFile
    }

    @Throws(IOException::class)
    fun saveBitmapToJPGImg(bitmap: Bitmap): Bitmap{
        var bitmap = bitmap
        val scale: Float = resources.displayMetrics.density
        var bitmapConfig: Bitmap.Config = bitmap.config // set default bitmap config if none
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        } // resource bitmaps are immutable, so we need to convert it to mutable one
        // resource bitmaps are immutable, so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true)
        val canvas = Canvas(bitmap) // new antialiased Paint
        // new antialiased Paint
        val paint = TextPaint(Paint.ANTI_ALIAS_FLAG) // text color - #3D3D3D
        // text color - #3D3D3D
        paint.color = getColor(R.color.white) // text size in pixels
        // text size in pixels
        paint.textSize = (binding.tvImgTitle.textSize) // text shadow
        paint.textAlign = Paint.Align.LEFT

        // text shadow
//        paint.setShadowLayer(2f, 0f, 1f, getColor(R.color.primary_theme)) // set text width to canvas width minus 16dp padding
        // set text width to canvas width minus 16dp padding
        val textWidth = canvas.width - (16 * scale.roundToInt())// init StaticLayout for text
        // init StaticLayout for text
        val textLayout = StaticLayout(binding.tvImgTitle.text, paint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f,
            0.0f, false) // get height of multiline text
        // get height of multiline text
        val textHeight = textLayout.height // get position of text's top left corner
        // get position of text's top left corner
        val x: Float = ((bitmap.width - textWidth) / 30).toFloat()
        val y: Float = ((bitmap.height - textHeight) / 30).toFloat() // draw text to the Canvas center
        val p = Paint()
        p.color = getColor(R.color.primary_theme) // text size in pixels
        p.style = Paint.Style.FILL
        var rectF = RectF(0.0f, 2.0f, 1200.0f,120.0f)
        /*if((ocDataModel.minesSiteName != "" && ocDataModel.pitName == "" &&  ocDataModel.pitLocation == "") || (ocDataModel.minesSiteName == "" && ocDataModel.pitName != "" &&  ocDataModel.pitLocation == "") || (ocDataModel.minesSiteName == "" && ocDataModel.pitName == "" &&  ocDataModel.pitLocation != "") ){
            //3line
            Log.e("4line","4Line")
            rectF = RectF(1000.0f, 60.0f, 500.0f,220.0f)
        }else  if((ocDataModel.minesSiteName == "" && ocDataModel.pitName != "" &&  ocDataModel.pitLocation != "") || (ocDataModel.minesSiteName != "" && ocDataModel.pitName == "" &&  ocDataModel.pitLocation != "")|| (ocDataModel.minesSiteName != "" && ocDataModel.pitName != "" &&  ocDataModel.pitLocation == "")){
            //4line
            Log.e("3line","3Line")
            rectF = RectF(1000.0f, 60.0f, 500.0f,280.0f)
        }else if(ocDataModel.minesSiteName != "" && ocDataModel.pitName != "" &&  ocDataModel.pitLocation != ""){
            //5line
            Log.e("4line","4Line")
            rectF = RectF(1000.0f, 60.0f, 500.0f,310.0f)
        }else if(ocDataModel.minesSiteName == "" && ocDataModel.pitName == "" && ocDataModel.pitLocation == ""){
     */       //2line
//        }
        //        val rectF = RectF(textHeight.toFloat(), textWidth.toFloat(), textHeight.toFloat(),textWidth.toFloat())
        canvas.drawRect(rectF, p)
        // draw text to the Canvas center
        canvas.save()
        canvas.translate(x, y)
        textLayout.draw(canvas)
        canvas.restore()
        return bitmap
    }

    private fun verifyStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
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

    override fun onBackPressed() {
        isImageFilled = binding.drawing.isFilled
        isImageEdited = binding.drawing.isEdited
        binding.drawing.isDrawingCacheEnabled = true
        img = binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config,
                false)
//        img =  saveBitmapToJPGImg(img)
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