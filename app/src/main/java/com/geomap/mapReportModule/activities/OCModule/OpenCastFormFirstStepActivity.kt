package com.geomap.mapReportModule.activities.OCModule

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.geomap.databinding.ActivityOpenCastFormFirstStepBinding
import com.geomap.databinding.CommonPopupLayoutBinding
import com.geomap.mapReportModule.models.CommonPopupListModel
import com.geomap.mapReportModule.models.OpenCastDetailsModel
import com.geomap.mapReportModule.models.OpenCastInsertModel
import com.geomap.utils.RetrofitService
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OpenCastFormFirstStepActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpenCastFormFirstStepBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    var minesSiteName : String? = null
    var pitName : String? = null
    var pitLocation : String? = null
    var shiftInchargeName : String? = null
    var geologistName : String? = null
    var faceLocation : String? = null
    var faceLengthM : String? = null
    var faceAreaM2 : String? = null
    var faceRockTypes : String? = null
    var benchRL : String? = null
    var benchHeightWidth : String? = null
    var benchAngle : String? = null
    var dipDirectionAngle : String? = null
    var thicknessOfOre : String? = null
    var thinessOfOverburden : String? = null
    var thicknessOfInterburden : String? = null
    var observedGradeOfOre : String? = null
    var actualGradeOfOre : String? = null
    var notes : String? = null
    var searchFilter : String = ""
    private var popupAdapter : PopupAdapter? = null

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            minesSiteName = binding.etMinesSiteName.text.toString()
            pitName = binding.etPitName.text.toString()
            pitLocation = binding.etPitLocation.text.toString()
            shiftInchargeName = binding.etShiftInchargeName.text.toString()
            geologistName = binding.etGeologistName.text.toString()
            faceLocation = binding.etFaceLocation.text.toString()
            faceLengthM = binding.etFaceLengthM.text.toString()
            faceAreaM2 = binding.etFaceAreaM2.text.toString()
            faceRockTypes = binding.etFaceRockTypes.text.toString()
            benchRL = binding.etBenchRL.text.toString()
            benchHeightWidth = binding.etBenchHeightWidth.text.toString()
            benchAngle = binding.etBenchAngle.text.toString()
            dipDirectionAngle = binding.etDipDirectionAngle.text.toString()
            thicknessOfOre = binding.etThicknessOfOre.text.toString()
            thinessOfOverburden = binding.etThinessOfOverburden.text.toString()
            thicknessOfInterburden = binding.etThicknessOfInterburden.text.toString()
            observedGradeOfOre = binding.etObservedGradeOfOre.text.toString()
            actualGradeOfOre = binding.etActualGradeOfOre.text.toString()
            notes = binding.edtNotes.text.toString()

            when {
               /* minesSiteName.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                pitName.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                pitLocation.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                shiftInchargeName.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                geologistName.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                faceLocation.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                faceLengthM.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                faceAreaM2.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                faceRockTypes.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                benchRL.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                benchHeightWidth.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                benchAngle.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                dipDirectionAngle.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                thicknessOfOre.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                thinessOfOverburden.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                thicknessOfInterburden.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                observedGradeOfOre.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                actualGradeOfOre.equals("") -> {
                    allDisable(binding.btnSubmit)
                }
                notes.equals("") -> {
                    allDisable(binding.btnSubmit)
                }*/

                else -> {
                    binding.btnSubmit.isEnabled = true
                    binding.btnSubmit.setBackgroundResource(R.drawable.enable_button)
                }
            }
        }

        override fun afterTextChanged(s : Editable) {}
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_cast_form_first_step)
        ctx = this@OpenCastFormFirstStepActivity
        act = this@OpenCastFormFirstStepActivity

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.etMinesSiteName.addTextChangedListener(userTextWatcher)
        binding.etPitName.addTextChangedListener(userTextWatcher)
        binding.etPitLocation.addTextChangedListener(userTextWatcher)
        binding.etShiftInchargeName.addTextChangedListener(userTextWatcher)
        binding.etGeologistName.addTextChangedListener(userTextWatcher)
        binding.etFaceLocation.addTextChangedListener(userTextWatcher)
        binding.etFaceLengthM.addTextChangedListener(userTextWatcher)
        binding.etFaceAreaM2.addTextChangedListener(userTextWatcher)
        binding.etFaceRockTypes.addTextChangedListener(userTextWatcher)
        binding.etFaceLocation.addTextChangedListener(userTextWatcher)
        binding.etBenchRL.addTextChangedListener(userTextWatcher)
        binding.etBenchHeightWidth.addTextChangedListener(userTextWatcher)
        binding.etBenchAngle.addTextChangedListener(userTextWatcher)
        binding.etDipDirectionAngle.addTextChangedListener(userTextWatcher)
        binding.etThicknessOfOre.addTextChangedListener(userTextWatcher)
        binding.etThinessOfOverburden.addTextChangedListener(userTextWatcher)
        binding.etThicknessOfInterburden.addTextChangedListener(userTextWatcher)
        binding.etObservedGradeOfOre.addTextChangedListener(userTextWatcher)
        binding.etActualGradeOfOre.addTextChangedListener(userTextWatcher)
        binding.edtNotes.addTextChangedListener(userTextWatcher)

        binding.cvSampleCollected.setOnClickListener {
            callPopupList(getString(R.string.choose_your_sample_collected),
                getString(R.string.pls_select_your_sample_collected), "1")
        }

        binding.cvWeathering.setOnClickListener {
            callPopupList(getString(R.string.choose_your_weathering),
                getString(R.string.pls_select_your_weathering), "2")
        }

        binding.cvRockStrength.setOnClickListener {
            callPopupList(getString(R.string.choose_your_rock_strength),
                getString(R.string.pls_select_your_rock_strength), "3")
        }

        binding.cvWaterCondition.setOnClickListener {
            callPopupList(getString(R.string.choose_your_water_condition),
                getString(R.string.pls_select_your_water_condition), "4")
        }

        binding.cvTypeOfGeologicalStructures.setOnClickListener {
            callPopupList(getString(R.string.choose_your_type_of_geological_structures),
                getString(R.string.pls_select_your_type_of_geological_structures), "5")
        }

        binding.cvTypeOfFaults.setOnClickListener {
            callPopupList(getString(R.string.choose_your_Type_of_faults),
                getString(R.string.pls_select_your_Type_of_faults), "6")
        }

        binding.btnSubmit.setOnClickListener {
//            callOpenCastFormSecondStepActivity(act, "0")
            var gson = Gson()
            var oc = OpenCastInsertModel()
            oc.minesSiteName = binding.etMinesSiteName.text.toString()

            val i = Intent(act, OpenCastFormSecondStepActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            i.putExtra("ocData",gson.toJson(oc))
            act.startActivity(i)
            act.finish()
        }

        binding.btnGeologistSignPadClear.setOnClickListener {
            binding.geologistSignPad.clear()
        }

        binding.btnGeologistClientSignPadClear.setOnClickListener {
            binding.geologistClientSignPad.clear()
        }

        binding.geologistSignPad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {

            }

            override fun onSigned() {
                binding.btnGeologistSignPadClear.isEnabled = true
                binding.btnGeologistSignPadClear.setBackgroundResource(R.drawable.enable_button)
            }

            override fun onClear() {
                binding.btnGeologistSignPadClear.isEnabled = false
                binding.btnGeologistSignPadClear.setBackgroundResource(R.drawable.disable_button)
            }
        })

        binding.geologistClientSignPad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {

            }

            override fun onSigned() {
                binding.btnGeologistClientSignPadClear.isEnabled = true
                binding.btnGeologistClientSignPadClear.setBackgroundResource(
                    R.drawable.enable_button)

            }

            override fun onClear() {
                binding.btnGeologistClientSignPadClear.isEnabled = false
                binding.btnGeologistClientSignPadClear.setBackgroundResource(
                    R.drawable.disable_button)
            }
        })
    }

    private fun callPopupList(title : String, searchHint : String, keyS : String) {
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
        tvTilte.text = title
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
                            popupAdapter?.filter?.filter(search)
                            searchFilter = search
                        }
                        "2" -> {
                            popupAdapter?.filter?.filter(search)
                            searchFilter = search
                        }
                        "3" -> {
                            popupAdapter?.filter?.filter(search)
                            searchFilter = search
                        }
                        "4" -> {
                            popupAdapter?.filter?.filter(search)
                            searchFilter = search
                        }
                        "5" -> {
                            popupAdapter?.filter?.filter(search)
                            searchFilter = search
                        }
                        "6" -> {
                            popupAdapter?.filter?.filter(search)
                            searchFilter = search
                        }
                    }
                } catch (e : java.lang.Exception) {
                    e.printStackTrace()
                }
                return false
            }
        })
        prepareAttributesListing(dialog, rvList, tvFound, pb, pbh, searchView, keyS)
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
    }

    private fun prepareAttributesListing(dialog : Dialog, rvList : RecyclerView,
        tvFound : TextView,
        progressBar : ProgressBar, progressBarHolder : FrameLayout, searchView : SearchView,
        keyS : String) {
        if (isNetworkConnected(ctx)) {
            showProgressBar(progressBar, progressBarHolder, act)
            searchView.isEnabled = false
            searchView.isClickable = false
            var serivce : Call<CommonPopupListModel>? = null
            when (keyS) {
                "1" -> {
                    serivce = RetrofitService.getInstance().getSampleCollectedsList
                }
                "2" -> {
                    serivce = RetrofitService.getInstance().getWeatheringDataList
                }
                "3" -> {
                    serivce = RetrofitService.getInstance().getRockStrengthDataList
                }
                "4" -> {
                    serivce = RetrofitService.getInstance().getWaterConditionDataList
                }
                "5" -> {
                    serivce = RetrofitService.getInstance().getTypeOfGeologicalStructuresList
                }
                "6" -> {
                    serivce = RetrofitService.getInstance().getTypeOfFaultList
                }
            }

            serivce?.enqueue(object :
                Callback<CommonPopupListModel> {
                override fun onResponse(call : Call<CommonPopupListModel>,
                    response : Response<CommonPopupListModel>) {
                    val model = response.body()
                    hideProgressBar(progressBar, progressBarHolder, act)
                    when {
                        model!!.responseCode!! == getString(R.string.ResponseCodesuccess) -> {
                            searchView.isEnabled = true
                            searchView.isClickable = true
                            rvList.layoutManager = LinearLayoutManager(ctx)
                            popupAdapter =
                                PopupAdapter(dialog, binding,
                                    model.responseData!!, rvList, tvFound, keyS)
                            rvList.adapter = popupAdapter
                        }
                        model.responseCode!! == getString(R.string.ResponseCodefail) -> {
                            showToast(model.responseMessage, act)
                        }
                        model.responseCode!! == getString(R.string.ResponseCodeDeleted) -> {
                            callDelete403(act, model.responseMessage)
                        }
                    }

                }

                override fun onFailure(call : Call<CommonPopupListModel>, t : Throwable) {
                    hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                }
            })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    class PopupAdapter(private var dialog : Dialog,
        private var binding : ActivityOpenCastFormFirstStepBinding,
        private val modelList : List<CommonPopupListModel.ResponseData>,
        private var rvCountryList : RecyclerView,
        private var tvFound : TextView, private var keyS : String) :
        RecyclerView.Adapter<PopupAdapter.MyViewHolder>(),
        Filterable {
        private var listFilterData : List<CommonPopupListModel.ResponseData> = modelList
        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MyViewHolder {
            val v : CommonPopupLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.common_popup_layout, parent, false)
            return MyViewHolder(v)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
            val mData : CommonPopupListModel.ResponseData = listFilterData[position]
            holder.bindingAdapter.tvName.text = mData.name
            holder.bindingAdapter.llMainLayout.setOnClickListener {
                when (keyS) {
                    "1" -> {
                        if (sampleCollectedId != mData.id) {
                            binding.tvHintSampleCollected.text = ""
                            binding.tvHintSampleCollected.text = ""
                        }
                        sampleCollectedId = mData.id
                        binding.tvSampleCollected.text = mData.name
                        binding.tvHintSampleCollected.text = mData.id.toString()
                    }
                    "2" -> {
                        if (weatheringId != mData.id) {
                            binding.tvHintWeathering.text = ""
                            binding.tvHintWeathering.text = ""
                        }
                        weatheringId = mData.id
                        binding.tvWeathering.text = mData.name
                        binding.tvHintWeathering.text = mData.id.toString()
                    }
                    "3" -> {
                        if (rockStrengthId != mData.id) {
                            binding.tvHintRockStrength.text = ""
                            binding.tvHintRockStrength.text = ""
                        }
                        rockStrengthId = mData.id
                        binding.tvRockStrength.text = mData.name
                        binding.tvHintRockStrength.text = mData.id.toString()
                    }
                    "4" -> {
                        if (waterConditionId != mData.id) {
                            binding.tvHintWaterCondition.text = ""
                            binding.tvHintWaterCondition.text = ""
                        }
                        waterConditionId = mData.id
                        binding.tvWaterCondition.text = mData.name
                        binding.tvHintWaterCondition.text = mData.id.toString()
                    }
                    "5" -> {
                        if (typeOfGeologicalStructuresId != mData.id) {
                            binding.tvHintTypeOfGeologicalStructures.text = ""
                            binding.tvHintTypeOfGeologicalStructures.text = ""
                        }
                        typeOfGeologicalStructuresId = mData.id
                        binding.tvTypeOfGeologicalStructures.text = mData.name
                        binding.tvHintTypeOfGeologicalStructures.text = mData.id.toString()
                    }
                    "6" -> {
                        if (typeOfFaultsId != mData.id) {
                            binding.tvHintTypeOfFaults.text = ""
                            binding.tvHintTypeOfFaults.text = ""
                        }
                        typeOfFaultsId = mData.id
                        binding.tvTypeOfFaults.text = mData.name
                        binding.tvHintTypeOfFaults.text = mData.id.toString()
                    }
                }

                Log.e("1", sampleCollectedId.toString())
                Log.e("2", weatheringId.toString())
                Log.e("3", rockStrengthId.toString())
                Log.e("4", waterConditionId.toString())
                Log.e("5", typeOfGeologicalStructuresId.toString())
                Log.e("6", typeOfFaultsId.toString())
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
                        val filteredList : MutableList<CommonPopupListModel.ResponseData> =
                            ArrayList<CommonPopupListModel.ResponseData>()
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
                        listFilterData =
                            filterResults.values as List<CommonPopupListModel.ResponseData>
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
        var sampleCollectedId : String? = null
        var weatheringId : String? = null
        var rockStrengthId : String? = null
        var waterConditionId : String? = null
        var typeOfGeologicalStructuresId : String? = null
        var typeOfFaultsId : String? = null
    }

    override fun onBackPressed() {
        finish()
    }
}