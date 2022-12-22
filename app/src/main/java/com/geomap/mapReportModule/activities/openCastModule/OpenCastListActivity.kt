package com.geomap.mapReportModule.activities.openCastModule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityOpenCastListBinding
import com.geomap.databinding.MappingReportListLayoutBinding
import com.geomap.mapReportModule.models.DashboardViewAllModel
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.utils.CONSTANTS
import com.geomap.utils.Converter
import com.geomap.utils.RetrofitService

class OpenCastListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastListBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private lateinit var viewModel : AllViewModel
    private val retrofitService = RetrofitService.getInstance()
    private var openCastListAdapter : OpenCastListAdapter? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_list)
        ctx = this@OpenCastListActivity
        act = this@OpenCastListActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.rvOpenCastList.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvOpenCastList.itemAnimator = DefaultItemAnimator()
        postData()
    }

    fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            viewModel = ViewModelProvider(this, UserModelFactory(
                UserRepository(retrofitService)))[AllViewModel::class.java]
            viewModel.getORViewAllListing(userId.toString())
            viewModel.getORViewAllListing.observe(this) { it ->
                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                when {
                    it?.responseCode == getString(R.string.ResponseCodesuccess) -> {
                        if (it.responseData!!.isEmpty()) {
                            binding.rvOpenCastList.visibility = View.GONE
                            binding.tvFound.visibility = View.VISIBLE
                        } else {
                            binding.rvOpenCastList.visibility = View.VISIBLE
                            binding.tvFound.visibility = View.GONE

                        }
                        openCastListAdapter =
                            OpenCastListAdapter(it.responseData!!)
                        binding.rvOpenCastList.adapter = openCastListAdapter
                    }
                    it.responseCode == act.getString(R.string.ResponseCodefail) -> {
                        binding.rvOpenCastList.visibility = View.GONE
                        binding.tvFound.visibility = View.VISIBLE
                        showToast(it.responseMessage, act)
                    }
                    it.responseCode == act.getString(R.string.ResponseCodeDeleted) -> {
                        callDelete403(act, it.responseMessage)
                    }
                }
            }
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    inner class OpenCastListAdapter(
        private val listModel : List<DashboardViewAllModel.ResponseData>
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
            holder.binding.tvName.text = Converter.format(listModel[position].pitName)
            holder.binding.tvArea.text = Converter.format(listModel[position].pitLoaction)
            holder.binding.tvDate.text = Converter.format(listModel[position].ocDate)
            holder.binding.tvSubTitleOne.setText(
                Html.fromHtml(
                    "Mines site name : <font color='black'>${Converter.format(listModel[position].minesSiteName)}</font>"),
                TextView.BufferType.SPANNABLE)
            holder.binding.tvSubTitleTwo.setText(Html.fromHtml(
                "Mapping sheet no : <font color='black'>${Converter.format(listModel[position].mappingSheetNo)}</font>"),
                TextView.BufferType.SPANNABLE)
            holder.binding.llMainLayout.setOnClickListener {
                callOpenCastDetailActivity(act, "1", listModel[position].mappingSheetNo)
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