package com.geomap.userModule.activities

import android.app.*
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.*
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.geomap.BuildConfig
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivitySignInBinding
import com.geomap.mvvm.*
import com.geomap.userModule.models.*
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.*

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var ctx: Context
    private lateinit var act: Activity
    var name: String? = null
    var password: String? = null
    private lateinit var viewModel: AllViewModel
    private val retrofitService = RetrofitService.getInstance()

    private var userTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
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
                binding.ltPassword.endIconDrawable = ContextCompat.getDrawable(ctx,
                    R.drawable.visibility_state_color)
            } else {
                binding.ivLock.setImageResource(R.drawable.ic_password_unfilled_icon)
                binding.ltPassword.endIconDrawable = ContextCompat.getDrawable(ctx,
                    R.drawable.visibility_state)
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        ctx = this@SignInActivity
        act = this@SignInActivity

        binding.etName.addTextChangedListener(userTextWatcher)
        binding.etPassword.addTextChangedListener(userTextWatcher)
        binding.etPassword.transformationMethod = PasswordTransformationMethod()

        binding.tvForgotPswd.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                val dialog = Dialog(ctx)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.forgot_layout)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
                val etEmail = dialog.findViewById<TextInputEditText>(R.id.etEmail)
                val ltEmail = dialog.findViewById<TextInputLayout>(R.id.ltEmail)
                val tvGoBack = dialog.findViewById<AppCompatButton>(R.id.tvGoBack)
                val btn = dialog.findViewById<Button>(R.id.Btn)
                val progressBar = dialog.findViewById<ProgressBar>(R.id.progressBar)
                val progressBarHolder = dialog.findViewById<FrameLayout>(R.id.progressBarHolder)
                tvTitle.text = getString(R.string.forgot_password)
                dialog.setOnKeyListener { _: DialogInterface?, keyCode: Int, _: KeyEvent? ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss()
                        return@setOnKeyListener true
                    }
                    false
                }

                val userTextWatcher: TextWatcher = object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                                   after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                               count: Int) {
                        val email = etEmail.text.toString()
                        if (email == "") {
                            allDisable(btn)
                            ltEmail.isErrorEnabled = false
                        } else {
                            btn.isEnabled = true
                            btn.setBackgroundResource(R.drawable.enable_button)
                        }
                    }

                    override fun afterTextChanged(s: Editable) {}
                }

                etEmail.addTextChangedListener(userTextWatcher)

                btn.setOnClickListener {
                    if (!etEmail.text.toString().isEmailValid()) {
                        etEmail.isFocusable = true
                        etEmail.requestFocus()
                        ltEmail.isErrorEnabled = true
                        ltEmail.error = getString(R.string.pls_provide_valid_email)
                    } else {
                        ltEmail.isErrorEnabled = false
                        postForgotPassword(etEmail, dialog)
                    }
                }
                tvGoBack.setOnClickListener { dialog.dismiss() }
                dialog.show()
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(true)
            } else {
                showToast(getString(R.string.no_server_found), act)
            }

        }

        binding.btnSignIn.setOnClickListener {
//            if (isValidPassword(binding.etPassword.text.toString())) {
            fcmCall()
//            }
        }
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) {
            binding.etPassword.isFocusable = true
            binding.etPassword.requestFocus()
            binding.ltPassword.isErrorEnabled = true
            binding.ltPassword.error = getString(R.string.invalid_password_less_than_8_chars)
            return false
        }

        if (password.firstOrNull { it.isDigit() } == null || password.firstOrNull { it.isLetter() } == null || password.firstOrNull { !it.isLetterOrDigit() } == null) {
            binding.etPassword.isFocusable = true
            binding.etPassword.requestFocus()
            binding.ltPassword.isErrorEnabled = true
            binding.ltPassword.error = getString(R.string.invalid_password_not_mix_of_char_types)
            return false
        }
        return true
    }

    private fun postForgotPassword(etEmail : TextInputEditText, dialog : Dialog) {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            RetrofitService.getInstance().postForgotPassword1(etEmail.text.toString()).enqueue(object : Callback<SuccessModel1> {
                override fun onResponse(call: Call<SuccessModel1>,
                                        response: Response<SuccessModel1>) {
                    try {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        dialog.dismiss()
                        val model: SuccessModel1 = response.body()!!
                        when (model.responseCode) {
                            getString(R.string.ResponseCodesuccess) -> {
                                showToast(model.responseMessage, act)
                            }
                            getString(R.string.ResponseCodefail) -> {
                                showToast(model.responseMessage, act)
                            }
                            getString(R.string.ResponseCodeDeleted) -> {
                                callDelete403(act, model.responseMessage)
                            }
                        }
                    } catch (e: Exception) {
                        dialog.dismiss()
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<SuccessModel1>, t: Throwable) {

                    dialog.dismiss()
                    hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                }
            })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    private fun fcmCall() {
        val sharedPreferences2 = ctx.getSharedPreferences(CONSTANTS.FCMToken, MODE_PRIVATE)
        fcmId = sharedPreferences2.getString(CONSTANTS.Token, "")
        if (TextUtils.isEmpty(fcmId)) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }
                // Get new FCM registration token
                fcmId = task.result.toString()
                // Log and toast
                Log.e("newToken", fcmId)
                val editor = getContext().getSharedPreferences(CONSTANTS.FCMToken, MODE_PRIVATE)
                    .edit()
                editor.putString(CONSTANTS.Token, fcmId) //Friend
                editor.apply()
                postLoginData()
            }
        } else {
            postLoginData()
        }
    }

    private fun postLoginData() {
        showProgressBar(binding.progressBar, binding.progressBarHolder, act)
        if (isNetworkConnected(ctx)) {
            binding.ltPassword.isErrorEnabled = false
            viewModel = ViewModelProvider(this,
                UserModelFactory(UserRepository(retrofitService)))[AllViewModel::class.java]
            viewModel.postLoginData(binding.etName.text.toString(),
                binding.etPassword.text.toString(), fcmId,
                Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),
                CONSTANTS.FLAG_ONE)
            viewModel.postLoginData.observe(this) {
                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                when {
                    it?.responseCode == getString(R.string.ResponseCodesuccess) -> {
                        checkLogin = "1"
                        saveLoginData(it.responseData, ctx, "1", act)
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
}