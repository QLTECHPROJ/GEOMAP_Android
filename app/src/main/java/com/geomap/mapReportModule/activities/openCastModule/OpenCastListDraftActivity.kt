package com.geomap.mapReportModule.activities.openCastModule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geomap.DataBaseFunctions
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastDraftListBinding
import com.geomap.databinding.ActivityOpenCastListBinding
import com.geomap.databinding.MappingReportListLayoutBinding
import com.geomap.roomDataBase.GeoMapDatabase
import com.geomap.roomDataBase.OpenCastMappingReport
import com.geomap.roomDataBase.UnderGroundMappingReport
import com.geomap.utils.CONSTANTS
import com.google.gson.Gson

class OpenCastListDraftActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastDraftListBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var gson = Gson()
    private var openCastListAdapter : OpenCastListAdapter? = null
    var list =  java.util.ArrayList<OpenCastMappingReport>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_draft_list)
        ctx = this@OpenCastListDraftActivity
        act = this@OpenCastListDraftActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        val mLayoutManage : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvOpenCastList.layoutManager = mLayoutManage
        binding.rvOpenCastList.itemAnimator = DefaultItemAnimator()
        DB = getDataBase(ctx)
        postData()
    }

    private fun postData() {

        DB = getDataBase(ctx)
        DB.taskDao().geAllOpenCastMappingReport1().observe(ctx as LifecycleOwner){lists ->
            list = lists as java.util.ArrayList<OpenCastMappingReport>
            callAdapter(list)
        }
    }

    private fun callAdapter(list: java.util.ArrayList<OpenCastMappingReport>) {
        if (list.isEmpty()) {
            binding.rvOpenCastList.visibility = View.GONE
            binding.tvFound.visibility = View.VISIBLE
        } else {
            binding.rvOpenCastList.visibility = View.VISIBLE
            binding.tvFound.visibility = View.GONE

            openCastListAdapter = OpenCastListAdapter(list)
            binding.rvOpenCastList.adapter = openCastListAdapter
            Log.e("List OpenCastMappingReport",
                "true" + DataBaseFunctions.gson.toJson(list).toString())
        }
    }

    inner class OpenCastListAdapter(
        private val listModel : ArrayList<OpenCastMappingReport>
    ) : RecyclerView.Adapter<OpenCastListAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : MappingReportListLayoutBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.mapping_report_list_layout,
                    parent,
                    false
                )
            return MyViewHolder(v)
        }

        inner class MyViewHolder(var binding : MappingReportListLayoutBinding) :
            RecyclerView.ViewHolder(binding.root)

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
            holder.binding.tvName.text = listModel[position].pitName
            holder.binding.tvArea.text = listModel[position].pitLocation
            holder.binding.tvDate.text = listModel[position].ocDate
            holder.binding.tvSubTitleOne.setText(
                Html.fromHtml(
                    "Mines site name : <font color='black'>${listModel[position].minesSiteName}</font>"),
                TextView.BufferType.SPANNABLE)
            holder.binding.tvSubTitleTwo.setText(Html.fromHtml(
                "Mapping sheet no : <font color='black'>${listModel[position].mappingSheetNo}</font>"),
                TextView.BufferType.SPANNABLE)
            holder.binding.llMainLayout.setOnClickListener {
                callOpenCastDetailDraftActivity(act, "1", gson.toJson(listModel[position]).toString())
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