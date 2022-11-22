package com.geomap.faqModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityFaqBinding
import com.geomap.databinding.FaqLayoutBinding
import com.geomap.faqModule.models.FaQResponseData
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.utils.RetrofitService

class FaqActivity : AppCompatActivity() {
    lateinit var binding : ActivityFaqBinding
    lateinit var adapter : FaqListAdapter
    lateinit var act : Activity
    lateinit var ctx : Context
    private lateinit var viewModel : AllViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq)
        act = this@FaqActivity
        ctx = this@FaqActivity
        getData()

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            viewModel = ViewModelProvider(this, UserModelFactory(
                UserRepository(retrofitService)))[AllViewModel::class.java]
            viewModel.faqLists()
            viewModel.faqLists.observe(this) {
                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                when {
                    it?.ResponseCode == getString(R.string.ResponseCodesuccess) -> {
                        binding.rvFAQList.visibility = View.VISIBLE
                        binding.rvFAQList.layoutManager = LinearLayoutManager(act)
                        adapter = FaqListAdapter(it.ResponseData)
                        binding.rvFAQList.adapter = adapter
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

    inner class FaqListAdapter(private val modelList : List<FaQResponseData>) :
        RecyclerView.Adapter<FaqListAdapter.MyViewHolder>() {
        var expandedPosition = -1
        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : FaqLayoutBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.faq_layout, parent, false)
            return MyViewHolder(v)
        }

        override fun onBindViewHolder(holder : MyViewHolder,
            position : Int) {

            if (modelList.isEmpty()) {
                binding.tvFound.visibility = View.VISIBLE
                binding.rvFAQList.visibility = View.GONE
            } else {
                binding.tvFound.visibility = View.GONE
                binding.rvFAQList.visibility = View.VISIBLE
            }

            holder.binding.tvTitle.text = modelList[position].question
            holder.binding.tvDesc.text = modelList[position].answer
            if (position == expandedPosition) {
                callExpandView(holder)
            } else {
                callCollapseView(holder)
            }

            holder.binding.llMainLayout.setOnClickListener {
                if (expandedPosition >= -1) {
                    val prev = expandedPosition
                    notifyItemChanged(prev)
                    callExpandView(holder)
                } else {
                    callCollapseView(holder)
                }
                expandedPosition = holder.position
                notifyItemChanged(expandedPosition)
            }
        }

        private fun callExpandView(holder : MyViewHolder) {
            holder.binding.llBgChange.isFocusable = true
            holder.binding.tvTitle.setTextColor(ContextCompat.getColor(ctx, R.color.white))
            holder.binding.tvDesc.requestFocus()
            holder.binding.tvDesc.visibility = View.VISIBLE
            holder.binding.ivClickRight.visibility = View.GONE
            holder.binding.ivClickDown.visibility = View.VISIBLE
            holder.binding.llBgChange.setBackgroundResource(R.drawable.faq_not_clicked)
            holder.binding.llMainLayout.setBackgroundResource(R.drawable.faq_clicked)
            holder.binding.ivClickDown.setImageResource(R.drawable.ic_down_arrow_white_icon)
        }

        private fun callCollapseView(holder : MyViewHolder) {
            holder.binding.llBgChange.isFocusable = false
            holder.binding.llBgChange.setBackgroundResource(R.color.transparent_white)
            holder.binding.llMainLayout.setBackgroundResource(R.drawable.faq_not_clicked)
            holder.binding.tvTitle.setTextColor(
                ContextCompat.getColor(ctx, R.color.light_black))
            holder.binding.tvDesc.visibility = View.GONE
            holder.binding.ivClickRight.visibility = View.VISIBLE
            holder.binding.ivClickDown.visibility = View.GONE
            holder.binding.ivClickDown.setImageResource(R.drawable.ic_arrow_icon)
        }

        override fun getItemCount() : Int {
            return modelList.size
        }

        inner class MyViewHolder(var binding : FaqLayoutBinding) :
            RecyclerView.ViewHolder(binding.root)
    }

    override fun onBackPressed() {
        finish()
    }
}