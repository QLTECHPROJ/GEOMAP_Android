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

class ContactUsActivity : AppCompatActivity() {
    lateinit var binding : ActivityContactUsBinding
    lateinit var act : Activity
    lateinit var ctx : Context
    var name : String? = ""
    var email : String? = ""
    var subject : String? = ""
    var message : String? = ""

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            name = binding.etName.text.toString()
            email = binding.etEmail.text.toString()
            subject = binding.etSubject.text.toString()
            message = binding.etMessage.text.toString()

            when {
                name.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnSubmit)
                }

                email.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnSubmit)
                }

                subject.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnSubmit)
                }

                message.equals("", ignoreCase = true) -> {
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

        binding.etName.addTextChangedListener(userTextWatcher)
        binding.etEmail.addTextChangedListener(userTextWatcher)
        binding.etSubject.addTextChangedListener(userTextWatcher)
        binding.etMessage.addTextChangedListener(userTextWatcher)

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {
            finish()
        }
    }

    private fun enableButton() {
        binding.btnSubmit.isEnabled = true
        binding.btnSubmit.setBackgroundResource(R.drawable.enable_button)
    }

    override fun onBackPressed() {
        finish()
    }
}