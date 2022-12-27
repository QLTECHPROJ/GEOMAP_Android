package com.geomap.userModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityContactUsBinding
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService

class ContactUsActivity : AppCompatActivity() {
    lateinit var binding : ActivityContactUsBinding
    lateinit var act : Activity
    lateinit var ctx : Context
    private var userId : String? = null
    private lateinit var viewModel : AllViewModel
    private val retrofitService = RetrofitService.getInstance()
    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            val name = binding.etName.text.toString()
            val mobileNo = binding.etMobileNo.text.toString()
            val email = binding.etEmail.text.toString()
            val subject = binding.etSubject.text.toString()
            val message = binding.etMessage.text.toString()

            when {
                name == "" -> {
                    allDisable(binding.btnSubmit)
                }

                mobileNo.length < 4 -> {
                    allDisable(binding.btnSubmit)
                }

                email == "" -> {
                    allDisable(binding.btnSubmit)
                }

                subject == "" -> {
                    allDisable(binding.btnSubmit)
                }

                message == "" -> {
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
            if (!binding.etEmail.text.toString().isEmailValid()) {
                binding.etEmail.isFocusable = true
                binding.etEmail.requestFocus()
                binding.ltEmail.isErrorEnabled = true
                binding.ltEmail.error = getString(R.string.pls_provide_valid_email)
            } else {
                postContactUs()
            }
        }
    }

    private fun enableButton() {
        binding.btnSubmit.isEnabled = true
        binding.btnSubmit.setBackgroundResource(R.drawable.enable_button)
    }

    private fun String.isEmailValid() : Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
                .matches()
    }

    private fun postContactUs() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            viewModel = ViewModelProvider(
                this, UserModelFactory(
                    UserRepository(retrofitService)
                )
            )[AllViewModel::class.java]
            viewModel.postContactUs(
                userId!!, binding.etName.text.toString(), binding.etMobileNo.text.toString(),
                binding.etEmail.text.toString(), binding.etSubject.text.toString(),
                binding.etMessage.text.toString()
            )
            viewModel.postContactUs.observe(this) {
                hideProgressBar(
                    binding.progressBar,
                    binding.progressBarHolder, act
                )
                when {
                    it?.responseCode == getString(R.string.ResponseCodesuccess) -> {
                        showToast(it.responseMessage, act)
                        finish()
                    }
                    it.responseCode == act.getString(R.string.ResponseCodefail) -> {
                        showToast(it.responseMessage, act)
                    }
                    it.responseCode == act.getString(R.string.ResponseCodeDeleted) -> {
                        callDelete403(act, it.responseMessage)
                    }
                }
            }
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}