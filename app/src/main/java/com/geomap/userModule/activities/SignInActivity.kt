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
import com.geomap.databinding.ActivitySignInBinding
import com.geomap.userModule.models.LoginModel
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
            postLoginData()
        }
    }

    private fun postLoginData() {
        callDashboardActivity(act, "0")
/*        if (isNetworkConnected(ctx)) {
            RetrofitService.getInstance()
                .postLoginData("id",binding.etName.text.toString(), binding.etPassword.text.toString())
                .enqueue(object : Callback<LoginModel> {
                    override fun onResponse(call : Call<LoginModel>,
                        response : Response<LoginModel>) {
                        try {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                            val versionModel : LoginModel? = response.body()!!
                            when (versionModel!!.responseCode) {
                                getString(R.string.ResponseCodesuccess) -> {
                                    callDashboardActivity(act, "0")
*//*
                                    when (versionModel.responseData?.loginFlag) {
                                        "1" -> {
                                            sendVerificationCode(
                                                binding.tvCountry.text.toString() + " " + binding.etNumber.text.toString())
                                        }
                                        "0" -> {
                                            callSignUpActivity(binding.etNumber.text.toString(),
                                                act)
                                        }
                                        else -> {
                                            callSignUpActivity(binding.etNumber.text.toString(),
                                                act)
                                        }
                                    }
*//*
                                }
                                getString(R.string.ResponseCodefail) -> {
                                    showToast(versionModel.responseMessage, act)
                                }
                                getString(R.string.ResponseCodeDeleted) -> {
                                    callDelete403(act, versionModel.responseMessage)
                                }
                            }
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call : Call<LoginModel>, t : Throwable) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                    }
                })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }*/
    }
}