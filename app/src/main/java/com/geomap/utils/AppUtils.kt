package com.geomap.utils

import android.app.Application

object AppUtils : Application() {
    const val MAIN_URL = "https://geomap.in"
    const val STAGING_MAIN_URL = "https://geomap.in/wp-json/CS/STAGING/API/v1/"
    const val LIVE_MAIN_URL = "https://geomap.in/wp-json/CS/STAGING/API/v1/"
    const val New_BASE_URL = STAGING_MAIN_URL
    var tncs_url = "$MAIN_URL/term_condition.php"
    var privacy_policy_url = "$MAIN_URL/privacy.php"
    var about_us = "$MAIN_URL/about-us/"
    var chat_Support_url = "$MAIN_URL/chatbot.php"
    var how_refer_works_url = "$MAIN_URL/how-refer-works/"
}