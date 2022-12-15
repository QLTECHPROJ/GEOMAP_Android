package com.geomap.mapReportModule.activities.underGroundModule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geomap.DataBaseFunctions
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundDraftListBinding
import com.geomap.databinding.MappingReportListLayoutBinding
import com.geomap.roomDataBase.UnderGroundMappingReport
import com.geomap.utils.CONSTANTS
import com.google.gson.Gson

class UnderGroundListDraftActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundDraftListBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var gson = Gson()
    private var underGroundListAdapter : UnderGroundListAdapter? = null
    var list = java.util.ArrayList<UnderGroundMappingReport>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_under_ground_draft_list)
        ctx = this@UnderGroundListDraftActivity
        act = this@UnderGroundListDraftActivity
        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
        val mLayoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvUnderGroundList.layoutManager = mLayoutManager
        binding.rvUnderGroundList.itemAnimator = DefaultItemAnimator()
        postData()
    }

    private fun postData() {
        DB = getDataBase(ctx)
        DB.taskDao().geAllUnderGroundMappingReport1(userId)
            .observe(ctx as LifecycleOwner) { lists ->
                list = lists as java.util.ArrayList<UnderGroundMappingReport>
                callAdapter(list)
            }
/*        try {
            GeoMapDatabase.databaseWriteExecutor3.execute {
                list = DB.taskDao().geAllUnderGroundMappingReport() as java.util.ArrayList<UnderGroundMappingReport>
                callAdapter(list)
            }
        }catch (e: Exception){
            e.printStackTrace();
        }*/
    }

    private fun callAdapter(list : java.util.ArrayList<UnderGroundMappingReport>) {
        if (list.isEmpty()) {
            binding.rvUnderGroundList.visibility = View.GONE
            binding.tvFound.visibility = View.VISIBLE
        } else {
            callData(list)
        }
    }

    private fun callData(list : ArrayList<UnderGroundMappingReport>) {

        binding.rvUnderGroundList.visibility = View.VISIBLE
        binding.tvFound.visibility = View.GONE
        underGroundListAdapter = UnderGroundListAdapter(list)
        binding.rvUnderGroundList.adapter = underGroundListAdapter
        Log.e("List UnderGroundMappingReport",
            "true" + DataBaseFunctions.gson.toJson(list).toString())
    }

    inner class UnderGroundListAdapter(
        private val listModel : ArrayList<UnderGroundMappingReport>) :
        RecyclerView.Adapter<UnderGroundListAdapter.MyViewHolder>() {
        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : MappingReportListLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.mapping_report_list_layout, parent,
                false)
            return MyViewHolder(v)
        }

        inner class MyViewHolder(
            var binding : MappingReportListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
            holder.binding.tvName.text = listModel[position].name
            holder.binding.tvArea.text = listModel[position].location
            holder.binding.tvDate.text = listModel[position].ugDate

            holder.binding.tvSubTitleOne.setText(
                Html.fromHtml(
                    "Mapped By : <font color='black'>${listModel[position].mappedBy}</font>"),
                TextView.BufferType.SPANNABLE)
            holder.binding.llMainLayout.setOnClickListener {
                callUnderGroundDetailDraftActivity(act, "1",
                    gson.toJson(listModel[position]).toString())
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