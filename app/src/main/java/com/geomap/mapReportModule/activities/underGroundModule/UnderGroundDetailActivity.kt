package com.geomap.mapReportModule.activities.underGroundModule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundDetailBinding
import com.geomap.databinding.AttributeLayoutBinding
import com.geomap.mapReportModule.models.Attribute
import com.geomap.mapReportModule.models.UnderGroundDetailsModel
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.google.gson.Gson
import java.util.*

class UnderGroundDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var attributesListAdapter : AttributesListAdapter? = null
    private var mapSerialNo : String? = null
    private var userId : String? = null
    private lateinit var viewModel : AllViewModel
    private lateinit var model : UnderGroundDetailsModel
    private var gson = Gson()
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_under_ground_detail)
        ctx = this@UnderGroundDetailActivity
        act = this@UnderGroundDetailActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        if (intent.extras != null) {
            mapSerialNo = intent.extras?.getString("id")
        }

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.llEdit.setOnClickListener {
            val i = Intent(act, UnderGroundFormFirstStepActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            i.putExtra("flag", "detail")
            i.putExtra("data", gson.toJson(model))
            act.startActivity(i)
        }

        val mLayoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvAttributesList.layoutManager = mLayoutManager
        binding.rvAttributesList.itemAnimator = DefaultItemAnimator()

        postData()

        binding.btnViewPdf.setOnClickListener {
            callViewPdfActivity(act, "1", "ug", mapSerialNo)
        }
    }

    fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            viewModel = ViewModelProvider(
                this, UserModelFactory(
                    UserRepository(retrofitService)
                )
            )[AllViewModel::class.java]
            viewModel.getUnderGroundDetails(mapSerialNo.toString())
            viewModel.getUnderGroundDetails.observe(this) { it ->
                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                when {
                    it?.ResponseCode == getString(R.string.ResponseCodesuccess) -> {
                        model = it
                        binding.llMainLayout.visibility = View.VISIBLE
                        binding.btnViewPdf.visibility = View.VISIBLE
                        val ugDetail =
                                UnderGroundDetailsModel(
                                    it.ResponseCode, it.ResponseData,
                                    it.ResponseMessage, it.ResponseStatus
                                )
                        binding.ugDetail = ugDetail
                        it.ResponseData.let {
                            if (it.attribute.isEmpty()) {
                                binding.tvAttributes.visibility = View.GONE
                                binding.rvAttributesList.visibility = View.GONE
                            } else {
                                binding.tvAttributes.visibility = View.VISIBLE
                                binding.rvAttributesList.visibility = View.VISIBLE
                                attributesListAdapter =
                                        AttributesListAdapter(
                                            it.attribute
                                        )
                                binding.rvAttributesList.adapter = attributesListAdapter
                            }

                            Glide.with(ctx).load(it.roofImage)
                                    .thumbnail(0.10f).into(binding.roofImage)
                            Glide.with(ctx).load(it.leftImage)
                                    .thumbnail(0.10f).into(binding.leftImage)
                            Glide.with(ctx).load(it.rightImage)
                                    .thumbnail(0.10f).into(binding.rightImage)
                            Glide.with(ctx).load(it.faceImage)
                                    .thumbnail(0.10f).into(binding.faceImage)
                        }
                    }
                    it.ResponseCode == act.getString(R.string.ResponseCodefail) -> {
                        showToast(it.ResponseMessage, act)
                    }
                    it.ResponseCode == act.getString(R.string.ResponseCodeDeleted) -> {
                        callDelete403(act, it.ResponseMessage)
                    }
                }
            }
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    inner class AttributesListAdapter(
            private val listModel : List<Attribute>
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