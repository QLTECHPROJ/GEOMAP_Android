package com.geomap.mapReportModule.activities.underGroundModule

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.geomap.DataBaseFunctions.Companion.saveUGReport
import com.geomap.DataBaseFunctions.Companion.updateUGReport
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
import com.geomap.utils.*
import com.geomap.utils.Converter.convertedFormat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit.RetrofitError
import retrofit.mime.TypedFile
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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
    var imgTitle = ""
    private lateinit var currPaint : ImageButton
    var ugDataModel = UnderGroundInsertModel()
    var i = 0
    private val requestExternalStorage = 1
    private val permissionsStorage = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )
    companion object {
        var isSignRoofFilled = false
        var isSignLeftFilled = false
        var isSignRightFilled = false
        var isSignFaceFilled = false
        var isSignRoofEdited = false
        var isSignLeftEdited = false
        var isSignRightEdited = false
        var isSignFaceEdited = false
        var isSignRoofEditedCounter = 0
        var isSignLeftEditedCounter = 0
        var isSignRightEditedCounter = 0
        var isSignFaceEditedCounter = 0
        private var mRequestPermissionHandler: RequestPermissionHandler? = null
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
                DataBindingUtil.setContentView(this, R.layout.activity_under_ground_form_third_step)
        ctx = this@UnderGroundFormThirdStepActivity
        act = this@UnderGroundFormThirdStepActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")
        mRequestPermissionHandler = RequestPermissionHandler()
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
        imgTitle = " Image "+ ugDataModel.ugDate
        signRoofBitMap = ugmr.roofImage
        signLeftBitMap = ugmr.leftImage
        signRightBitMap = ugmr.rightImage
        signFaceBitMap = ugmr.faceImage
        binding.tvName.text = getString(R.string.roof)
        binding.tvImgTitle.text =  getString(R.string.roof) + imgTitle
        if (signRoofBitMap != null) {
//            signRoofBitMap =  saveBitmapToJPGImg(signRoofBitMap!!)
            if (binding.drawing.drawingCache != null) {
                binding.drawing.startNew()
                binding.drawing.background = getDrawable(R.drawable.grid_bg_new)
            }
            val d : Drawable = BitmapDrawable(resources!!, signRoofBitMap)
            binding.drawing.background = d
            binding.drawing.isFilled = true
            binding.drawing.isEdited = isSignRoofEdited
        } else {
            callDisable("0")
        }

        initView()
        val gson = Gson()
        Log.e("UGData", gson.toJson(ugDataModel).toString())
        binding.btnClear.isEnabled = true
        binding.btnClear.setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
        binding.btnClear.setBackgroundResource(R.drawable.border_enable_button)

        binding.btnClear.setOnClickListener {
           callDisable("1")
            when (i) {
                0 -> {
                    isSignRoofFilled = false
                    isSignRoofEdited = false
                    isSignRoofEditedCounter = 0
                }
                1 -> {
                    isSignLeftFilled = false
                    isSignLeftEdited = false
                    isSignLeftEditedCounter = 0
                }
                2 -> {
                    isSignRightFilled = false
                    isSignRightEdited = false
                    isSignRightEditedCounter = 0
                }
                3 -> {
                    isSignFaceFilled = false
                    isSignFaceEdited = false
                    isSignFaceEditedCounter = 0
                }
            }
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

    @SuppressLint("SetTextI18n")
    private fun saveImage(image : Bitmap) {
        when (i) {
            0 -> {
                binding.tvName.text = getString(R.string.left)
                signRoofBitMap = image.copy(image.config, false)
                binding.tvImgTitle.text =  getString(R.string.roof) + imgTitle
//                signRoofBitMap =  saveBitmapToJPGImg(signRoofBitMap!!)
                isSignRoofFilled = binding.drawing.isFilled
                if(binding.drawing.isEdited && isSignRoofEditedCounter != 0){
                    isSignRoofEditedCounter = 0
                }
                if(isSignRoofEditedCounter == 0) {
                    isSignRoofEdited = binding.drawing.isEdited
                    isSignRoofEditedCounter++
                }
                Log.e("before IF isSignRoofFilled", isSignRoofFilled.toString())
                Log.e("before IF isRoofEdited", isSignRoofEdited.toString())
                if (signLeftBitMap != null) {
//                    signLeftBitMap =  saveBitmapToJPGImg(signLeftBitMap!!)
                    callEnable(signLeftBitMap!!, "left")
                    binding.drawing.isFilled = true
                } else {
                    callDisable("1")
                }
                i++
            }
            1 -> {
                binding.tvName.text = getString(R.string.right)
                signLeftBitMap = image.copy(image.config, false)
                binding.tvImgTitle.text =  getString(R.string.left) + imgTitle
//                signLeftBitMap =  saveBitmapToJPGImg(signLeftBitMap!!)
                isSignLeftFilled = binding.drawing.isFilled
                if(binding.drawing.isEdited && isSignLeftEditedCounter != 0){
                    isSignLeftEditedCounter = 0
                }
                if(isSignLeftEditedCounter == 0) {
                    isSignLeftEdited = binding.drawing.isEdited
                    isSignLeftEditedCounter++
                }
                Log.e("before IF isSignLeftFilled", isSignLeftFilled.toString())
                Log.e("before IF isLeftEdited", isSignLeftEdited.toString())
                if (signRightBitMap != null) {
//                    signRightBitMap =  saveBitmapToJPGImg(signRightBitMap!!)
                    callEnable(signRightBitMap!!, "right")
                    binding.drawing.isFilled = true
                } else {
                    callDisable("1")
                }
                i++

            }
            2 -> {
                binding.btnNext.text = getString(R.string.submit)
                binding.tvName.text = getString(R.string.face)
                binding.tvImgTitle.text =  getString(R.string.right) + imgTitle
//                signRightBitMap =  saveBitmapToJPGImg(signRightBitMap!!)
                signRightBitMap = image.copy(image.config, false)
                isSignRightFilled = binding.drawing.isFilled
                if(binding.drawing.isEdited && isSignRightEditedCounter != 0){
                    isSignRightEditedCounter = 0
                }
                if(isSignRightEditedCounter == 0) {
                    isSignRightEdited = binding.drawing.isEdited
                    isSignRightEditedCounter++
                }
                Log.e("before IF isSignRightFilled", isSignRightFilled.toString())
                Log.e("before IF isRightEdited", isSignRightEdited.toString())
                if (signFaceBitMap != null) {
//                    signFaceBitMap =  saveBitmapToJPGImg(signFaceBitMap!!)
                    callEnable(signFaceBitMap!!, "face")
                    binding.drawing.isFilled = true
                    isSignFaceFilled = true
                } else {
                    callDisable("1")
                }
                i++
                binding.drawing.startNew()
            }
            3 -> {
                signFaceBitMap = image.copy(image.config, false)
                binding.tvImgTitle.text =  getString(R.string.face) + imgTitle
//                signFaceBitMap =  saveBitmapToJPGImg(signFaceBitMap!!)
                isSignFaceFilled = binding.drawing.isFilled
                if(binding.drawing.isEdited && isSignFaceEditedCounter != 0){
                    isSignFaceEditedCounter = 0
                }
                if(isSignFaceEditedCounter == 0) {
                    isSignFaceEdited = binding.drawing.isEdited
                    isSignFaceEditedCounter++
                }
                Log.e("before IF isFaceFilled", isSignFaceFilled.toString())
                Log.e("before IF isFaceEdited", isSignFaceEdited.toString())
                if (signFaceBitMap != null) {
                    callEnable(signFaceBitMap!!, "face")
                    binding.drawing.isFilled = true
                    if(isSignFaceEditedCounter != 0 && !isSignFaceEdited && flagUG != "0") {
                        isSignFaceFilled = true
                    }
                    Log.e("after IF isFaceFilled", isSignFaceFilled.toString())
                    Log.e("after IF isFaceEdited", isSignFaceEdited.toString())
                } else {
                    callDisable("1")
                }
                postUndergroundInsert()
            }
        }
    }

    private fun postUndergroundInsert() {
        if (isSignRoofEdited) {
            binding.tvImgTitle.text =  getString(R.string.roof) + imgTitle
            signRoofBitMap =  saveBitmapToJPGImg(signRoofBitMap!!)
            signRoof = TypedFile(CONSTANTS.MULTIPART_FORMAT, saveBitmapToJPG("RoofImage", signRoofBitMap!!))
        } else if (isSignRoofFilled) {
            signRoof = getImage("RoofImage", signRoofBitMap!!)
        }else {
            signRoof = getImage("RoofImage", signRoofBitMap!!)
        }

        if (isSignLeftEdited) {
            binding.tvImgTitle.text =  getString(R.string.left) + imgTitle
            signLeftBitMap =  saveBitmapToJPGImg(signLeftBitMap!!)
            signLeft = TypedFile(CONSTANTS.MULTIPART_FORMAT, saveBitmapToJPG("LeftImage", signLeftBitMap!!))
        } else if (isSignLeftFilled) {
            signLeft = getImage("LeftImage", signLeftBitMap!!)
        }else{
            signLeft = getImage("LeftImage", signLeftBitMap!!)
        }

        if (isSignRightEdited) {
            binding.tvImgTitle.text =  getString(R.string.right) + imgTitle
            signRightBitMap =  saveBitmapToJPGImg(signRightBitMap!!)
            signRight = TypedFile(CONSTANTS.MULTIPART_FORMAT, saveBitmapToJPG("RightImage", signRightBitMap!!))
        } else if (isSignRightFilled) {
            signRight = getImage("RightImage", signRightBitMap!!)
        }else{
            signRight = getImage("RightImage", signRightBitMap!!)
        }

        if (isSignFaceEdited) {
            binding.tvImgTitle.text =  getString(R.string.face) + imgTitle
            signFaceBitMap =  saveBitmapToJPGImg(signFaceBitMap!!)
            signFace = TypedFile(CONSTANTS.MULTIPART_FORMAT, saveBitmapToJPG("FaceImage", signFaceBitMap!!))
        } else if (isSignFaceFilled) {
            signFace = getImage("FaceImage", signFaceBitMap!!)
        }else{
            signFace = getImage("FaceImage", signFaceBitMap!!)
        }

        if (isNetworkConnected(ctx)) {
            if (flagUG == "0" || flagUG == "1") {
                showProgressBar(binding.progressBar, binding.progressBarHolder, act)
                APIClientProfile.apiService!!.postUndergroundInsert(userId, ugDataModel.name,
                    ugDataModel.comment, attributeDataModelList,
                    convertedFormat(ugDataModel.ugDate, CONSTANTS.SERVER_TIME_FORMAT,CONSTANTS.DATE_MONTH_YEAR_FORMAT_z),
                    ugDataModel.mapSerialNo, ugDataModel.shift, ugDataModel.mappedBy,
                    ugDataModel.scale, ugDataModel.location, ugDataModel.venieLoad,
                    ugDataModel.xCordinate, ugDataModel.yCordinate, ugDataModel.zCordinate,
                    signRoof, signLeft, signRight, signFace,
                    object : retrofit.Callback<SuccessModel> {
                        override fun success(
                                model : SuccessModel,
                                response : retrofit.client.Response
                        ) {
                            when (model.ResponseCode) {
                                getString(R.string.ResponseCodesuccess) -> {
                                    hideProgressBar(
                                        binding.progressBar, binding.progressBarHolder,
                                        act
                                    )
                                    showToast(model.ResponseMessage, act)
                                    isSignRoofFilled = false
                                    isSignLeftFilled = false
                                    isSignRightFilled = false
                                    isSignFaceFilled = false
                                    isSignRoofEdited = false
                                    isSignLeftEdited = false
                                    isSignRightEdited = false
                                    isSignFaceEdited = false
                                    isSignRoofEditedCounter = 0
                                    isSignLeftEditedCounter = 0
                                    isSignRightEditedCounter = 0
                                    isSignFaceEditedCounter = 0
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
                                getString(R.string.ResponseCodeDeleted) -> {
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
                callOfflineFunction()
            }
        } else {
            callOfflineFunction()
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

    private fun callOfflineFunction() {
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
        obj.uid = ugmr.uid
        if (flagUG == "2") {
            updateUGReport(obj, ctx)
            showToast(getString(R.string.underground_updated), act)
        } else {
            saveUGReport(obj, ctx)
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

    private fun callDisable(flag : String) {
        if (flag == "1") {
            binding.drawing.startNew()
        }
        binding.tvImgTitle.text =  binding.tvName.text.toString() + imgTitle
        var bitmap = BitmapFactory.decodeResource(resources!!, R.drawable.grid_bg_new)
        bitmap = saveBitmapToJPGImg(bitmap)
        val d : Drawable = BitmapDrawable(resources!!, bitmap)
        binding.drawing.background = d
    }

    private fun callEnable(signBitMap : Bitmap, bitmapString : String) {
        binding.drawing.startNew()
        binding.drawing.background = getDrawable(R.drawable.grid_bg_new)
        val d : Drawable = BitmapDrawable(resources!!, signBitMap)
        binding.drawing.background = d
        Log.e(bitmapString, "$i")
    }

    @Throws(IOException::class) fun saveBitmapToJPG(name : String, bitmap : Bitmap) : File {
        val datetime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), "Pictures"
        )
        val imageFile = File(
            file,
            String.format("$name$datetime.jpg", System.currentTimeMillis())
        )
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream : OutputStream = FileOutputStream(imageFile)
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
        val textLayout = StaticLayout("  "+ binding.tvImgTitle.text, paint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f,
            0.0f, false) // get height of multiline text
        // get height of multiline text
        val textHeight = textLayout.height // get position of text's top left corner
        // get position of text's top left corner
        val x: Float = ((bitmap.width - textWidth) / 30).toFloat()
        val y: Float = ((bitmap.height - textHeight) / 30).toFloat() // draw text to the Canvas center
        val p = Paint()
        p.color = getColor(R.color.primary_theme) // text size in pixels
        p.style = Paint.Style.FILL
        //        p.strokeWidth = 20.0f
        var rectF = RectF(0.0f, 2.0f, 1200.0f,120.0f)
   /*     if((ugDataModel.name == "" &&  ugDataModel.location != "") || (ugDataModel.name != "" && ugDataModel.location == "")){
            //3line
            Log.e("3line","3Line")
            rectF = RectF(1000.0f, 60.0f, 500.0f,220.0f)
        }else if(ugDataModel.name != "" &&  ugDataModel.location != ""){
            //4line
            Log.e("4line","4Line")
            rectF = RectF(1000.0f, 60.0f, 500.0f,280.0f)
        }else if(ugDataModel.name == "" && ugDataModel.location == ""){
            //2line
            Log.e("2line","2Line")
            rectF = RectF(1000.0f, 60.0f, 500.0f,100.0f)
        }*/
//        val rectF = RectF(textHeight.toFloat(), textWidth.toFloat(), textHeight.toFloat(),textWidth.toFloat())
        canvas.drawRect(rectF, p)
        // draw text to the Canvas center
        canvas.save()
        canvas.translate(x, y)
        textLayout.draw(canvas)
        canvas.restore()
        return bitmap
    }

    private fun initView() {
        val manageP = ContextCompat.checkSelfPermission(ctx,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        val readP = ContextCompat.checkSelfPermission(ctx,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionG = PackageManager.PERMISSION_GRANTED
        val permissionD = PackageManager.PERMISSION_DENIED
        val aManageP = Manifest.permission.MANAGE_EXTERNAL_STORAGE
        val aReadP = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (readP == permissionG) {
                callProfilePathSet()
            } else {
                callPermission(arrayOf(aReadP))
            }
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            if (manageP == permissionG && readP == permissionG) {
                callProfilePathSet()
            } else {
                if (readP != permissionG) {
                    callPermission(arrayOf(aReadP))
                } else if (readP == permissionD) {
                    callReadPermission("1", "Files and media")
                }else if (manageP == permissionD) {
                    callReadPermission("1", "Files and media")
                } else {
                    callPermission(arrayOf(aManageP,aReadP))
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val readPI = ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.READ_MEDIA_IMAGES)
            if (manageP == permissionG && readPI == permissionG) {
                callProfilePathSet()
            } else {
                if ( readPI != permissionG) {
                    callPermission(arrayOf(aReadP))
                }  else if (readPI == permissionD) {
                    callReadPermission("0", "Photos and videos")
                } else if (manageP == permissionD) {
                    callReadPermission("0", "Files and media")
                } else {
                    callPermission(arrayOf(aReadP))
                }
            }
        } else {
            callProfilePathSet()
        }

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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
        }*/
        currPaint = binding.paintColors.getChildAt(0) as ImageButton
        currPaint.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.paint_pressed))
    }

    private fun callReadPermission(version: String, text: String) {
        val buildable = AlertDialog.Builder(ctx)
        buildable.setPositiveButton(R.string.Settings) { dialogs: DialogInterface, _: Int ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
            dialogs.dismiss()
        }

        if (version == "0") {
            buildable.setMessage("To upload image allow ${
                getString(R.string.app_name)
            } access to your device's files. Tap Setting > permission, and turn $text on.")
        } else if (version == "1") {
            buildable.setMessage("To upload image allow ${
                getString(R.string.app_name)
            } access to your device's files. Tap Setting > permission, and turn $text on.")
        }

        buildable.setNegativeButton(ctx.getString(
            R.string.not_now)) { dialogue: DialogInterface, _: Int -> dialogue.dismiss() }
        buildable.setCancelable(true)
        val alert11 = buildable.create()
        alert11.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        alert11.show()
        alert11.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
        alert11.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
    }
    private fun callProfilePathSet() {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun callPermission(arry: Array<String>) {

        mRequestPermissionHandler!!.requestPermission(act, arry, 123,
            object : RequestPermissionHandler.RequestPermissionListener {
                override fun onSuccess() {
                    callProfilePathSet()
                }

                override fun onFailed() {}
            })
    }
    @SuppressLint("SetTextI18n")
    override fun onBackPressed() {
        binding.drawing.isDrawingCacheEnabled = true
        when (i) {
            0 -> {
                signRoofBitMap =
                        binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
                binding.tvImgTitle.text =  getString(R.string.roof) + imgTitle
//                signRoofBitMap = saveBitmapToJPGImg(signRoofBitMap!!)
                ugmr.roofImage = signRoofBitMap
                if(binding.drawing.isEdited && isSignRoofEditedCounter != 0){
                    isSignRoofEditedCounter = 0
                }
                if(isSignRoofEditedCounter == 0) {
                    isSignRoofEdited = binding.drawing.isEdited
                    isSignRoofEditedCounter++
                }
                finish()
            }
            1 -> {
                signLeftBitMap =
                        binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
                binding.tvImgTitle.text =  getString(R.string.left) + imgTitle
//                signLeftBitMap = saveBitmapToJPGImg(signLeftBitMap!!)
                ugmr.leftImage = signLeftBitMap
                if(binding.drawing.isEdited && isSignLeftEditedCounter != 0){
                    isSignLeftEditedCounter = 0
                }
                if(isSignLeftEditedCounter == 0) {
                    isSignLeftEdited = binding.drawing.isEdited
                    isSignLeftEditedCounter++
                }
                callEnable(signRoofBitMap!!, "roof")
//                if(isSignRoofEditedCounter != 0){
//                    isSignRoofEditedCounter = 0
//                }
                binding.btnNext.text = getString(R.string.next)
                binding.tvName.text = getString(R.string.roof)
                i--
            }
            2 -> {
                signRightBitMap =
                        binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
                binding.tvImgTitle.text =  getString(R.string.right) + imgTitle
//                signRightBitMap = saveBitmapToJPGImg(signRightBitMap!!)
                ugmr.rightImage = signRightBitMap
                if(binding.drawing.isEdited && isSignRightEditedCounter != 0){
                    isSignRightEditedCounter = 0
                }
                if(isSignRightEditedCounter == 0) {
                    isSignRightEdited = binding.drawing.isEdited
                    isSignRightEditedCounter++
                }
                callEnable(signLeftBitMap!!, "left")
//                if(isSignLeftEditedCounter != 0){
//                    isSignLeftEditedCounter = 0
//                }
                binding.btnNext.text = getString(R.string.next)
                binding.tvName.text = getString(R.string.left)
                i--
            }
            3 -> {
                signFaceBitMap =
                        binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
                binding.tvImgTitle.text =  getString(R.string.face) + imgTitle
//                signFaceBitMap = saveBitmapToJPGImg(signFaceBitMap!!)
                ugmr.faceImage = signFaceBitMap
                if(binding.drawing.isEdited && isSignFaceEditedCounter != 0){
                    isSignFaceEditedCounter = 0
                }
                if(isSignFaceEditedCounter == 0) {
                    isSignFaceEdited = binding.drawing.isEdited
                    isSignFaceEditedCounter++
                }
//                if(isSignRightEditedCounter != 0){
//                    isSignRightEditedCounter = 0
//                }
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
