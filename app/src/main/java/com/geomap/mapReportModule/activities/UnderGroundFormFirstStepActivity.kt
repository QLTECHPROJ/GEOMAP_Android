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
            val dialog = Dialog(ctx)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.common_list_layout)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val tvTilte : TextView = dialog.findViewById(R.id.tvTilte)
            val rvList : RecyclerView = dialog.findViewById(R.id.rvList)
            val searchView : SearchView = dialog.findViewById(R.id.searchView)
            val tvFound : TextView = dialog.findViewById(R.id.tvFound)
            val pb : ProgressBar = dialog.findViewById(R.id.progressBar)
            val pbh : FrameLayout = dialog.findViewById(R.id.progressBarHolder)
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
            prepareAttributesListing(dialog, rvList, tvFound, pb, pbh, searchView)
            dialog.show()
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
        }

        binding.cvNos.setOnClickListener {
            val dialog = Dialog(ctx)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.common_list_layout)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val tvTilte : TextView = dialog.findViewById(R.id.tvTilte)
            val rvList : RecyclerView = dialog.findViewById(R.id.rvList)
            val searchView : SearchView = dialog.findViewById(R.id.searchView)
            val tvFound : TextView = dialog.findViewById(R.id.tvFound)
            val pb : ProgressBar = dialog.findViewById(R.id.progressBar)
            val pbh : FrameLayout = dialog.findViewById(R.id.progressBarHolder)
            tvTilte.text = getString(R.string.choose_your_nos)
            rvList.visibility = View.VISIBLE
            rvList.layoutManager = LinearLayoutManager(ctx)

            nosAdapter = NosAdapter(dialog, binding,
                nosModel, rvList, tvFound)
            rvList.adapter = nosAdapter
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

            dialog.show()
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
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

    private fun prepareAttributesListing(dialog : Dialog, rvList : RecyclerView,
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
                            rvList.layoutManager = LinearLayoutManager(ctx)
                            attributesAdapter = AttributesAdapter(dialog, binding,
                                model.responseData!!, rvList, tvFound)
                            rvList.adapter = attributesAdapter
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
        private var rvList : RecyclerView,
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
            holder.bindingAdapter.tvName.text = mData.name
            holder.bindingAdapter.llMainLayout.setOnClickListener {
                if (attributesId != mData.id) {
                    binding.tvHintMineralization.text = ""
                    binding.tvHintMineralization.text = ""
                }
                attributesId = mData.id
                binding.tvMineralization.text = mData.name
                binding.tvHintMineralization.text = mData.id.toString()
                nosModel = mData.nosList!!
                nosAdapter?.notifyDataSetChanged()
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

    class NosAdapter(
        private var dialog : Dialog,
        private var binding : ActivityUnderGroundFormFirstStepBinding,
        private val modelList : List<AttributesListModel.ResponseData.Nos>,
        private var rvList : RecyclerView,
        private var tvFound : TextView
    ) : RecyclerView.Adapter<NosAdapter.MyViewHolder>(),
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
            holder.bindingAdapter.tvName.text = mData.name
            holder.bindingAdapter.llMainLayout.setOnClickListener {
                if (nosId != mData.id) {
                    binding.tvHintMineralizationOne.text = ""
                    binding.tvHintMineralizationOne.text = ""
                }
                nosId = mData.id
                binding.tvMineralizationOne.text = mData.name
                binding.tvHintMineralizationOne.text = mData.id.toString()
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
        var nosId : Int? = 0
        var desc : String? = ""
        var attributeSearchFilter : String = ""
        var nosSearchFilter : String = ""
        lateinit var nosModel : List<AttributesListModel.ResponseData.Nos>
        lateinit var rvList : RecyclerView
        var attributesAdapter : AttributesAdapter? = null
        var nosAdapter : NosAdapter? = null
    }
}