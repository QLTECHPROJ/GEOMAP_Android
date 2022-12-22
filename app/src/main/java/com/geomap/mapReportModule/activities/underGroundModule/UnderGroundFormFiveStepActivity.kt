package com.geomap.mapReportModule.activities.underGroundModule

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormThirdStepBinding
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.attributeDataModelList
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.flagUG
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.ugmr
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity.Companion.signLeftBitMap
import com.geomap.mapReportModule.models.AttributeDataModel
import com.geomap.mapReportModule.models.UnderGroundInsertModel
import com.geomap.utils.CONSTANTS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class UnderGroundFormFiveStepActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnderGroundFormThirdStepBinding
    private lateinit var ctx: Context
    private lateinit var act: Activity
    private var userId: String? = null
    private lateinit var currPaint: ImageButton
    var ugDataModel = UnderGroundInsertModel()
    var i = 0
    val gson = Gson()
    private val requestExternalStorage = 1
    private val permissionsStorage = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_under_ground_form_third_step)
        ctx = this@UnderGroundFormFiveStepActivity
        act = this@UnderGroundFormFiveStepActivity
        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            val gson = Gson()
            val data = intent.getStringExtra("ugData")
            val type1 = object : TypeToken<UnderGroundInsertModel>() {}.type
            ugDataModel = gson.fromJson(data, type1)
            val data1 = intent.getStringExtra("attributeData")
            val type2 = object : TypeToken<ArrayList<AttributeDataModel>>() {}.type
            attributeDataModelList = gson.fromJson(data1, type2)
            intent.extras!!.clear()
        }
//        if (flagUG == "1" || flagUG == "2") {
            signLeftBitMap = ugmr.leftImage
            if (signLeftBitMap != null) {
                callEnable(signLeftBitMap!!, "left")
            } else {
                callDisable("0")
            }
//        }else{
//            if (ugmr.leftImage != null) {
//                callEnable(ugmr.leftImage!!, "left")
//            } else {
//                callDisable("0")
//            }
//        }

        initViewSandVars()
        binding.drawing.isDrawingCacheEnabled = true
        val gson = Gson()
        Log.e("UGData", gson.toJson(ugDataModel).toString())
        binding.btnClear.isEnabled = true
        binding.btnClear.setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
        binding.btnClear.setBackgroundResource(R.drawable.border_enable_button)
        binding.tvName.text = getString(R.string.left)
        binding.btnNext.text = getString(R.string.next)

        binding.btnClear.setOnClickListener {
            if (binding.drawing.drawingCache != null) {
                binding.drawing.isDrawingCacheEnabled = false
                binding.drawing.startNew()
                binding.drawing.isDrawingCacheEnabled = true
                binding.drawing.background = getDrawable(R.drawable.grid_bg)
            }
        }


        binding.btnNext.setOnClickListener {
            saveImage(binding.drawing.drawingCache)
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

    private fun saveImage(image: Bitmap) {
        signLeftBitMap = image
        ugmr.leftImage = signLeftBitMap
        val i = Intent(act, UnderGroundFormSixStepActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        i.putExtra("ugData", gson.toJson(ugDataModel))
        i.putExtra("attributeData", gson.toJson(attributeDataModelList).toString())
        act.startActivity(i)
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
        binding.drawing.isDrawingCacheEnabled = true
        if (binding.drawing.drawingCache != null) {
            signLeftBitMap = binding.drawing.drawingCache.copy(binding.drawing.drawingCache.config, false)
            ugmr.leftImage = signLeftBitMap
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
