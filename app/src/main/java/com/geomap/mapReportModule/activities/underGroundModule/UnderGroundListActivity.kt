package com.geomap.mapReportModule.activities.underGroundModule

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
import com.geomap.databinding.ActivityUnderGroundListBinding
import com.geomap.databinding.MappingReportListLayoutBinding
import com.geomap.mapReportModule.models.DashboardViewAllModel
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.utils.CONSTANTS
import com.geomap.utils.Converter
import com.geomap.utils.RetrofitService

class UnderGroundListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundListBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private lateinit var viewModel : AllViewModel
    private val retrofitService = RetrofitService.getInstance()
    private var underGroundListAdapter : UnderGroundListAdapter? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_under_ground_list)
        ctx = this@UnderGroundListActivity
        act = this@UnderGroundListActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.rvUnderGroundList.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvUnderGroundList.itemAnimator = DefaultItemAnimator()
        postData()
    }

    fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            viewModel = ViewModelProvider(this, UserModelFactory(
                UserRepository(retrofitService)))[AllViewModel::class.java]
            viewModel.getURViewAllListing(userId.toString())
            viewModel.getURViewAllListing.observe(this) {
                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                when {
                    it?.responseCode == getString(R.string.ResponseCodesuccess) -> {
                        if (it.responseData!!.isEmpty()) {
                            binding.rvUnderGroundList.visibility = View.GONE
                            binding.tvFound.visibility = View.VISIBLE
                        } else {
                            binding.rvUnderGroundList.visibility = View.VISIBLE
                            binding.tvFound.visibility = View.GONE

                        }
                        underGroundListAdapter = UnderGroundListAdapter(
                            it.responseData!!)
                        binding.rvUnderGroundList.adapter = underGroundListAdapter
                    }
                    it.responseCode == act.getString(R.string.ResponseCodefail) -> {
                        binding.rvUnderGroundList.visibility = View.GONE
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

    inner class UnderGroundListAdapter(
        private val listModel : List<DashboardViewAllModel.ResponseData>
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
            holder.binding.tvName.text = Converter.format(listModel[position].name)
            holder.binding.tvArea.text = Converter.format(listModel[position].location)
            holder.binding.tvDate.text = Converter.format(listModel[position].ugDate)

            holder.binding.tvSubTitleOne.setText(
                Html.fromHtml(
                    "Mapped By : <font color='black'>${
                        Converter.format(listModel[position].mappedBy)
                    }</font>"),
                TextView.BufferType.SPANNABLE)
            holder.binding.tvSubTitleTwo.setText(Html.fromHtml(
                "Map serial no : <font color='black'>${
                    Converter.format(listModel[position].mapSerialNo)
                }</font>"),
                TextView.BufferType.SPANNABLE)
            holder.binding.llMainLayout.setOnClickListener {
                callUnderGroundDetailActivity(act, "1", listModel[position].mapSerialNo)
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