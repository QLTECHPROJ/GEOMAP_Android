package com.geomap.userModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.allDisable
import com.geomap.GeoMapApp.callDashboardActivity
import com.geomap.R
import com.geomap.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignInBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    var name : String? = ""
    var password : String? = ""

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            name = binding.etName.text.toString()
            password = binding.etPassword.text.toString()

            if (name.equals("") || password.equals("")) {
                allDisable(binding.btnSignIn)
            } else {
                binding.btnSignIn.isEnabled = true
                binding.btnSignIn.setBackgroundResource(R.drawable.enable_button)
            }

            if (name!!.isNotEmpty()) {
                binding.ivUserIcon.setImageResource(R.drawable.ic_user_filled_icon)
            } else {
                binding.ivUserIcon.setImageResource(R.drawable.ic_user_unfilled_icon)
            }

            if (password!!.isNotEmpty()) {
                binding.ivLock.setImageResource(R.drawable.ic_password_filled_icon)
            } else {
                binding.ivLock.setImageResource(R.drawable.ic_password_unfilled_icon)
            }
        }

        override fun afterTextChanged(s : Editable) {}
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        ctx = this@SignInActivity
        act = this@SignInActivity

        binding.etName.addTextChangedListener(userTextWatcher)
        binding.etPassword.addTextChangedListener(userTextWatcher)

        binding.btnSignIn.setOnClickListener {
            callDashboardActivity(act, "0")
        }
    }
}