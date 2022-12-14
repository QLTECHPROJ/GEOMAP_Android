package com.geomap.userModule.activities

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityMenuListBinding
import com.geomap.userModule.models.ConfirmSuccessModel
import com.geomap.userModule.models.UserCommonDataModel
import com.geomap.utils.CONSTANTS
import com.geomap.utils.RetrofitService
import com.geomap.webView.TncActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMenuListBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var userId : String? = null
    private var supportDialog : Dialog? = null
    private var supportTitle : String? = null
    private var supportText : String? = null
    private var supportEmail : String? = null
    private var logoutDialog : Dialog? = null
    val shared : SharedPreferences? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_list)
        ctx = this@MenuListActivity
        act = this@MenuListActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")

        val shareded = getSharedPreferences(CONSTANTS.PREFE_ACCESS_SplashData, Context.MODE_PRIVATE)
        supportTitle = shareded.getString(CONSTANTS.supportTitle, "")
        supportText = shareded.getString(CONSTANTS.supportText, "")
        supportEmail = shareded.getString(CONSTANTS.supportEmail, "")

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.llEditProfile.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                callUserProfileActivity(act, "1")
            } else {
                showToast(ctx.getString(R.string.no_server_found), act)
            }
        }

        binding.llUnderGroundList.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                callUnderGroundListActivity(act, "1")
            } else {
                showToast(ctx.getString(R.string.no_server_found), act)
            }
        }

        binding.llOpenCastList.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                callOpenCastListActivity(act, "1")
            } else {
                showToast(ctx.getString(R.string.no_server_found), act)
            }
        }

        binding.llUnderGroundListDraft.setOnClickListener {
            callUnderGroundListDraftActivity(act, "1")
        }

        binding.llOpenCastListDraft.setOnClickListener {
            callOpenCastListDraftActivity(act, "1")
        }

        binding.llAboutUs.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                startActivity(
                    Intent(ctx, TncActivity::class.java).putExtra(
                        CONSTANTS.Web,
                        getString(R.string.about_us)
                    )
                )
            } else {
                showToast(ctx.getString(R.string.no_server_found), act)
            }
        }

        binding.llFAQ.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                callFaqActivity(act, "1")
            } else {
                showToast(ctx.getString(R.string.no_server_found), act)
            }
        }

        binding.llContactUs.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                callContactUsActivity(act, "1")
            } else {
                showToast(ctx.getString(R.string.no_server_found), act)
            }
        }

        binding.llSyncData.setOnClickListener {
            callSyncDataActivity(act, "1")
        }

        binding.llLogOut.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                logoutDialog = Dialog(ctx)
                logoutDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                logoutDialog!!.setContentView(R.layout.logout_layout)
                logoutDialog!!.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
                logoutDialog!!.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val tvGoBack = logoutDialog!!.findViewById<AppCompatButton>(R.id.tvGoBack)
                val btn = logoutDialog!!.findViewById<Button>(R.id.Btn)
