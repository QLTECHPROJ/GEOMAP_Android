package com.geomap.userModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityContactUsBinding
import com.geomap.userModule.models.ContactUsModel
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : AppCompatActivity() {
    lateinit var binding : ActivityContactUsBinding
    lateinit var act : Activity
    lateinit var ctx : Context
    private var userId : String? = null
    var name : String? = ""
    var mobileNo : String? = ""
    var email : String? = ""
    var subject : String? = ""
    var message : String? = ""

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            name = binding.etName.text.toString()
            mobileNo = binding.etMobileNo.text.toString()
            email = binding.etEmail.text.toString()
            subject = binding.etSubject.text.toString()
            message = binding.etMessage.text.toString()

            when {
                name.equals("") -> {
                    allDisable(binding.btnSubmit)
                }

                mobileNo.equals("") -> {
                    allDisable(binding.btnSubmit)
                }

                email.equals("") -> {
                    allDisable(binding.btnSubmit)
                }

                subject.equals("") -> {
                    allDisable(binding.btnSubmit)
                }

                message.equals("") -> {
                    allDisable(binding.btnSubmit)
                }

                else -> {
                    enableButton()
                }
            }
        }

        override fun afterTextChanged(s : Editable) {}
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)
        act = this@ContactUsActivity
        ctx = this@ContactUsActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        binding.etName.addTextChangedListener(userTextWatcher)
        binding.etMobileNo.addTextChangedListener(userTextWatcher)
        binding.etEmail.addTextChangedListener(userTextWatcher)
        binding.etSubject.addTextChangedListener(userTextWatcher)
        binding.etMessage.addTextChangedListener(userTextWatcher)

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {
            postContactUs()
        }
    }

    private fun enableButton() {
        binding.btnSubmit.isEnabled = true
        binding.btnSubmit.setBackgroundResource(R.drawable.enable_button)
    }

    private fun postContactUs() {
        if (isNetworkConnected(ctx)) {
            RetrofitService.getInstance().postContactUs(
                userId, binding.etName.text.toString(), binding.etMobileNo.text.toString(),
                binding.etEmail.text.toString(), binding.etSubject.text.toString(),
                binding.etMessage.text.toString()).enqueue(object : Callback<ContactUsModel?> {
                override fun onResponse(call : Call<ContactUsModel?>,
                    response : Response<ContactUsModel?>) {
                    val model = response.body()
                    if (model!!.responseCode.equals(
                            getString(R.string.ResponseCodesuccess))) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        showToast(model.responseMessage, act)
                        finish()
                    } else if (model.responseCode.equals(
                            ctx.getString(R.string.ResponseCodefail))) {
                        showToast(model.responseMessage, act)
                    } else if (model.responseCode.equals(
                            ctx.getString(R.string.ResponseCodeDeleted))) {
                        callDelete403(act, model.responseMessage)
                    }
                }

                override fun onFailure(call : Call<ContactUsModel?>, t : Throwable) {
                    hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                }
            })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}