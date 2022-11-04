package com.geomap.mapReportModule.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityDashboardBinding
import com.geomap.databinding.MappingReportListLayoutBinding
import com.geomap.mapReportModule.models.DashboardModel
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDashboardBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var dialog : Dialog? = null
    private var underGroundListAdapter : UnderGroundListAdapter? = null
    private var openCastListAdapter : OpenCastListAdapter? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        ctx = this@DashboardActivity
        act = this@DashboardActivity

        binding.llMenu.setOnClickListener {
            callMenuListActivity(act, "1")
        }

        binding.tvAddReport.setOnClickListener {
//            callAddReportActivity(act, "1")
            dialog = Dialog(ctx)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setContentView(R.layout.add_report_layout)
            dialog!!.window!!.setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(ctx, R.color.transparent_white)))
            dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            val btnAddUnderGroundsReport =
                dialog!!.findViewById<Button>(R.id.btnAddUnderGroundsReport)
            val btnAddOpenCastReport = dialog!!.findViewById<Button>(R.id.btnAddOpenCastReport)


            dialog!!.setOnKeyListener { _ : DialogInterface?, keyCode : Int, _ : KeyEvent? ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog!!.dismiss()
                    return@setOnKeyListener true
                }
                false
            }

            btnAddUnderGroundsReport.setOnClickListener {
                callUnderGroundFormFirstStepActivity(act, "1")
            }

            btnAddOpenCastReport.setOnClickListener {
                callOpenCastFormFirstStepActivity(act, "1")
            }
            dialog!!.show()
            dialog!!.setCancelable(true)
        }

        binding.tvUnderGroundListViewAll.setOnClickListener {
            callUnderGroundListActivity(act, "1")
        }

        binding.tvOpenCastListViewAll.setOnClickListener {
            callOpenCastListActivity(act, "1")
        }

        val mLayoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvUnderGroundList.layoutManager = mLayoutManager
        binding.rvUnderGroundList.itemAnimator = DefaultItemAnimator()

        val mLayoutManage : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvOpenCastList.layoutManager = mLayoutManage
        binding.rvOpenCastList.itemAnimator = DefaultItemAnimator()

        postData()
    }

    private fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            RetrofitService.getInstance()
                .getDashboardlisting
                .enqueue(object : Callback<DashboardModel> {
                    override fun onResponse(call : Call<DashboardModel>,
                        response : Response<DashboardModel>) {
                        try {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            val model : DashboardModel? = response.body()!!
                            when (model!!.responseCode) {
                                getString(R.string.ResponseCodesuccess) -> {
                                    binding.llUnderGroundList.visibility = View.VISIBLE
                                    binding.rvUnderGroundList.visibility = View.VISIBLE
                                    binding.llOpenCastList.visibility = View.VISIBLE
                                    binding.rvOpenCastList.visibility = View.VISIBLE
                                    underGroundListAdapter = UnderGroundListAdapter(
                                        model.responseData?.underGround!!)
                                    binding.rvUnderGroundList.adapter = underGroundListAdapter
                                    openCastListAdapter =
                                        OpenCastListAdapter(model.responseData?.openCast!!)
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

                    override fun onFailure(call : Call<DashboardModel>, t : Throwable) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                    }
                })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    inner class UnderGroundListAdapter(
        private val listModel : List<DashboardModel.ResponseData.UnderGround>
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
            holder.binding.tvArea.text = listModel[position].location
            holder.binding.tvSubTitle.text = listModel[position].description
            holder.binding.tvDate.text = listModel[position].createdAt

            holder.binding.llMainLayout.setOnClickListener {
                callUnderGroundDetailActivity(act, "1")
            }
        }

        override fun getItemCount() : Int {
            return if (listModel.size < 2) {
                listModel.size
            } else {
                2
            }
        }
    }

    inner class OpenCastListAdapter(
        private val listModel : List<DashboardModel.ResponseData.OpenCast>
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
            holder.binding.tvArea.text = listModel[position].location
            holder.binding.tvSubTitle.text = listModel[position].description
            holder.binding.tvDate.text = listModel[position].createdAt

            holder.binding.llMainLayout.setOnClickListener {
                callOpenCastDetailActivity(act, "1")
            }
        }

        override fun getItemCount() : Int {
            return if (listModel.size < 2) {
                listModel.size
            } else {
                2
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}