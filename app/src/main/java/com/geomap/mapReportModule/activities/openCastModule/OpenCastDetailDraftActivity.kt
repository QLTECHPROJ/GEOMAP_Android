package com.geomap.mapReportModule.activities.openCastModule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastDetailDraftBinding
import com.geomap.roomDataBase.OpenCastMappingReport
import com.geomap.utils.CONSTANTS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OpenCastDetailDraftActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastDetailDraftBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var report : String? = null
    var ocReportData = OpenCastMappingReport()
    val gson = Gson()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_detail_draft)
        ctx = this@OpenCastDetailDraftActivity
        act = this@OpenCastDetailDraftActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            report = intent.getStringExtra("report")
            val type1 = object : TypeToken<OpenCastMappingReport>() {}.type
            ocReportData = gson.fromJson(report, type1)
            binding.ocDetail = ocReportData
            binding.llMainLayout.visibility = View.VISIBLE

            if (ocReportData.geologistSign != null) {
                Glide.with(this).asBitmap().load(
                    ocReportData.geologistSign).into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap,
                        transition: Transition<in Bitmap?>?) {
                        binding.imgGeologistSign.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}

                })
            }

            if (ocReportData.clientsGeologistSign != null) {
                Glide.with(this).asBitmap().load(
                    ocReportData.clientsGeologistSign).into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap,
                        transition: Transition<in Bitmap?>?) {
                        binding.imgClientGeologistSign.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}

                })
            }

            if (ocReportData.image != null) {
                Glide.with(this).asBitmap().load(
                    ocReportData.image).into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap,
                        transition: Transition<in Bitmap?>?) {
                        binding.image.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}

                })
            }
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
        binding.llEdit.setOnClickListener {
            val i = Intent(act, OpenCastFormFirstStepActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            i.putExtra("flag","detailDraft")
            i.putExtra("data",gson.toJson(ocReportData))
            act.startActivity(i)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}