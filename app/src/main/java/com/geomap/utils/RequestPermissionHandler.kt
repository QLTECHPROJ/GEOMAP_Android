package com.geomap.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class RequestPermissionHandler {
    private var mActivity : Activity? = null
    private var mRequestPermissionListener : RequestPermissionListener? = null
    private var mRequestCode = 0
    fun requestPermission(
        activity : Activity?, permissions : Array<String>, requestCode : Int,
        listener : RequestPermissionListener?
    ) {
        mActivity = activity
        mRequestCode = requestCode
        mRequestPermissionListener = listener
        requestUnGrantedPermissions(permissions, requestCode)
    }

    private fun requestUnGrantedPermissions(permissions : Array<String>, requestCode : Int) {
        val unGrantedPermissions = findUnGrantedPermissions(permissions)
        if (unGrantedPermissions.isEmpty()) {
            mRequestPermissionListener!!.onSuccess()
            return
        }
        ActivityCompat.requestPermissions(mActivity!!, unGrantedPermissions, requestCode)
    }

    private fun isPermissionGranted(permission : String) : Boolean {
        return (ActivityCompat.checkSelfPermission(mActivity!!, permission)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun findUnGrantedPermissions(permissions : Array<String>) : Array<String> {
        val unGrantedPermissionList : MutableList<String> = ArrayList()
        for (permission in permissions) {
            if (!isPermissionGranted(permission)) {
                unGrantedPermissionList.add(permission)
            }
        }
        return unGrantedPermissionList.toTypedArray()
    }

    interface RequestPermissionListener {
        fun onSuccess()
        fun onFailed()
    }
}