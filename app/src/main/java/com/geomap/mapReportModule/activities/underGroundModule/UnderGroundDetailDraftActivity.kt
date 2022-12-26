package com.geomap.mapReportModule.activities.underGroundModule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundDetailDraftBinding
import com.geomap.databinding.AttributeLayoutBinding
import com.geomap.mapReportModule.models.AttributeDataModel
import com.geomap.roomDataBase.UnderGroundMappingReport
import com.geomap.utils.CONSTANTS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UnderGroundDetailDraftActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundDetailDraftBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var attributesListAdapter : AttributesListAdapter? = null
    private var userId : String? = null
    private var report : String? = null
    private var ugReportData = UnderGroundMappingReport()
    private var attributeDataList = ArrayList<AttributeDataModel>()
    val gson = Gson()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_under_ground_detail_draft)
        ctx = this@UnderGroundDetailDraftActivity
        act = this@UnderGroundDetailDraftActivity
        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        binding.rvAttributesList.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvAttributesList.itemAnimator = DefaultItemAnimator()
        if (intent.extras != null) {
            report = intent.getStringExtra("report")
            val type1 = object : TypeToken<UnderGroundMappingReport>() {}.type
            ugReportData = gson.fromJson(report, type1)
            binding.ugDetail = ugReportData
            binding.llMainLayout.visibility = View.VISIBLE
            val type2 = object : TypeToken<java.util.ArrayList<AttributeDataModel>>() {}.type
            attributeDataList = gson.fromJson(ugReportData.attributes, type2)
            if (attributeDataList.size == 0) {
                binding.tvAttributes.visibility = View.GONE
                binding.rvAttributesList.visibility = View.GONE
            } else {
                binding.tvAttributes.visibility = View.VISIBLE
                binding.rvAttributesList.visibility = View.VISIBLE
                attributesListAdapter = AttributesListAdapter(attributeDataList)
                binding.rvAttributesList.adapter = attributesListAdapter
            }
            if (ugReportData.leftImage != null) {
                Glide.with(this).asBitmap().load(
                    ugReportData.leftImage
                ).into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                            resource : Bitmap,
                            transition : Transition<in Bitmap?>?
                    ) {
                        binding.leftImage.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder : Drawable?) {}
                })
            }
            if (ugReportData.rightImage != null) {
                Glide.with(this).asBitmap().load(
                    ugReportData.rightImage
                ).into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                            resource : Bitmap,
                            transition : Transition<in Bitmap?>?
                    ) {
                        binding.rightImage.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder : Drawable?) {}
                })
            }
            if (ugReportData.roofImage != null) {
                Glide.with(this).asBitmap().load(
                    ugReportData.roofImage
                ).into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                            resource : Bitmap,
                            transition : Transition<in Bitmap?>?
                    ) {
                        binding.roofImage.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder : Drawable?) {}
                })
            }
            if (ugReportData.faceImage != null) {
                Glide.with(this).asBitmap().load(
                    ugReportData.faceImage
                ).into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                            resource : Bitmap,
                            transition : Transition<in Bitmap?>?
                    ) {
                        binding.faceImage.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder : Drawable?) {}
                })
            }
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.llEdit.setOnClickListener {
            val i = Intent(act, UnderGroundFormFirstStepActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            i.putExtra("flag", "detailDraft")
            i.putExtra("data", gson.toJson(ugReportData))
            act.startActivity(i)
        }
    }

    inner class AttributesListAdapter(
            private val listModel : List<AttributeDataModel>
    ) : RecyclerView.Adapter<AttributesListAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : AttributeLayoutBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.attribute_layout,
                        parent,
                        false
                    )
            return MyViewHolder(v)
        }

        inner class MyViewHolder(var binding : AttributeLayoutBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
            holder.binding.tvName.text = listModel[position].name
            holder.binding.tvNos.text = listModel[position].nose
            holder.binding.tvProperties.text = listModel[position].properties

            if ((listModel.size - position) <= 1) {
                holder.binding.viewLine.visibility = View.GONE
            }
        }

        override fun getItemCount() : Int {
            return listModel.size
        }
    }

    override fun onBackPressed() {
        finish()
    }
}