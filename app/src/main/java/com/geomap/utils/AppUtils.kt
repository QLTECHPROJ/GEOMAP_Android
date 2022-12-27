package com.geomap.utils

import android.app.Application

object AppUtils : Application() {
    const val MAIN_URL = "https://www.qlresources.com.au"
    const val STAGING_MAIN_URL = "https://admin.qlresources.com.au/geomap/api/v1/"
    const val New_BASE_URL = STAGING_MAIN_URL
    var about_us = "$MAIN_URL/about/"
}