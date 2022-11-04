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
import com.geomap.databinding.ActivityOpenCastListBinding
import com.geomap.databinding.MappingReportListLayoutBinding
import com.geomap.mapReportModule.models.DashboardViewAllModel
import com.geomap.mapReportModule.models.DummyModel
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenCastListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastListBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var openCastListAdapter : OpenCastListAdapter? = null
    private var listModel : Array<DummyModel> = arrayOf(
        DummyModel("My Report Name 1"),
        DummyModel("My Report Name 2"), DummyModel("My Report Name 3"),
        DummyModel("My Report Name 4")
    )

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_list)
        ctx = this@OpenCastListActivity
        act = this@OpenCastListActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        val mLayoutManage : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvOpenCastList.layoutManager = mLayoutManage
        binding.rvOpenCastList.itemAnimator = DefaultItemAnimator()

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
                                    binding.rvOpenCastList.visibility = View.VISIBLE
                                    openCastListAdapter =
                                        OpenCastListAdapter(model.responseData!!)
                                    binding.rvOpenCastList.adapter = openCastListAdapter
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
            holder.binding.tvName.text = listModel[position].name
            holder.binding.tvArea.text = listModel[position].name
//                "${listModel[position].address}, ${listModel[position].country}"
//            holder.binding.tvSubTitle.text = listModel[position].description
            holder.binding.tvDate.text = listModel[position].createdAt
            holder.binding.llMainLayout.setOnClickListener {
                callOpenCastDetailActivity(act, "1")
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