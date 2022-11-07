package com.geomap.faqModule.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityFaqBinding
import com.geomap.databinding.FaqLayoutBinding
import com.geomap.faqModule.models.FaqListModel
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqActivity : AppCompatActivity() {
    lateinit var binding : ActivityFaqBinding
    var faqListModel : FaqListModel? = null
    private var modelList : ArrayList<FaqListModel.ResponseData>? = null
    lateinit var adapter : FaqListAdapter
    lateinit var act : Activity
    lateinit var ctx : Context
    private var userId : String? = ""
    lateinit var section : ArrayList<String>

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq)
        act = this@FaqActivity
        ctx = this@FaqActivity
        val shared1 = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared1.getString(CONSTANTS.userId, "")
        modelList = ArrayList()
        section = ArrayList()
        prepareData()
        binding.llBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun prepareData() {
        if (isNetworkConnected(this)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            val listCall = RetrofitService.getInstance().faqLists
            listCall.enqueue(object : Callback<FaqListModel?> {
                override fun onResponse(call : Call<FaqListModel?>,
                    response : Response<FaqListModel?>) {
                    try {
                        val listModel = response.body()
                        if (listModel!!.responseCode.equals(getString(R.string.ResponseCodesuccess),
                                ignoreCase = true)) {
                            binding.rvFAQList.visibility = View.VISIBLE
                            hideProgressBar(binding.progressBar,
                                binding.progressBarHolder, act)
                            faqListModel = listModel
                            binding.rvFAQList.layoutManager = LinearLayoutManager(act)
                            adapter =
                                FaqListAdapter(listModel.responseData!!)
                            binding.rvFAQList.adapter = adapter

                        }
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call : Call<FaqListModel?>, t : Throwable) {
                    hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                }
            })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    inner class FaqListAdapter(private val modelList : List<FaqListModel.ResponseData>) :
        RecyclerView.Adapter<FaqListAdapter.MyViewHolder>() {
        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : FaqLayoutBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.faq_layout, parent, false)
            return MyViewHolder(v)
        }

        @SuppressLint("ResourceType") override fun onBindViewHolder(holder : MyViewHolder,
            position : Int) {
            for (i in modelList.indices) {
                section.add(modelList[position].question.toString())
                section.add(modelList[position].answer.toString())
            }

            holder.binding.tvTitle.text = modelList[position].question
            holder.binding.tvDesc.text = modelList[position].answer

            holder.binding.llMainLayout.setOnClickListener {
                callChangeArrow(holder)
            }
            /* holder.binding.ivClickRight.setOnClickListener {
                 myBackPress = true
                 holder.binding.tvTitle.setTextColor(ContextCompat.getColor(ctx, R.color.white))
                 holder.binding.tvDesc.isFocusable = true
                 holder.binding.tvDesc.requestFocus()
                 holder.binding.tvDesc.visibility = View.VISIBLE
                 holder.binding.ivClickRight.visibility = View.GONE
                 holder.binding.ivClickDown.visibility = View.VISIBLE
                 holder.binding.llBgChange.setBackgroundResource(R.drawable.faq_not_clicked)
                 holder.binding.llMainLayout.setBackgroundResource(R.drawable.faq_clicked)
                 holder.binding.ivClickDown.setImageResource(R.drawable.ic_white_arrow_down_icon)
             }
             holder.binding.ivClickDown.setOnClickListener {
                 myBackPress = true
                 holder.binding.llBgChange.setBackgroundResource(R.color.transparent_white)
                 holder.binding.llMainLayout.setBackgroundResource(R.drawable.faq_not_clicked)
                 holder.binding.tvTitle.setTextColor(ContextCompat.getColor(ctx, R.color.light_black))
                 holder.binding.tvDesc.visibility = View.GONE
                 holder.binding.ivClickRight.visibility = View.VISIBLE
                 holder.binding.ivClickDown.visibility = View.GONE
                 holder.binding.ivClickDown.setImageResource(R.drawable.ic_right_gray_arrow_icon)
             }*/
            if (modelList.isEmpty()) {
                binding.tvFound.visibility = View.VISIBLE
                binding.rvFAQList.visibility = View.GONE
            } else {
                binding.tvFound.visibility = View.GONE
                binding.rvFAQList.visibility = View.VISIBLE
            }
        }

        private fun callChangeArrow(holder : MyViewHolder) {
            if (holder.binding.ivClickDown.visibility == View.VISIBLE) {
                holder.binding.llBgChange.setBackgroundResource(R.color.transparent_white)
                holder.binding.llMainLayout.setBackgroundResource(R.drawable.faq_not_clicked)
                holder.binding.tvTitle.setTextColor(
                    ContextCompat.getColor(ctx, R.color.light_black))
                holder.binding.tvDesc.visibility = View.GONE
                holder.binding.ivClickRight.visibility = View.VISIBLE
                holder.binding.ivClickDown.visibility = View.GONE
                holder.binding.ivClickDown.setImageResource(R.drawable.ic_arrow_icon)
            } else {
                holder.binding.tvTitle.setTextColor(ContextCompat.getColor(ctx, R.color.white))
                holder.binding.tvDesc.isFocusable = true
                holder.binding.tvDesc.requestFocus()
                holder.binding.tvDesc.visibility = View.VISIBLE
                holder.binding.ivClickRight.visibility = View.GONE
                holder.binding.ivClickDown.visibility = View.VISIBLE
                holder.binding.llBgChange.setBackgroundResource(R.drawable.faq_not_clicked)
                holder.binding.llMainLayout.setBackgroundResource(R.drawable.faq_clicked)
                holder.binding.ivClickDown.setImageResource(R.drawable.ic_down_arrow_white_icon)
            }

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