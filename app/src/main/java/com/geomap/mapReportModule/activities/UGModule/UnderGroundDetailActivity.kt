package com.geomap.mapReportModule.activities.UGModule

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
import com.geomap.mapReportModule.models.UnderGroundDetailsModel
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnderGroundDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundDetailBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var attributesListAdapter : AttributesListAdapter? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_under_ground_detail)
        ctx = this@UnderGroundDetailActivity
        act = this@UnderGroundDetailActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        val mLayoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvAttributesList.layoutManager = mLayoutManager
        binding.rvAttributesList.itemAnimator = DefaultItemAnimator()

        postData()

        binding.btnViewPdf.setOnClickListener {
            callViewPdfActivity(act, "1")
        }
    }

    private fun postData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            RetrofitService.getInstance()
                .getUnderGroundDetails("1")
                .enqueue(object : Callback<UnderGroundDetailsModel> {
                    override fun onResponse(call : Call<UnderGroundDetailsModel>,
                        response : Response<UnderGroundDetailsModel>) {
                        try {
                            hideProgressBar(binding.progressBar,
                                binding.progressBarHolder, act)
                            val model : UnderGroundDetailsModel? = response.body()!!
                            when (model!!.responseCode) {
                                getString(R.string.ResponseCodesuccess) -> {
                                    binding.llMainLayout.visibility = View.VISIBLE
                                    binding.btnViewPdf.visibility = View.VISIBLE
                                    model.responseData?.let {
                                        it
                                        binding.tvSerialNo.text = it.mapSerialNo
                                        binding.tvDate.text = it.ugDate
                                        binding.tvShift.text = it.shift
                                        binding.tvMappedBy.text = it.mappedBy
                                        binding.tvScale.text = it.scale
                                        binding.tvLocation.text = it.location
                                        binding.tvLoadName.text = it.venieLoad
                                        binding.tvXCoordinate.text = it.xCordinate
                                        binding.tvYCoordinate.text = it.yCordinate
                                        binding.tvZCoordinate.text = it.zCordinate
                                        attributesListAdapter =
                                            AttributesListAdapter(model.responseData?.attribute!!)
                                        binding.rvAttributesList.adapter = attributesListAdapter
                                    }
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

                    override fun onFailure(call : Call<UnderGroundDetailsModel>, t : Throwable) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder,
                            act)
                    }
                })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    inner class AttributesListAdapter(
        private val listModel : List<UnderGroundDetailsModel.ResponseData.Attribute>
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