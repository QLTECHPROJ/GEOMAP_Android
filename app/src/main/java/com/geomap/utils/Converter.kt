package com.geomap.utils
import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Converter {
    @JvmStatic fun format(text : String?) : String {
        return if (text.isNullOrBlank()) "-" else text
    }
    @JvmStatic fun formatDateList(text : String) : String {
        val s = convertedFormat(text,
            CONSTANTS.DATE_MONTH_YEAR_FORMAT_z_D, CONSTANTS.SERVER_TIME_FORMAT)
        return s.toString()
    }

    @JvmStatic
    open fun convertedFormat(DisplayDate: String?, output: String?, input: String?): String? {
        val outputFormat: DateFormat = SimpleDateFormat(output,Locale.getDefault())
        val inputFormat: DateFormat = SimpleDateFormat(input,Locale.getDefault())
        var date: Date? = inputFormat.parse(DisplayDate.toString())
        val outputText : String= outputFormat.format(date)
        return outputText
    }
}