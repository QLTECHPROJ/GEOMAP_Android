package com.geomap.mapReportModule.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUnderGroundFormFirstStepBinding
import com.geomap.databinding.CommonPopupLayoutBinding
import com.geomap.mapReportModule.models.AttributesListModel
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UnderGroundFormFirstStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUnderGroundFormFirstStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    var desc : String? = ""
    var attributeSearchFilter : String = ""
    private var attributesAdapter : AttributesAdapter? = null
    private var nosAdapter : NosAdapter? = null

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            desc = binding.edtDescription.text.toString()

            if (desc.equals("")) {
                allDisable(binding.btnNextStep)
            } else {
                binding.btnNextStep.isEnabled = true
                binding.btnNextStep.setBackgroundResource(R.drawable.enable_button)
            }
        }

        override fun afterTextChanged(s : Editable) {}
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_under_ground_form_first_step)
        ctx = this@UnderGroundFormFirstStepActivity
        act = this@UnderGroundFormFirstStepActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.edtDescription.addTextChangedListener(userTextWatcher)

        binding.cvAttributes.setOnClickListener {
            callPopupList(getString(R.string.choose_your_attributes),
                getString(R.string.pls_select_your_attributes), "1")
        }

        binding.cvNos.setOnClickListener {
            callPopupList(getString(R.string.choose_your_nos),
                getString(R.string.pls_select_your_nos), "1")
        }

        binding.tvAddAttributes.setOnClickListener {
            binding.llMainLayout.visibility = View.VISIBLE
        }

        binding.btnNextStep.setOnClickListener {
            callUnderGroundFormSecondStepActivity(act, "0")
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun callPopupList(title : String, searchHint : String, keyS : String) {
        val dialog = Dialog(ctx)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.common_list_layout)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tvTilte : TextView = dialog.findViewById(R.id.tvTilte)
        val rvStateList : RecyclerView = dialog.findViewById(R.id.rvStateList)
        val searchView : SearchView = dialog.findViewById(R.id.searchView)
        val tvFound : TextView = dialog.findViewById(R.id.tvFound)
        val pb : ProgressBar = dialog.findViewById(R.id.progressBar)
        val pbh : FrameLayout = dialog.findViewById(R.id.progressBarHolder)
        tvTilte.text = title
        rvStateList.visibility = View.VISIBLE
        rvStateList.layoutManager = LinearLayoutManager(ctx)
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
        searchEditText.hint = searchHint

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(search : String) : Boolean {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                return false
            }

            override fun onQueryTextChange(search : String) : Boolean {
                try {
                    when (keyS) {
                        "1" -> {
                            attributesAdapter?.filter?.filter(search)
                            attributeSearchFilter = search
                        }
//                        "2" -> {
//                            cityAdapter?.filter?.filter(search)
//                            citysearchFilter = search
//                        }
//                        "3" -> {
//                            sportAdapter?.filter?.filter(search)
//                            sportsearchFilter = search
//                        }
//                        "4" -> {
//                            roleAdapter?.filter?.filter(search)
//                            rolesearchFilter = search
//                        }
                    }
                } catch (e : java.lang.Exception) {
                    e.printStackTrace()
                }
                return false
            }
        })

        when (keyS) {
            "1" -> {
                prepareAttributesListing(dialog, rvStateList, tvFound, pb, pbh, searchView)
            }
            "2" -> {
                prepareNosListing(dialog, rvStateList, tvFound, pb, pbh, searchView)
            }
            /*
            "3" -> {
                prepareSportListing(dialog, rvStateList, tvFound, pb, pbh, searchView)
            }
            "4" -> {
                prepareRoleListing(dialog, rvStateList, tvFound, pb, pbh, searchView)
            }*/
        }
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
    }

    private fun prepareAttributesListing(dialog : Dialog, rvStateList : RecyclerView,
        tvFound : TextView,
        progressBar : ProgressBar, progressBarHolder : FrameLayout, searchView : SearchView) {
        if (isNetworkConnected(ctx)) {
            showProgressBar(progressBar, progressBarHolder, act)
            searchView.isEnabled = false
            searchView.isClickable = false
            RetrofitService.getInstance().getAttributesList.enqueue(object :
                Callback<AttributesListModel> {
                override fun onResponse(call : Call<AttributesListModel>,
                    response : Response<AttributesListModel>) {
                    val model = response.body()
                    hideProgressBar(progressBar, progressBarHolder, act)
                    when {
                        model!!.responseCode!! == getString(R.string.ResponseCodesuccess) -> {
                            searchView.isEnabled = true
                            searchView.isClickable = true
                            rvStateList.layoutManager = LinearLayoutManager(ctx)
                            attributesAdapter = AttributesAdapter(dialog, binding,
                                model.responseData!!, rvStateList, tvFound)
                            rvStateList.adapter = attributesAdapter
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
            showToast(getString(R.string.no_server_found), act)
        }
    }

    private fun prepareNosListing(dialog : Dialog, rvStateList : RecyclerView,
        tvFound : TextView,
        progressBar : ProgressBar, progressBarHolder : FrameLayout, searchView : SearchView) {
        if (isNetworkConnected(ctx)) {
            showProgressBar(progressBar, progressBarHolder, act)
            searchView.isEnabled = false
            searchView.isClickable = false
            RetrofitService.getInstance().getAttributesList.enqueue(object :
                Callback<AttributesListModel> {
                override fun onResponse(call : Call<AttributesListModel>,
                    response : Response<AttributesListModel>) {
                    val model = response.body()
                    hideProgressBar(progressBar, progressBarHolder, act)
                    when {
                        model!!.responseCode!! == getString(R.string.ResponseCodesuccess) -> {
                            searchView.isEnabled = true
                            searchView.isClickable = true
                            rvStateList.layoutManager = LinearLayoutManager(ctx)
//                            nosAdapter = NosAdapter(dialog, binding,
//                                model.responseData.nosList!!, rvStateList, tvFound)
//                            rvStateList.adapter = nosAdapter
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
            showToast(getString(R.string.no_server_found), act)
        }
    }

    class AttributesAdapter(private var dialog : Dialog,
        private var binding : ActivityUnderGroundFormFirstStepBinding,
        private val modelList : List<AttributesListModel.ResponseData>,
        private var rvCountryList : RecyclerView,
        private var tvFound : TextView) : RecyclerView.Adapter<AttributesAdapter.MyViewHolder>(),
        Filterable {
        private var listFilterData : List<AttributesListModel.ResponseData> = modelList
        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : CommonPopupLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.common_popup_layout, parent, false)
            return MyViewHolder(v)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
            val mData : AttributesListModel.ResponseData = listFilterData[position]
            holder.bindingAdapter.tvCountryName.text = mData.name
            holder.bindingAdapter.llMainLayout.setOnClickListener {
                if (attributesId != mData.id) {
                    binding.tvHintMineralization.text = ""
                    binding.tvHintMineralization.text = ""
                }
                attributesId = mData.id
                binding.tvMineralization.text = mData.name
                binding.tvHintMineralization.text = mData.id.toString()
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
                        rvCountryList.visibility = View.GONE
                    } else {
                        tvFound.visibility = View.GONE
                        rvCountryList.visibility = View.VISIBLE
                        listFilterData = filterResults.values as List<AttributesListModel.ResponseData>
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
        private val modelList : List<AttributesListModel.ResponseData.Nos>,
        private var rvCountryList : RecyclerView,
        private var tvFound : TextView) : RecyclerView.Adapter<NosAdapter.MyViewHolder>(),
        Filterable {

        private var listFilterData : List<AttributesListModel.ResponseData.Nos> = modelList
        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : CommonPopupLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.common_popup_layout, parent, false)
            return MyViewHolder(v)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
            val mData : AttributesListModel.ResponseData.Nos = listFilterData[position]
            holder.bindingAdapter.tvCountryName.text = mData.name
            holder.bindingAdapter.llMainLayout.setOnClickListener {
                if (nosId != mData.id) {
                    binding.tvHintMineralization.text = ""
                    binding.tvHintMineralization.text = ""
                }
                nosId = mData.id
                binding.tvMineralization.text = mData.name
                binding.tvHintMineralization.text = mData.id.toString()
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
                        rvCountryList.visibility = View.GONE
                    } else {
                        tvFound.visibility = View.GONE
                        rvCountryList.visibility = View.VISIBLE
                        listFilterData = filterResults.values as List<AttributesListModel.ResponseData.Nos>
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
        var attributesId : Int? = 0
        var nosId : Int? = 0
    }
}