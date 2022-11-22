package com.geomap.mapReportModule.activities.underGroundModule

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.geomap.DataBaseFunctions
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormFirstStepBinding
import com.geomap.databinding.AttributeLayoutBinding
import com.geomap.databinding.CommonPopupLayoutBinding
import com.geomap.mapReportModule.models.AttributeDataModel
import com.geomap.mapReportModule.models.AttributesListModel
import com.geomap.roomDataBase.AttributeData
import com.geomap.roomDataBase.GeoMapDatabase
import com.geomap.roomDataBase.Nos
import com.geomap.utils.RetrofitService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UnderGroundFormFirstStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundFormFirstStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private lateinit var dialog : Dialog
    lateinit var rvList : RecyclerView
    lateinit var tvFound : TextView
    lateinit var pb : ProgressBar
    lateinit var pbh : FrameLayout
    lateinit var searchView : SearchView
    private var attributesListAdapter : AttributesListAdapter? = null
    lateinit var tvTilte : TextView
    var attributeList = ArrayList<AttributeData>()
    var attributeDataModel = AttributeDataModel()
    var nosList = ArrayList<Nos>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_under_ground_form_first_step)
        ctx = this@UnderGroundFormFirstStepActivity
        act = this@UnderGroundFormFirstStepActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnNextStep.isEnabled = true
        binding.btnNextStep.setBackgroundResource(R.drawable.enable_button)
        dialog = Dialog(ctx)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.common_list_layout)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        rvList = dialog.findViewById(R.id.rvList)
        searchView = dialog.findViewById(R.id.searchView)
        tvFound = dialog.findViewById(R.id.tvFound)
        pb = dialog.findViewById(R.id.progressBar)
        pbh = dialog.findViewById(R.id.progressBarHolder)
        tvTilte = dialog.findViewById(R.id.tvTilte)

        val mLayoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.rvAttributesList.layoutManager = mLayoutManager
        binding.rvAttributesList.itemAnimator = DefaultItemAnimator()

        binding.llMainLayout.visibility = View.VISIBLE
        binding.cvAttributes.setOnClickListener {
            dialog = Dialog(ctx)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.common_list_layout)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            rvList = dialog.findViewById(R.id.rvList)
            searchView = dialog.findViewById(R.id.searchView)
            tvFound = dialog.findViewById(R.id.tvFound)
            pb = dialog.findViewById(R.id.progressBar)
            pbh = dialog.findViewById(R.id.progressBarHolder)
            tvTilte = dialog.findViewById(R.id.tvTilte)
            tvTilte.text = getString(R.string.choose_your_attributes)
            rvList.visibility = View.VISIBLE
            rvList.layoutManager = LinearLayoutManager(ctx)
            dialog.setOnKeyListener { _ : DialogInterface?, keyCode : Int, _ : KeyEvent? ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss()
                }
                false
            }
            searchView.onActionViewExpanded()
            val searchEditText : EditText = searchView.findViewById(
                androidx.appcompat.R.id.search_src_text)
            searchEditText.setTextColor(ContextCompat.getColor(ctx, R.color.light_black))
            searchEditText.setHintTextColor(ContextCompat.getColor(ctx, R.color.light_black))
            searchView.clearFocus()
            searchEditText.hint = getString(R.string.pls_select_your_attributes)

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(search : String) : Boolean {
                    window.setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                    return false
                }

                override fun onQueryTextChange(search : String) : Boolean {
                    try {
                        attributesAdapter?.filter?.filter(search)
                        attributeSearchFilter = search
                    } catch (e : java.lang.Exception) {
                        e.printStackTrace()
                    }
                    return false
                }
            })
            if (attributeModelList.isEmpty()) {
                tvFound.visibility = View.VISIBLE
                rvList.visibility = View.GONE
                tvFound.text = getString(R.string.no_result_found)
            } else {
                tvFound.visibility = View.GONE
                rvList.visibility = View.VISIBLE
                attributesAdapter = AttributesAdapter(dialog, binding, attributeModelList, rvList,
                    tvFound, ctx)
                rvList.adapter = attributesAdapter
            }
            dialog.show()
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
        }

        binding.cvNos.setOnClickListener {
            dialog = Dialog(ctx)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.common_list_layout)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            rvList = dialog.findViewById(R.id.rvList)
            searchView = dialog.findViewById(R.id.searchView)
            tvFound = dialog.findViewById(R.id.tvFound)
            pb = dialog.findViewById(R.id.progressBar)
            pbh = dialog.findViewById(R.id.progressBarHolder)
            tvTilte = dialog.findViewById(R.id.tvTilte)
            tvTilte.text = getString(R.string.choose_your_nos)
            rvList.layoutManager = LinearLayoutManager(ctx)

            dialog.setOnKeyListener { _ : DialogInterface?, keyCode : Int, _ : KeyEvent? ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss()
                }
                false
            }
            searchView.onActionViewExpanded()
            val searchEditText : EditText = searchView.findViewById(
                androidx.appcompat.R.id.search_src_text)
            searchEditText.setTextColor(ContextCompat.getColor(ctx, R.color.light_black))
            searchEditText.setHintTextColor(ContextCompat.getColor(ctx, R.color.light_black))
            searchView.clearFocus()
            searchEditText.hint = getString(R.string.pls_select_your_nos)

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(search : String) : Boolean {
                    window.setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                    return false
                }

                override fun onQueryTextChange(search : String) : Boolean {
                    try {
                        nosAdapter?.filter?.filter(search)
                        nosSearchFilter = search
                    } catch (e : java.lang.Exception) {
                        e.printStackTrace()
                    }
                    return false
                }
            })
            if (attributesId == 0) {
                showToast(getString(R.string.please_select_attribute), act)
            } else {
                if (nosModelList.isEmpty()) {
                    tvFound.visibility = View.VISIBLE
                    rvList.visibility = View.GONE
                    tvFound.text = getString(R.string.no_result_found)
                } else {
                    tvFound.visibility = View.GONE
                    rvList.visibility = View.VISIBLE
                    nosAdapter = NosAdapter(dialog, binding, nosModelList, rvList, tvFound, ctx)
                    rvList.adapter = nosAdapter
                    dialog.show()
                    dialog.setCanceledOnTouchOutside(true)
                    dialog.setCancelable(true)
                }
            }
        }

        binding.tvAddAttributes.setOnClickListener {
            checkCondition("1")
        }

        binding.btnNextStep.setOnClickListener {
            checkCondition("0")
        }
    }

    private fun clearData(flag : String) {
        attributesId = 0
        nosId = 0
        attributesName = ""
        nosName = ""
        property = ""
        attributesAdapter = null
        nosAdapter = null
        binding.tvAttributeName.text = getString(R.string.select_attributes)
        binding.tvNos.text = getString(R.string.select_nos)
        binding.edtProperty.setText("")
        binding.llMainLayout.visibility = View.VISIBLE
        if (flag == "0") {
            if (attributeDataModelList.isEmpty()) {
                showToast(getString(R.string.please_add_attribute_data), act)
            } else {
                nosModelList = ArrayList<AttributesListModel.ResponseData.Nos>()
                attributeModelList = ArrayList<AttributesListModel.ResponseData>()
                callUnderGroundFormSecondStepActivity(act, "1",
                    gson.toJson(attributeDataModelList).toString())

            }
        }
    }

    private fun saveData(flag : String) {
        attributeDataModel = AttributeDataModel()
        attributeDataModel.name = attributesName!!
        attributeDataModel.nose = nosName!!
        attributeDataModel.properties = property!!
        attributeDataModelList.add(attributeDataModel)
        Log.e("attribute Data Model", gson.toJson(attributeDataModelList).toString())
        if (attributeDataModelList.isEmpty()) {
            binding.tvAttributes.visibility = View.GONE
            binding.cvAttributesList.visibility = View.GONE
        } else {
            binding.tvAttributes.visibility = View.VISIBLE
            binding.cvAttributesList.visibility = View.VISIBLE
            attributesListAdapter =
                AttributesListAdapter(
                    attributeDataModelList)
            binding.rvAttributesList.adapter = attributesListAdapter
        }
        clearData(flag)
    }

    private fun checkCondition(flag : String) {
        property = binding.edtProperty.text.toString()
        if (attributesName != "" && nosName != "" && property != "") {
            if (attributeDataModelList.isEmpty()) {
                saveData(flag)
            } else {
                for (i in attributeDataModelList.indices) {
                    if (attributeDataModelList[i].name == attributesName && attributeDataModelList[i].nose == nosName && attributeDataModelList[i].properties == property) {
                        break
                    } else if (i == attributeDataModelList.size - 1) {
                        saveData(flag)
                    }
                }
            }
        } else if (attributesName == "" && nosName == "" && property == "") {
            clearData(flag)
        } else if (property == "" && attributesName != "" && nosName != "") {
            showToast(getString(R.string.please_add_propery), act)
        } else if (nosName == "" && attributesName != "" && property != "") {
            showToast(getString(R.string.please_select_nos), act)
        }
    }

    inner class AttributesListAdapter(
        private val listModel : ArrayList<AttributeDataModel>
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
        sdt = 3
        attributesId = 0
        nosId = 0
        attributesName = ""
        nosName = ""
        property = ""
        attributeSearchFilter = ""
        nosSearchFilter = ""
        nosModelList = ArrayList<AttributesListModel.ResponseData.Nos>()
        attributeModelList = ArrayList<AttributesListModel.ResponseData>()
        attributeDataModelList = ArrayList<AttributeDataModel>()
        attributesAdapter = null
        nosAdapter = null
        finish()
    }

    override fun onResume() {
        prepareAttributesListing()
        super.onResume()
    }

    private fun prepareAttributesListing() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(pb, pbh, act)
            searchView.isEnabled = false
            searchView.isClickable = false
            RetrofitService.getInstance().getAttributesList.enqueue(object :
                Callback<AttributesListModel> {
                override fun onResponse(call : Call<AttributesListModel>,
                    response : Response<AttributesListModel>) {
                    val model = response.body()
                    hideProgressBar(pb, pbh, act)
                    when {
                        model!!.responseCode!! == getString(R.string.ResponseCodesuccess) -> {
                            searchView.isEnabled = true
                            searchView.isClickable = true
                            attributeModelList = model.responseData!!

                        }
                        model.responseCode!! == getString(R.string.ResponseCodefail) -> {
                            showToast(model.responseMessage, act)
                        }
                        model.responseCode!! == getString(R.string.ResponseCodeDeleted) -> {
                            callDelete403(act, model.responseMessage)
                        }
                    }

                }

                override fun onFailure(call : Call<AttributesListModel>, t : Throwable) {
                    hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                }
            })
        } else {
            DB = Room.databaseBuilder(ctx, GeoMapDatabase::class.java, "GeoMap_database").build()
            attributeList = ArrayList<AttributeData>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                attributeList = DB.taskDao().geAllAttributeData() as ArrayList<AttributeData>
                Log.e("List AttributeData",
                    "true" + DataBaseFunctions.gson.toJson(attributeList).toString())
                callGetNos(0)
            }

        }
    }

    private fun callGetNos(i : Int) {
        GeoMapDatabase.databaseWriteExecutor.execute {
            nosList = DB.taskDao().geAllNos(attributeList[i].iD) as ArrayList<Nos>

            Log.e("List Nos", gson.toJson(nosList).toString())
            callSaveNos(i)
        }
    }

    private fun callSaveNos(i : Int) {
        var x = i
        val obj = AttributesListModel.ResponseData()
        obj.id = attributeList[i].iD
        obj.name = attributeList[i].name
        obj.nosList = ArrayList<AttributesListModel.ResponseData.Nos>()
        for (j in nosList.indices) {
            val objNos = AttributesListModel.ResponseData.Nos()
            objNos.id = nosList[j].iD
            objNos.name = nosList[j].name
            objNos.attributeId = nosList[j].attributeId
            nosModelList.add(objNos)
            obj.nosList!!.add(objNos)
        }
        attributeModelList.add(obj)
        Log.e(" response data", gson.toJson(attributeModelList).toString())
        x++
        if (x < attributeList.size) {
            Log.e(" x data", x.toString())

            callGetNos(x)
        }
    }

    class AttributesAdapter(private var dialog : Dialog,
        private var binding : ActivityUnderGroundFormFirstStepBinding,
        private val modelList : ArrayList<AttributesListModel.ResponseData>,
        private var rvList : RecyclerView, private var tvFound : TextView,
        var ctx : Context) : RecyclerView.Adapter<AttributesAdapter.MyViewHolder>(), Filterable {
        private var listFilterData : List<AttributesListModel.ResponseData> = modelList
        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : CommonPopupLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.common_popup_layout, parent, false)
            return MyViewHolder(v)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
            val mData : AttributesListModel.ResponseData = listFilterData[position]
            holder.bindingAdapter.tvName.text = mData.name
            holder.bindingAdapter.llMainLayout.setOnClickListener {
                if (attributesId != mData.id) {
                    nosId = 0
                    binding.tvNos.text = ctx.getString(R.string.select_nos)
                }
                attributesId = mData.id
                attributesName = mData.name
                binding.tvAttributeName.text = mData.name
                nosModelList = mData.nosList!!
                if (nosModelList.isEmpty()) {
                    tvFound.visibility = View.VISIBLE
                    rvList.visibility = View.GONE
                    tvFound.text = ctx.getString(R.string.no_result_found)
                } else {
                    tvFound.visibility = View.GONE
                    rvList.visibility = View.VISIBLE
                    nosAdapter = NosAdapter(dialog, binding, nosModelList, rvList, tvFound, ctx)
                    rvList.adapter = nosAdapter
                }
                dialog.dismiss()
            }

        }

        override fun getItemCount() : Int {
            return listFilterData.size
        }

        override fun getFilter() : Filter {
            return object : Filter() {
                override fun performFiltering(charSequence : CharSequence) : FilterResults {
                    val filterResults = FilterResults()
                    val charString = charSequence.toString()
                    listFilterData = if (charString.isEmpty()) {
                        modelList
                    } else {
                        val filteredList : MutableList<AttributesListModel.ResponseData> =
                            ArrayList<AttributesListModel.ResponseData>()
                        for (row in modelList) {
                            if (row.name!!.lowercase(Locale.getDefault()).contains(
                                    charString.lowercase(Locale.getDefault()))) {
                                filteredList.add(row)
                            }
                        }
                        filteredList
                    }
                    filterResults.values = listFilterData
                    return filterResults
                }

                @SuppressLint("SetTextI18n")
                override fun publishResults(charSequence : CharSequence,
                    filterResults : FilterResults) {
                    if (listFilterData.isEmpty()) {
                        tvFound.visibility = View.VISIBLE
                        tvFound.text = "Sorry we are not available in this state yet"
                        rvList.visibility = View.GONE
                    } else {
                        tvFound.visibility = View.GONE
                        rvList.visibility = View.VISIBLE
                        listFilterData =
                            filterResults.values as List<AttributesListModel.ResponseData>
                        notifyDataSetChanged()
                    }
                }
            }
        }

        inner class MyViewHolder(
            var bindingAdapter : CommonPopupLayoutBinding) : RecyclerView.ViewHolder(
            bindingAdapter.root)
    }

    class NosAdapter(private var dialog : Dialog,
        private var binding : ActivityUnderGroundFormFirstStepBinding,
        private val modelList : ArrayList<AttributesListModel.ResponseData.Nos>,
        private var rvList : RecyclerView, private var tvFound : TextView,
        var ctx : Context) : RecyclerView.Adapter<NosAdapter.MyViewHolder>(), Filterable {
        private var listFilterData : List<AttributesListModel.ResponseData.Nos> = modelList
        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : CommonPopupLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.common_popup_layout, parent, false)
            return MyViewHolder(v)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
            val mData : AttributesListModel.ResponseData.Nos = listFilterData[position]
            holder.bindingAdapter.tvName.text = mData.name
            holder.bindingAdapter.llMainLayout.setOnClickListener {
                nosId = mData.id
                binding.tvNos.text = mData.name
                nosName = mData.name
                dialog.dismiss()
            }

        }

        override fun getItemCount() : Int {
            return listFilterData.size
        }

        override fun getFilter() : Filter {
            return object : Filter() {
                override fun performFiltering(charSequence : CharSequence) : FilterResults {
                    val filterResults = FilterResults()
                    val charString = charSequence.toString()
                    listFilterData = if (charString.isEmpty()) {
                        modelList
                    } else {
                        val filteredList : MutableList<AttributesListModel.ResponseData.Nos> =
                            ArrayList<AttributesListModel.ResponseData.Nos>()
                        for (row in modelList) {
                            if (row.name!!.lowercase(Locale.getDefault()).contains(
                                    charString.lowercase(Locale.getDefault()))) {
                                filteredList.add(row)
                            }
                        }
                        filteredList
                    }
                    filterResults.values = listFilterData
                    return filterResults
                }

                @SuppressLint("SetTextI18n")
                override fun publishResults(charSequence : CharSequence,
                    filterResults : FilterResults) {
                    if (listFilterData.isEmpty()) {
                        tvFound.visibility = View.VISIBLE
                        tvFound.text = "Sorry we are not available in this state yet"
                        rvList.visibility = View.GONE
                    } else {
                        tvFound.visibility = View.GONE
                        rvList.visibility = View.VISIBLE
                        listFilterData =
                            filterResults.values as List<AttributesListModel.ResponseData.Nos>
                        notifyDataSetChanged()
                    }
                }
            }
        }

        inner class MyViewHolder(
            var bindingAdapter : CommonPopupLayoutBinding) : RecyclerView.ViewHolder(
            bindingAdapter.root)
    }

    companion object {
        var sdt = 3
        var attributesId : Int? = 0
        var attributesName : String? = ""
        var nosName : String? = ""
        var nosId : Int? = 0
        var property : String? = ""
        var attributeSearchFilter : String = ""
        var nosSearchFilter : String = ""
        var nosModelList = ArrayList<AttributesListModel.ResponseData.Nos>()
        var attributeModelList = ArrayList<AttributesListModel.ResponseData>()
        var attributeDataModelList = ArrayList<AttributeDataModel>()
        var attributesAdapter : AttributesAdapter? = null
        var nosAdapter : NosAdapter? = null
        val gson = Gson()
    }
}