//                val progressBar = logoutDialog!!.findViewById<ProgressBar>(R.id.progressBar)
//                val progressBarHolder =
//                    logoutDialog!!.findViewById<FrameLayout>(R.id.progressBarHolder)
                logoutDialog!!.setOnKeyListener { _ : DialogInterface?, keyCode : Int, _ : KeyEvent? ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        logoutDialog!!.dismiss()
                        return@setOnKeyListener true
                    }
                    false
                }
                btn.setOnClickListener {
                    logoutDialog!!.dismiss()
                    showProgressBar(binding.progressBar, binding.progressBarHolder, act)
                    callLogoutApi()
                }
                tvGoBack.setOnClickListener { logoutDialog!!.dismiss() }
                logoutDialog!!.show()
                logoutDialog!!.setCancelable(true)
                logoutDialog!!.setCanceledOnTouchOutside(true)
            } else {
                showToast(getString(R.string.no_server_found), act)
            }
        }

        binding.llSupport.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                supportDialog = Dialog(ctx)
                supportDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                supportDialog!!.setContentView(R.layout.support_layout)
                supportDialog!!.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
                supportDialog!!.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val tvEmail = supportDialog!!.findViewById<TextView>(R.id.tvEmail)
                val tvTitle = supportDialog!!.findViewById<TextView>(R.id.tvTitle)
                val tvHeader = supportDialog!!.findViewById<TextView>(R.id.tvHeader)
                val btnClose = supportDialog!!.findViewById<Button>(R.id.btnClose)
                if (supportTitle.equals("", ignoreCase = true)) {
                    tvTitle.text = getString(R.string.support)
                } else {
                    tvTitle.text = supportTitle
                }

                if (supportText.equals("", ignoreCase = true)) {
                    tvHeader.text = getString(R.string.support_quotes)
                } else {
                    tvHeader.text = supportText
                }

                if (supportEmail.equals("", ignoreCase = true)) {
                    tvEmail.text = getString(R.string.support_link)
                } else {
                    tvEmail.text = supportEmail
                }

                tvEmail.setOnClickListener {
                    val intent = Intent(Intent.ACTION_SEND)
                    val recipients = if (supportEmail.equals("", ignoreCase = true)) {
                        arrayOf(getString(R.string.support_link))
                    } else {
                        arrayOf(supportEmail)
                    }

                    intent.putExtra(Intent.EXTRA_EMAIL, recipients)
                    intent.putExtra(Intent.EXTRA_SUBJECT, "")
                    intent.putExtra(Intent.EXTRA_TEXT, "")
                    intent.putExtra(Intent.EXTRA_CC, "")
                    intent.type = "text/html"
                    intent.setPackage("com.google.android.gm")
                    try {
                        startActivity(Intent.createChooser(intent, "Send mail"))
                    } catch (ex : ActivityNotFoundException) {
                        showToast("There are no email clients installed.", act)
                    }
                }

                supportDialog!!.setOnKeyListener { _ : DialogInterface?, keyCode : Int, _ : KeyEvent? ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        supportDialog!!.dismiss()
                        return@setOnKeyListener true
                    }
                    false
                }
                btnClose.setOnClickListener { supportDialog!!.dismiss() }
                supportDialog!!.show()
                supportDialog!!.setCancelable(true)
                supportDialog!!.setCanceledOnTouchOutside(true)
            } else {
                showToast(getString(R.string.no_server_found), act)
            }
        }

    }

    override fun onResume() {
        prepareData()
        super.onResume()
    }

    private fun prepareData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            RetrofitService.getInstance().getUserDetails(userId)
                    .enqueue(object : Callback<UserCommonDataModel> {
                        override fun onResponse(
                                call : Call<UserCommonDataModel>,
                                response : Response<UserCommonDataModel>
                        ) {
                            hideProgressBar(
                                binding.progressBar, binding.progressBarHolder,
                                act
                            )
                            val coachStatusModel : UserCommonDataModel? = response.body()
                            when (coachStatusModel!!.responseCode) {
                                getString(R.string.ResponseCodesuccess) -> {
                                    if (coachStatusModel.responseData!!.profileImage == "") {
                                        binding.civProfile.visibility = View.GONE
                                        binding.rlCameraBg.visibility = View.GONE
                                        val name = if (coachStatusModel.responseData!!.name == "") {
                                            "Guest"
                                        } else {
                                            coachStatusModel.responseData!!.name
                                        }
                                        binding.tvUserName.text = name
                                        binding.rlLetter.visibility = View.VISIBLE
                                        binding.tvLetter.text = name!!.substring(0, 1)
                                    } else {
                                        binding.civProfile.visibility = View.VISIBLE
                                        binding.rlCameraBg.visibility = View.VISIBLE
                                        binding.rlLetter.visibility = View.GONE
                                        try {
                                            Glide.with(ctx)
                                                    .load(coachStatusModel.responseData!!.profileImage)
                                                    .thumbnail(0.10f)
                                                    .apply(RequestOptions.bitmapTransform(RoundedCorners(126)))
                                                    .into(binding.civProfile)
                                        } catch (e : Exception) {
                                            e.printStackTrace()
                                            try {
                                                Glide.with(ctx).load(
                                                        coachStatusModel.responseData!!.profileImage).thumbnail(
                                                        0.10f).apply(RequestOptions.bitmapTransform(
                                                        RoundedCorners(126))).into(
                                                        binding.civProfile)
                                            } catch (e1: Exception) {
                                                e1.printStackTrace()
                                            }
                                        }
                                        binding.tvUserName.text = coachStatusModel.responseData!!.name
                                    }
                                }
                                getString(R.string.ResponseCodefail) -> {
                                    showToast(coachStatusModel.responseMessage, act)
                                }
                                getString(R.string.ResponseCodeDeleted) -> {
                                    callDelete403(act, coachStatusModel.responseMessage)
                                }
                            }
                        }

                        override fun onFailure(call : Call<UserCommonDataModel>, t : Throwable) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        }
                    })
        } else {
            val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
            binding.civProfile.visibility = View.GONE
            binding.rlCameraBg.visibility = View.GONE
            val name = shared.getString(CONSTANTS.name, "Guest")
            binding.tvUserName.text = name
            binding.rlLetter.visibility = View.VISIBLE
            binding.tvLetter.text = name?.substring(0, 1)
        }
    }

    private fun callLogoutApi() {
        val shared = getSharedPreferences(CONSTANTS.FCMToken, MODE_PRIVATE)
        val fcmId = shared.getString(CONSTANTS.Token, "")
        val deviceId = Settings.Secure.getString(
            getContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )
        if (isNetworkConnected(ctx)) {
            RetrofitService.getInstance().postLogout(userId, CONSTANTS.FLAG_ONE, fcmId, deviceId)
                    .enqueue(object : Callback<ConfirmSuccessModel?> {
                        override fun onResponse(
                                call : Call<ConfirmSuccessModel?>,
                                response : Response<ConfirmSuccessModel?>
                        ) {
                            val successModel = response.body()
                            if (successModel != null) {
                                when (successModel.ResponseCode) {
                                    (getString(R.string.ResponseCodesuccess)) -> {
                                        callDelete403(act, successModel.ResponseMessage)
                                    }
                                    getString(R.string.ResponseCodefail) -> {
                                        showToast(successModel.ResponseMessage, act)
                                    }
                                    getString(R.string.ResponseCodeDeleted) -> {
                                        callDelete403(act, successModel.ResponseMessage)
                                    }
                                }
                            }
                        }

                        override fun onFailure(call : Call<ConfirmSuccessModel?>, t : Throwable) {
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