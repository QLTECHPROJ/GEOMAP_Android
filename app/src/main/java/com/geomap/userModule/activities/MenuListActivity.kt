package com.geomap.userModule.activities

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityMenuListBinding
import com.geomap.utils.CONSTANTS
import com.geomap.webView.TncActivity

class MenuListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMenuListBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var supportDialog : Dialog? = null
    private var supportTitle : String? = null
    private var supportText : String? = null
    private var supportEmail : String? = null
    private var logoutDialog : Dialog? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_list)
        ctx = this@MenuListActivity
        act = this@MenuListActivity

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
            callUnderGroundListActivity(act, "1")
        }

        binding.llOpenCastList.setOnClickListener {
            callOpenCastListActivity(act, "1")
        }
        binding.llAboutUs.setOnClickListener {
            startActivity(Intent(ctx, TncActivity::class.java).putExtra(CONSTANTS.Web,
                getString(R.string.about_us)))
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
            if (isNetworkConnected(ctx)) {
                callSyncDataActivity(act, "1")
            } else {
                showToast(ctx.getString(R.string.no_server_found), act)
            }
        }

        binding.llLogOut.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                logoutDialog = Dialog(ctx)
                logoutDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                logoutDialog!!.setContentView(R.layout.logout_layout)
                logoutDialog!!.window!!.setBackgroundDrawable(
                    ColorDrawable(ContextCompat.getColor(ctx, R.color.primary_light_theme)))
                logoutDialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
                val tvGoBack = logoutDialog!!.findViewById<AppCompatButton>(R.id.tvGoBack)
                val btn = logoutDialog!!.findViewById<Button>(R.id.Btn)
                val progressBar = logoutDialog!!.findViewById<ProgressBar>(R.id.progressBar)
                val progressBarHolder =
                    logoutDialog!!.findViewById<FrameLayout>(R.id.progressBarHolder)
                logoutDialog!!.setOnKeyListener { _ : DialogInterface?, keyCode : Int, _ : KeyEvent? ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        logoutDialog!!.hide()
                        return@setOnKeyListener true
                    }
                    false
                }
                btn.setOnClickListener {
                    logoutDialog!!.hide()
                    showProgressBar(progressBar, progressBarHolder, act)
//                    logoutCall(binding.progressBar)

                    callSignActivity("", act)
                }
                tvGoBack.setOnClickListener { logoutDialog!!.hide() }
                logoutDialog!!.show()
                logoutDialog!!.setCancelable(false)
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
                    ColorDrawable(ContextCompat.getColor(ctx, R.color.primary_transparent)))
                supportDialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
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
                supportDialog!!.setCancelable(false)
            } else {
                showToast(getString(R.string.no_server_found), act)
            }
        }

    }

/*
    private fun logoutCall(progressBar : ProgressBar) {
        deleteCall(ctx)
        callLogoutApi(progressBar)
    }
*/

    override fun onBackPressed() {
        finish()
    }
}