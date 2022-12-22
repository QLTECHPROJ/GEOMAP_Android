package com.geomap.utils

object Converter {
    @JvmStatic fun format(text : String?) : String {
        return if (text.isNullOrBlank()) "-" else text
    }
}