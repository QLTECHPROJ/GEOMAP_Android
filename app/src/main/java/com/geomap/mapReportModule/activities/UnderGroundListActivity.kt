package com.geomap.mapReportModule.activities

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
import com.geomap.databinding.ActivityUnderGroundListBinding
import com.geomap.databinding.MappingReportListLayoutBinding
import com.geomap.mapReportModule.models.DashboardViewAllModel
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnderGroundListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundListBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var underGroundListAdapter : UnderGroundListAdapter? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_under_ground_list)
        ctx = this@UnderGroundListActivity
        act = this@UnderGroundListActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        val mLayoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvUnderGroundList.layoutManager = mLayoutManager
        binding.rvUnderGroundList.itemAnimator = DefaultItemAnimator()

        postData()
    }

    private fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            RetrofitService.getInstance()
                .getDashboardViewAlllisting
                .enqueue(object : Callback<DashboardViewAllModel> {
                    override fun onResponse(call : Call<DashboardViewAllModel>,
                        response : Response<DashboardViewAllModel>) {
                        try {
                            hideProgressBar(binding.progressBar,
                                binding.progressBarHolder, act)
                            val model : DashboardViewAllModel? = response.body()!!
                            when (model!!.responseCode) {
                                getString(R.string.ResponseCodesuccess) -> {
                                    binding.rvUnderGroundList.visibility = View.VISIBLE
                                    underGroundListAdapter = UnderGroundListAdapter(
                                        model.responseData!!)
                                    binding.rvUnderGroundList.adapter = underGroundListAdapter
                                }
                                getString(R.string.ResponseCodefail) -> {
                                    showToast(model.responseMessage, act)
                                }
                                getString(R.string.ResponseCodeDeleted) -> {
                                    callDelete403(act, model.responseMessage)
                                }
                            }
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call : Call<DashboardViewAllModel>, t : Throwable) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder,
                            act)
                    }
                })
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
            holder.binding.tvName.text = listModel[position].name
            holder.binding.tvArea.text = listModel[position].name
//                "${listModel[position].location}, ${listModel[position].country}"
//            holder.binding.tvSubTitle.text = listModel[position].description
            holder.binding.tvDate.text = listModel[position].createdAt

            holder.binding.llMainLayout.setOnClickListener {
                callUnderGroundDetailActivity(act, "1")
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