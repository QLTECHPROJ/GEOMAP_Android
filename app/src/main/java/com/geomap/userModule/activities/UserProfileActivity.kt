package com.geomap.userModule.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.geomap.BuildConfig
import com.geomap.GeoMapApp.*
import com.geomap.R
import com.geomap.databinding.ActivityUserProfileBinding
import com.geomap.utils.CONSTANTS
import com.geomap.utils.FileUtil.getPath
import com.geomap.utils.RequestPermissionHandler
import retrofit.mime.TypedFile
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserProfileBinding
    private lateinit var ctx : Context
    private lateinit var act : Activity
    var name : String? = ""
    var email : String? = ""
    var mobileNo : String? = ""
    var dob : String? = ""
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0
    private var birthYear = 0
    private var ageYear : Int = 0
    private var ageMonth : Int = 0
    private var ageDate : Int = 0
    private var profilePicPath : String? = ""
    private var typedFile : TypedFile? = null
    private var mRequestPermissionHandler : RequestPermissionHandler? = null

    private var userTextWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {
            name = binding.etName.text.toString()
            email = binding.etEmail.text.toString()
            mobileNo = binding.etMobileNo.text.toString()

            when {
                name.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnUpdate)
                }

                email.equals("", ignoreCase = true) -> {
                    allDisable(binding.btnUpdate)
                }

                mobileNo.equals("", ignoreCase = true) -> {
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

        binding.llBack.setOnClickListener {
            onBackPressed()
        }

        binding.etName.addTextChangedListener(userTextWatcher)
        binding.etEmail.addTextChangedListener(userTextWatcher)
        binding.etMobileNo.addTextChangedListener(userTextWatcher)

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
            finish()
        }

        binding.btnUpdate.setOnClickListener {
            if (!binding.etEmail.text.toString().isEmailValid()) {
                binding.etEmail.isFocusable = true
                binding.etEmail.requestFocus()
                binding.ltEmail.isErrorEnabled = true
                binding.ltEmail.error = getString(R.string.pls_provide_valid_email)
            } else {
                binding.ltEmail.isErrorEnabled = false
                finish()
            }
        }
    }

    private fun String.isEmailValid() : Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTENT_REQUEST && resultCode == Activity.RESULT_OK) {
            setProfilePic(profilePicPath)
            val map = HashMap<String, String?>()
            map[CONSTANTS.custId] = "custId"
            typedFile = TypedFile(CONSTANTS.MULTIPART_FORMAT, image)
            enableButton()
            Log.e("Camera Image URL", image.toString())
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImageUri = data.data
                setProfilePic(selectedImageUri.toString())
                Log.e("Gallery Image URL", selectedImageUri.toString())
                val map = HashMap<String, String?>()
                map[CONSTANTS.custId] = "custId"
                val file = File(Objects.requireNonNull(getPath(selectedImageUri!!, ctx)))
                typedFile = TypedFile(CONSTANTS.MULTIPART_FORMAT, file)
                enableButton()
            }
        } else if (requestCode == Activity.RESULT_CANCELED) {
            act.finish()
        }
    }

    private fun enableButton() {
        binding.btnUpdate.isEnabled = true
        binding.btnUpdate.setBackgroundResource(R.drawable.enable_button)
    }

    private fun setProfilePic(profilePic : String?) {
        if (profilePic.equals("")) {
            Glide.with(applicationContext).load(R.drawable.default_profile).thumbnail(0.10f)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(126))).into(binding.civProfile)
        } else {
            Glide.with(applicationContext).load(profilePic).thumbnail(0.10f)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(126))).into(binding.civProfile)
        }
    }

    private fun selectImage() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    ctx, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    ctx, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) {
                callProfilePathSet()
            } else {
                mRequestPermissionHandler!!.requestPermission(
                    act, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                    ), 123, object : RequestPermissionHandler.RequestPermissionListener {
                        override fun onSuccess() {
                            callProfilePathSet()
                        }

                        override fun onFailed() {}
                    })
            }
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    ctx, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    ctx, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) {
                callProfilePathSet()
            } else {
                if (ContextCompat.checkSelfPermission(
                        ctx, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        ctx, Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED) {
                    mRequestPermissionHandler!!.requestPermission(
                        act, arrayOf(
                            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                        ), 123, object : RequestPermissionHandler.RequestPermissionListener {
                            override fun onSuccess() {
                                callProfilePathSet()
                            }

                            override fun onFailed() {}
                        })
                } else if (ContextCompat.checkSelfPermission(
                        ctx, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED) {
                    callCamaraPermission()
                } else if (ContextCompat.checkSelfPermission(
                        ctx, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED) {
                    callReadPermission()
                }
            }
        } else {
            callProfilePathSet()
        }
    }

    private fun callProfilePathSet() {
        options = if (profilePicPath == "") {
            arrayOf(
                getString(R.string.takePhoto), getString(R.string.chooseFromGallary),
                getString(R.string.cancel_small)
            )
        } else {
            arrayOf(
                getString(R.string.takePhoto), getString(R.string.chooseFromGallary),
                getString(R.string.cancel_small)
            )
        }
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(R.string.addPhoto)
        builder.setItems(options) { dialog : DialogInterface, item : Int ->
            if (options[item] == getString(R.string.takePhoto)) {
                val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (pictureIntent.resolveActivity(ctx.packageManager) != null) {
                    var photoFile : File? = null
                    try {
                        photoFile = createImageFile()
                    } catch (ex : IOException) {
                        ex.printStackTrace()
                    }
                    if (photoFile != null) {
                        val photoURI =
                            FileProvider.getUriForFile(
                                ctx, BuildConfig.APPLICATION_ID + ".provider", photoFile
                            )
                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(pictureIntent, CONTENT_REQUEST)
                    }
                }
            } else if (options[item] == getString(R.string.chooseFromGallary)) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }/* else if (options[item] == getString(R.string.removeProfilePicture)) {
                if (isNetworkConnected(ctx)) {
                    showProgressBar(binding.progressBar, binding.progressBarHolder, act)
                    val listCall = APINewClient.client.getRemoveProfile(coUserId)
                    listCall.enqueue(object : Callback<RemoveProfileModel?> {
                        override fun onResponse(call: Call<RemoveProfileModel?>, response: Response<RemoveProfileModel?>) {
                            try {
                                val viewModel = response.body()
                                hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                                if (viewModel != null) {
                                    if (viewModel.responseCode.equals(activity?.getString(R.string.ResponseCodesuccess))) {
                                        showToast(viewModel.responseMessage, act)
                                        val shared = ctx.getSharedPreferences(CONSTANTS.PREFE_ACCESS_SIGNIN_COUSER, Context.MODE_PRIVATE)
                                        val editor = shared.edit()
                                        editor.putString(CONSTANTS.PREFE_ACCESS_IMAGE, "")
                                        editor.apply()
                                        profileViewData()
                                    } else if (viewModel.responseCode.equals(activity?.getString(R.string.ResponseCodeDeleted))) {
                                        callDelete403(act, viewModel.responseMessage)
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: Call<RemoveProfileModel?>, t: Throwable) {
                            hideProgressBar(binding.progressBar, binding.progressBarHolder, act)
                        }
                    })
                }
            }*/ else if (options[item] == ctx.getString(R.string.cancel_small)) {
                //                val p = Properties()
                //                addToSegment("Profile Photo Cancelled", p, CONSTANTS.track)
                dialog.dismiss()
            }
        }
        val alert11 = builder.create()
        alert11.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        alert11.show()
    }

    @Throws(IOException::class) private fun createImageFile() : File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        image = File.createTempFile(imageFileName, ".jpg", storageDir)
        profilePicPath = image.absolutePath
        //        Glide.with(ctx).load(profilePicPath).thumbnail(0.10f)
        //            .apply(RequestOptions.bitmapTransform(RoundedCorners(126))).into(binding.civProfile)
        Log.e("image url", profilePicPath.toString())
        return image
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
            ctx.getString(R.string.Settings)
        ) { dialogs : DialogInterface, _ : Int ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", ctx.packageName, null)
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

    private fun callReadPermission() {
        val buildable = AlertDialog.Builder(ctx)
        buildable.setMessage(
            """To upload image allow ${
                ctx.getString(
                    R.string.app_name
                )
            } access to your device's files. 
Tap Setting > permission, and turn "Files and media" on."""
        )
        buildable.setCancelable(true)
        buildable.setPositiveButton(
            ctx.getString(R.string.Settings)
        ) { dialogs : DialogInterface, _ : Int ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", ctx.packageName, null)
            intent.data = uri
            startActivity(intent)
            dialogs.dismiss()
        }
        buildable.setNegativeButton(
            ctx.getString(R.string.not_now)
        ) { dialogue : DialogInterface, _ : Int -> dialogue.dismiss() }
        val alert11 = buildable.create()
        alert11.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        alert11.show()
        alert11.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
        alert11.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(ctx, R.color.primary_theme))
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun setDate() {
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(this, R.style.DialogTheme,
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
                if (birthYear < 3) {
                    binding.etDob.isFocusable = true
                    binding.etDob.requestFocus()
                    binding.ltDob.isErrorEnabled = true
                    binding.ltDob.error = getString(R.string.check_dob)
                    allDisable(binding.btnUpdate)
                    binding.btnUpdate.setTextColor(
                        ContextCompat.getColor(applicationContext, R.color.white))
                    binding.btnUpdate.setBackgroundResource(R.drawable.disable_button)
                } else {
                    binding.ltDob.isErrorEnabled = false
                    enableButton()
                    binding.btnUpdate.setTextColor(
                        ContextCompat.getColor(applicationContext, R.color.white))
                    binding.btnUpdate.setBackgroundResource(R.drawable.enable_button)
                }
            }, mYear, mMonth, mDay)
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