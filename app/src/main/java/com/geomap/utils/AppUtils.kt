package com.geomap.utils

import android.app.Application

object AppUtils : Application() {
    const val MAIN_URL = "https://www.qlresources.com.au"
    const val STAGING_MAIN_URL = "https://geomap.qlresources.com.au/api/staging/v1/"
    const val LIVE_MAIN_URL = "https://geomap.qlresources.com.au/api/live/v1/"
    const val New_BASE_URL = LIVE_MAIN_URL
    var about_us = "$MAIN_URL/about/"
}