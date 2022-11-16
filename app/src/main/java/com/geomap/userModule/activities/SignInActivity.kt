package com.geomap.userModule.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivitySignInBinding
import com.geomap.userModule.models.UserCommonDataModel
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                binding.ltPassword.endIconDrawable =
                    ContextCompat.getDrawable(ctx, R.drawable.visibility_state_color)
            } else {
                binding.ivLock.setImageResource(R.drawable.ic_password_unfilled_icon)
                binding.ltPassword.endIconDrawable =
                    ContextCompat.getDrawable(ctx, R.drawable.visibility_state)
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
        binding.etPassword.transformationMethod = PasswordTransformationMethod()

        binding.btnSignIn.setOnClickListener {
            if (isValidPassword(binding.etPassword.text.toString())) {
                postLoginData()
            }
        }
    }

    private fun isValidPassword(password : String) : Boolean {
        if (password.length < 8) {
            binding.etPassword.isFocusable = true
            binding.etPassword.requestFocus()
            binding.ltPassword.isErrorEnabled = true
            binding.ltPassword.error = getString(R.string.invalid_password_less_than_8_chars)
            return false
        }

        if (password.filter { it.isDigit() }
                .firstOrNull() == null || password.filter { it.isLetter() }
                .firstOrNull() == null || password.filter { !it.isLetterOrDigit() }
                .firstOrNull() == null) {
            binding.etPassword.isFocusable = true
            binding.etPassword.requestFocus()
            binding.ltPassword.isErrorEnabled = true
            binding.ltPassword.error = getString(R.string.invalid_password_not_mix_of_char_types)
            return false
        }
        return true
    }

    private fun postLoginData() {
        if (isNetworkConnected(ctx)) {
            binding.ltPassword.isErrorEnabled = false
            fcmId = getSharedPreferences(CONSTANTS.FCMToken, Context.MODE_PRIVATE).getString(
                CONSTANTS.Token, "").toString()
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            RetrofitService.getInstance()
                .postLoginData(binding.etName.text.toString(), binding.etPassword.text.toString(),
                    fcmId,
                    Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),
                    CONSTANTS.FLAG_ONE)
                .enqueue(object : Callback<UserCommonDataModel> {
                    override fun onResponse(call : Call<UserCommonDataModel>,
                        response : Response<UserCommonDataModel>) {
                        try {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            val model : UserCommonDataModel? = response.body()!!
                            when (model!!.responseCode) {
                                getString(R.string.ResponseCodesuccess) -> {
                                    saveLoginData(model.responseData, ctx, "1", act)
                                }
                                getString(R.string.ResponseCodefail) -> {
                                    showToast(model.responseMessage, act)
                                }
                                getString(R.string.ResponseCodeDeleted) -> {
                                    callDelete403(act, model.responseMessage)
                                }
                            }
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call : Call<UserCommonDataModel>, t : Throwable) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                    }
                })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

}