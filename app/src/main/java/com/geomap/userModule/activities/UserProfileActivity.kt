package com.geomap.userModule.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.geomap.BuildConfig
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUserProfileBinding
import com.geomap.mapReportModule.models.SuccessModel
import com.geomap.mvvm.AllViewModel
import com.geomap.mvvm.UserModelFactory
import com.geomap.mvvm.UserRepository
import com.geomap.userModule.models.ProfileUpdateModel
import com.geomap.userModule.models.UserCommonDataModel
import com.geomap.utils.APIClientProfile.apiService
import com.geomap.utils.CONSTANTS
import com.geomap.utils.FileUtil.getPath
import com.geomap.utils.RequestPermissionHandler
import com.geomap.utils.RetrofitService
import retrofit.RetrofitError
import retrofit.mime.TypedFile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserProfileBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    private var name : String? = null
    private var email : String? = null
    private var mobileNo : String? = null
    private var dob : String? = null
    private var id : String? = null
    private var userId : String? = null
    private var profileImage : String? = null
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0
    private var birthYear = 0
    private var ageYear : Int = 0
    private var ageMonth : Int = 0
    private var ageDate : Int = 0
    private var profilePicPath : String? = null
    private var typedFile : TypedFile? = null
    private var mRequestPermissionHandler : RequestPermissionHandler? = null
    private var deleteDialog : Dialog? = null
    private var mLastClickTime : Long = 0
    private lateinit var viewModel : AllViewModel

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            val nameUser = binding.etName.text.toString()
            val emailUser = binding.etEmail.text.toString()
            val mobileNoUser = binding.etMobileNo.text.toString()

            when {
                nameUser == name && emailUser == email && mobileNoUser == mobileNo -> {
                    allDisable(binding.btnUpdate)
                }

                nameUser.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnUpdate)
                }

                emailUser.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnUpdate)
                }

                mobileNoUser.length < 4 -> {
                    allDisable(binding.btnUpdate)
                }

                birthYear < 18 -> {
                    allDisable(binding.btnUpdate)
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)
        ctx = this@UserProfileActivity
        act = this@UserProfileActivity

        val shared = getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE)
        userId = shared.getString(CONSTANTS.userId, "")
        mRequestPermissionHandler = RequestPermissionHandler()

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.etName.addTextChangedListener(userTextWatcher)
        binding.etEmail.addTextChangedListener(userTextWatcher)
        binding.etMobileNo.addTextChangedListener(userTextWatcher)

        prepareData()

        binding.etDob.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setDate()
            }
        }

        binding.llImageUpload.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                selectImage()
            } else {
                showToast(ctx.getString(R.string.no_server_found), act)
            }
        }

        binding.btnDeleteAccount.setOnClickListener {
            if (isNetworkConnected(ctx)) {
                deleteDialog = Dialog(ctx)
                deleteDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                deleteDialog!!.setContentView(R.layout.logout_layout)
                deleteDialog!!.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
                deleteDialog!!.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val tvGoBack = deleteDialog!!.findViewById<TextView>(R.id.tvGoBack)
                val tvTitle = deleteDialog!!.findViewById<TextView>(R.id.tvTitle)
                val tvHeader = deleteDialog!!.findViewById<TextView>(R.id.tvHeader)
                val btn = deleteDialog!!.findViewById<Button>(R.id.Btn)
                val progressBar = deleteDialog!!.findViewById<ProgressBar>(R.id.progressBar)
                val progressBarHolder =
                    deleteDialog!!.findViewById<FrameLayout>(R.id.progressBarHolder)
                tvTitle.text = getString(R.string.delete_account)
                tvHeader.text = getString(R.string.delete_ac_quotes)

                deleteDialog!!.setOnKeyListener { _ : DialogInterface?, keyCode : Int, _ : KeyEvent? ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        deleteDialog!!.dismiss()
                        return@setOnKeyListener true
                    }
                    false
                }
                btn.setOnClickListener {
                    deleteDialog!!.dismiss()
                    showProgressBar(progressBar, progressBarHolder, act)
                    callDeleteAcApi()
                }
                tvGoBack.setOnClickListener { deleteDialog!!.dismiss() }
                deleteDialog!!.show()
                deleteDialog!!.setCancelable(false)
            } else {
                showToast(getString(R.string.no_server_found), act)
            }
        }


        binding.btnUpdate.setOnClickListener {
            if (!binding.etEmail.text.toString().isEmailValid()) {
                binding.etEmail.isFocusable = true
                binding.etEmail.requestFocus()
                binding.ltEmail.isErrorEnabled = true
                binding.ltEmail.error = getString(R.string.pls_provide_valid_email)
            } else {
                binding.ltEmail.isErrorEnabled = false
                prepareUpdateData()
            }
        }
    }

    private fun callDeleteAcApi() {

        if (isNetworkConnected(ctx)) {
            RetrofitService.getInstance().postDeleteUser(
                userId
            ).enqueue(object : Callback<SuccessModel?> {
                override fun onResponse(
                    call : Call<SuccessModel?>,
                    response : Response<SuccessModel?>
                ) {
                    val model = response.body()
                    hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return
                    }
                    mLastClickTime = SystemClock.elapsedRealtime()
                    if (model!!.ResponseCode == getString(R.string.ResponseCodesuccess)) {
                        callDelete403(act, model.ResponseMessage)
                    } else if (model.ResponseCode == ctx.getString(R.string.ResponseCodefail)) {
                        showToast(model.ResponseMessage, act)
                    } else if (model.ResponseCode == ctx.getString(R.string.ResponseCodeDeleted)) {
                        callDelete403(act, model.ResponseMessage)
                    }
                }

                override fun onFailure(call : Call<SuccessModel?>, t : Throwable) {
                    hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                }
            })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    private fun String.isEmailValid() : Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    private fun prepareUpdateData() {
        if (isNetworkConnected(ctx)) {
            showProgressBar(binding.progressBar, binding.progressBarHolder, act)
            val map = HashMap<String, String?>()
            map[CONSTANTS.id] = id
            apiService!!.getProfileUpdate(userId, binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etDob.text.toString(), binding.etMobileNo.text.toString(), typedFile,
                object : retrofit.Callback<ProfileUpdateModel> {
                    override fun success(
                        model : ProfileUpdateModel,
                        response : retrofit.client.Response
                    ) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        if (model.responseCode.equals(
                                ctx.getString(R.string.ResponseCodesuccess)
                            )
                        ) {
                            showToast(model.responseMessage, act)
                            val shared1 =
                                getSharedPreferences(
                                    CONSTANTS.PREFE_ACCESS_USERDATA,
                                    Context.MODE_PRIVATE
                                )
                            val editor1 = shared1.edit()
                            editor1.putString(CONSTANTS.profileImage, model.responseData?.profileImage)
                            editor1.putString(CONSTANTS.dob, model.responseData?.dob)
                            editor1.putString(CONSTANTS.mobile, model.responseData?.mobile)
                            editor1.putString(CONSTANTS.name, model.responseData?.name)
                            editor1.putString(CONSTANTS.email, model.responseData?.email)
                            editor1.apply()
                            prepareData()
                            finish()
                        } else if (model.responseCode.equals(
                                ctx.getString(R.string.ResponseCodefail)
                            )
                        ) {
                            showToast(model.responseMessage, act)
                        } else if (model.responseCode.equals(
                                ctx.getString(R.string.ResponseCodeDeleted)
                            )
                        ) {
                            callDelete403(act, model.responseMessage)
                        }
                    }

                    override fun failure(e : RetrofitError) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        showToast(e.message, act)
                    }
                })
        } else {
            showToast(getString(R.string.no_server_found), act)
        }
    }

    private fun prepareData() {
        showProgressBar(binding.progressBar, binding.progressBarHolder, act)
        if (isNetworkConnected(ctx)) {
            RetrofitService.getInstance().getUserDetails(userId)
                .enqueue(object : Callback<UserCommonDataModel> {
                    override fun onResponse(
                        call : Call<UserCommonDataModel>,
                        response : Response<UserCommonDataModel>
                    ) {
                        hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        val coachStatusModel : UserCommonDataModel? = response.body()
                        when (coachStatusModel!!.responseCode) {
                            getString(R.string.ResponseCodesuccess) -> {
//                                binding.rlMainLayout.visibility = View.VISIBLE
                                name = coachStatusModel.responseData!!.name
                                email = coachStatusModel.responseData!!.email
                                mobileNo = coachStatusModel.responseData!!.mobile
                                dob = coachStatusModel.responseData!!.dob
                                profileImage = coachStatusModel.responseData!!.profileImage
                                binding.etName.setText(name)
                                binding.etEmail.setText(email)
                                binding.etMobileNo.setText(mobileNo)
                                binding.etDob.setText(dob)
                                if (coachStatusModel.responseData!!.dob != "") {
                                    try {
                                        val outputFormat: DateFormat = SimpleDateFormat(
                                            CONSTANTS.DATE_MONTH_YEAR_FORMAT1)
                                        val inputFormat: DateFormat = SimpleDateFormat(
                                            CONSTANTS.DATE_MONTH_YEAR_FORMAT)
                                        val inputText = dob
                                        val date: Date? = inputFormat.parse(inputText!!)
                                        val outputText: String = outputFormat.format(date!!)
                                        val dateSpilt = outputText.split(" ")
                                        ageYear = dateSpilt[2].toInt()
                                        ageMonth = dateSpilt[1].toInt()
                                        ageDate = dateSpilt[0].toInt()
                                        birthYear = getAge(ageYear, ageMonth, ageDate)
                                    }catch (e: Exception){
                                        e.printStackTrace()
                                    }
                                }
                                binding.ivCameraIconBg.visibility = View.VISIBLE
                                binding.ivCameraIcon.visibility = View.VISIBLE
                                if (coachStatusModel.responseData!!.profileImage == "") {
                                    binding.civProfile.visibility = View.GONE
                                    binding.rlCameraBg.visibility = View.GONE
                                    val name = if (coachStatusModel.responseData!!.name == "") {
                                        "Guest"
                                    } else {
                                        coachStatusModel.responseData!!.name
                                    }
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
                                    } catch (_ : Exception) {
                                        try {
                                            Glide.with(ctx)
                                                .load(coachStatusModel.responseData!!.profileImage)
                                                .thumbnail(0.10f)
                                                .apply(RequestOptions.bitmapTransform(RoundedCorners(126)))
                                                .into(binding.civProfile)
                                        } catch (e: Exception) {
                                        }
                                    }
                                }

//                                binding.etMobileNo.isEnabled = false
//                                binding.etMobileNo.isClickable = false
//                                binding.etMobileNo.setTextColor(
//                                    ContextCompat.getColor(applicationContext, R.color.light_gray))

//                                binding.etEmail.isEnabled = false
//                                binding.etEmail.isClickable = false
//                                binding.etEmail.setTextColor(
//                                    ContextCompat.getColor(applicationContext, R.color.light_gray))

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
            setProfilePic("")
            binding.civProfile.visibility = View.GONE
            binding.rlCameraBg.visibility = View.GONE
            val name = "Guest"
            binding.rlLetter.visibility = View.VISIBLE
            binding.tvLetter.text = name.substring(0, 1)
        }
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTENT_REQUEST && resultCode == Activity.RESULT_OK) {
            val map = HashMap<String, String?>()
            map[CONSTANTS.userId] = "userId"
            val photo: Bitmap? = data!!.extras!!["data"] as Bitmap?
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "IMG_" + timeStamp + "_"
            image =  File(cacheDir, "$imageFileName.jpg")
            val os : OutputStream = BufferedOutputStream(FileOutputStream(image))
            photo!!.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.close()
            typedFile = TypedFile(CONSTANTS.MULTIPART_FORMAT, image)
            setProfilePic(image.toString())
            enableButton()
            Log.e("Camera Image URL", image.toString())
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImageUri = data.data
                setProfilePic(selectedImageUri.toString())
                Log.e("Gallery Image URL", selectedImageUri.toString())
                val map = HashMap<String, String?>()
                map[CONSTANTS.userId] = "userId"
                val file = File(Objects.requireNonNull(getPath(selectedImageUri!!, ctx)))
                typedFile = TypedFile(CONSTANTS.MULTIPART_FORMAT, file)
                enableButton()
            }
        } else if (requestCode == Activity.RESULT_CANCELED) {
            finish()
        }
    }

    private fun enableButton() {
        binding.btnUpdate.isEnabled = true
        binding.btnUpdate.setBackgroundResource(R.drawable.enable_button)
    }

    private fun setProfilePic(profilePic : String?) {
        if (profilePic.equals("")) {
            try {
                Glide.with(applicationContext).load(R.drawable.default_profile).thumbnail(0.10f)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(126)))
                    .into(binding.civProfile)
            } catch (_ : Exception) {
            }
        } else {
            try {
                Glide.with(applicationContext).load(profilePic).thumbnail(0.10f)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(126)))
                    .into(binding.civProfile)
            } catch (_ : Exception) {
            }
        }
    }

    private fun selectImage() {
//        val writeP = ContextCompat.checkSelfPermission(ctx,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readP = ContextCompat.checkSelfPermission(ctx,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        val camaraP = ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA)
        val permissionG = PackageManager.PERMISSION_GRANTED
        val permissionD = PackageManager.PERMISSION_DENIED
//        val aWriteP = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val aReadP = Manifest.permission.READ_EXTERNAL_STORAGE
        val aCamaraP = Manifest.permission.CAMERA
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (readP == permissionG && camaraP == permissionG) {
                callProfilePathSet()
            } else {
                callPermission(arrayOf(aReadP, aCamaraP))
            }
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            if (readP == permissionG && camaraP == permissionG) {
                callProfilePathSet()
            } else {
                if (camaraP != permissionG && readP != permissionG) {
                    callPermission(arrayOf(aReadP, aCamaraP))
                } else if (camaraP == permissionD) {
                    callCamaraPermission()
                } else if (readP == permissionD) {
                    callReadPermission("1", "Files and media")
                } else {
                    callPermission(arrayOf(aReadP, aCamaraP))
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val readPI = ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.READ_MEDIA_IMAGES)
            if (readPI == permissionG && camaraP == permissionG) {
                callProfilePathSet()
            } else {
                if (camaraP != permissionG && readPI != permissionG) {
                    callPermission(arrayOf(aReadP, aCamaraP))
                } else if (camaraP == permissionD) {
                    callCamaraPermission()
                } else if (readPI == permissionD) {
                    callReadPermission("0", "Photos and videos")
                } else {
                    callPermission(arrayOf(aReadP, aCamaraP))
                }
            }
        } else {
            callProfilePathSet()
        }
    }
    private fun callPermission(arry: Array<String>) {

        mRequestPermissionHandler!!.requestPermission(act, arry, 123,
            object : RequestPermissionHandler.RequestPermissionListener {
                override fun onSuccess() {
                    callProfilePathSet()
                }

                override fun onFailed() {}
            })
    }

    private fun callCamaraPermission() {
        val building = AlertDialog.Builder(ctx)
        building.setMessage(
            """To camera allow ${
                ctx.getString(
                    R.string.app_name
                )
            } access to your camera. Tap Setting > permission, and turn Camera on."""
        )
        building.setCancelable(true)
        building.setPositiveButton(
            getString(R.string.Settings)
        ) { dialogs : DialogInterface, _ : Int ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
            dialogs.dismiss()
        }
        building.setNegativeButton(
            ctx.getString(R.string.not_now)
        ) { dialogs : DialogInterface, _ : Int -> dialogs.dismiss() }
        val alert11 = building.create()
        alert11.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        alert11.show()
        alert11.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
        alert11.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
    }

    private fun callReadPermission(version : String, text : String) {
        val buildable = AlertDialog.Builder(ctx)
        buildable.setPositiveButton(R.string.Settings) { dialogs : DialogInterface, _ : Int ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
            dialogs.dismiss()
        }

        if (version == "0") {
            buildable.setMessage(
                "To upload image allow ${
                    getString(R.string.app_name)
                } access to your device's files. Tap Setting > permission, and turn $text on."
            )
        } else if (version == "1") {
            buildable.setMessage(
                "To upload image allow ${
                    getString(R.string.app_name)
                } access to your device's files. Tap Setting > permission, and turn $text on."
            )
        }

        buildable.setNegativeButton(
            ctx.getString(R.string.not_now)
        ) { dialogue : DialogInterface, _ : Int -> dialogue.dismiss() }
        buildable.setCancelable(true)
        val alert11 = buildable.create()
        alert11.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        alert11.show()
        alert11.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
        alert11.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
    }

    private fun callProfilePathSet() {
        options = arrayOf(getString(R.string.takePhoto), getString(R.string.chooseFromGallary),
            getString(R.string.cancel_small))
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(R.string.addPhoto)
        builder.setItems(options) { dialog: DialogInterface, item: Int ->
            if (options[item] == getString(R.string.takePhoto)) {
                val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(pictureIntent, CONTENT_REQUEST)
            } else if (options[item] == getString(R.string.chooseFromGallary)) {
                val intent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            } else if (options[item] == ctx.getString(R.string.cancel_small)) {
                dialog.dismiss()
            }
        }
        val alert11 = builder.create()
        alert11.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        alert11.show()
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun setDate() {
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            this, R.style.DialogTheme,
            { view : DatePicker, year : Int, monthOfYear : Int, dayOfMonth : Int ->
                view.minDate = System.currentTimeMillis() - 1000
                val cal = Calendar.getInstance()
                cal.timeInMillis
                cal[year, monthOfYear] = dayOfMonth
                val date = cal.time
                val sdf = SimpleDateFormat(CONSTANTS.DATE_MONTH_YEAR_FORMAT)
                val strDate = sdf.format(date)
                binding.etDob.setText(strDate)
                ageYear = year
                ageMonth = monthOfYear
                ageDate = dayOfMonth
                birthYear = getAge(ageYear, ageMonth, ageDate)
                if (birthYear < 18) {
                    binding.etDob.isFocusable = true
                    binding.etDob.requestFocus()
                    binding.ltDob.isErrorEnabled = true
                    binding.ltDob.error = getString(R.string.check_dob)
                    allDisable(binding.btnUpdate)
                } else {
                    binding.ltDob.isErrorEnabled = false
                    enableButton()
                }
            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    private fun getAge(year : Int, month : Int, day : Int) : Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age
    }

    companion object {
        private lateinit var options : Array<String>
        lateinit var image : File
        private const val CONTENT_REQUEST = 100
    }

    override fun onBackPressed() {
        finish()
    }
}