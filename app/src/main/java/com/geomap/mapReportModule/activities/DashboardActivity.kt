package com.geomap.mapReportModule.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityDashboardBinding
import com.geomap.databinding.MappingReportListLayoutBinding
import com.geomap.mapReportModule.models.DummyModel

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDashboardBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var underGroundListAdapter : UnderGroundListAdapter? = null
    private var openCastListAdapter : OpenCastListAdapter? = null
    private var listModel : Array<DummyModel> = arrayOf(
        DummyModel("My Report Name 1"),
        DummyModel("My Report Name 2"), DummyModel("My Report Name 3"),
        DummyModel("My Report Name 4")
    )

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        ctx = this@DashboardActivity
        act = this@DashboardActivity

        binding.llMenu.setOnClickListener {
            callMenuListActivity(act, "1")
        }

        binding.tvAddReport.setOnClickListener {
            callAddReportActivity(act, "1")
        }

        binding.tvUnderGroundListViewAll.setOnClickListener {
            callUnderGroundListActivity(act, "1")
        }

        binding.tvOpenCastListViewAll.setOnClickListener {
            callOpenCastListActivity(act, "1")
        }

        underGroundListAdapter = UnderGroundListAdapter(listModel)
        val mLayoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvUnderGroundList.layoutManager = mLayoutManager
        binding.rvUnderGroundList.itemAnimator = DefaultItemAnimator()
        binding.rvUnderGroundList.adapter = underGroundListAdapter

        openCastListAdapter = OpenCastListAdapter(listModel)
        val mLayoutManage : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvOpenCastList.layoutManager = mLayoutManage
        binding.rvOpenCastList.itemAnimator = DefaultItemAnimator()
        binding.rvOpenCastList.adapter = openCastListAdapter
    }

    inner class UnderGroundListAdapter(
        private val listModel : Array<DummyModel>
    ) : RecyclerView.Adapter<UnderGroundListAdapter.MyViewHolder>() {

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
            holder.binding.tvName.text = listModel[position].title

            holder.binding.llMainLayout.setOnClickListener {
                callUnderGroundDetailActivity(act, "1")

            }
        }

        override fun getItemCount() : Int {
            return 2
        }
    }

    inner class OpenCastListAdapter(
        private val listModel : Array<DummyModel>
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
            holder.binding.tvName.text = listModel[position].title
            holder.binding.llMainLayout.setOnClickListener {
                callOpenCastDetailActivity(act, "1")

            }
        }

        override fun getItemCount() : Int {
            return 2
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}