package com.geomap.mapReportModule.activities.underGroundModule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundDetailBinding
import com.geomap.databinding.AttributeLayoutBinding
import com.geomap.mapReportModule.models.AttributeDataModel
import com.geomap.mapReportModule.models.UnderGroundDetailsModel
import com.geomap.roomDataBase.OpenCastMappingReport
import com.geomap.roomDataBase.UnderGroundMappingReport
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnderGroundDetailDraftActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnderGroundDetailBinding
    private lateinit var ctx: Context
    private lateinit var act: Activity
    private var attributesListAdapter: AttributesListAdapter? = null
    private var userId: String? = null
    private var report: String? = null
    var ugReportData = UnderGroundMappingReport()
    var attributeDataList = ArrayList<AttributeDataModel>()
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_under_ground_detail)
        ctx = this@UnderGroundDetailDraftActivity
        act = this@UnderGroundDetailDraftActivity
        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            report = intent.getStringExtra("report")
            val type1 = object : TypeToken<UnderGroundMappingReport>() {}.type
            ugReportData = gson.fromJson(report, type1)
            postData()
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvAttributesList.layoutManager = mLayoutManager
        binding.rvAttributesList.itemAnimator = DefaultItemAnimator()


        binding.btnViewPdf.setOnClickListener {
            callViewPdfActivity(act, "1")
        }
    }

    private fun postData() {
        binding.llMainLayout.visibility = View.VISIBLE
        binding.btnViewPdf.visibility = View.VISIBLE
        binding.tvSerialNo.text = ugReportData.mapSerialNo
        binding.tvDate.text = ugReportData.ugDate
        binding.tvShift.text = ugReportData.shift
        binding.tvMappedBy.text = ugReportData.mappedBy
        binding.tvScale.text = ugReportData.scale
        binding.tvLocation.text = ugReportData.locations
        binding.tvLoadName.text = ugReportData.veinOrLoad
        binding.tvXCoordinate.text = ugReportData.xCordinate
        binding.tvYCoordinate.text = ugReportData.yCordinate
        binding.tvZCoordinate.text = ugReportData.zCordinate
        val type1 = object : TypeToken<UnderGroundMappingReport>() {}.type
        attributeDataList = gson.fromJson(ugReportData.attributes, type1)
        if (attributeDataList.size == 0) {
            binding.tvAttributes.visibility = View.GONE
            binding.rvAttributesList.visibility = View.GONE
        } else {
            binding.tvAttributes.visibility = View.VISIBLE
            binding.rvAttributesList.visibility = View.VISIBLE
            attributesListAdapter = AttributesListAdapter(attributeDataList)
            binding.rvAttributesList.adapter = attributesListAdapter
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

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
            holder.binding.tvName.text = listModel[position].name
            holder.binding.tvNos.text = listModel[position].nose
            holder.binding.tvProperties.text = listModel[position].properties
        }

        override fun getItemCount() : Int {
            return listModel.size
        }
    }

    override fun onBackPressed() {
        finish()
    }
}