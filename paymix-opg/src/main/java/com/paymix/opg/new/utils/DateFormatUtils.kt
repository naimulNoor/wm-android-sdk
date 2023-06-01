package com.walletmix.paymixbusiness.utils

import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DateFormatUtils {

    companion object {
        val shared: DateFormatUtils by lazy { DateFormatUtils() }
    }

    private val preFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var dateFormat = "dd MMM, yyyy hh:mm a"
    private val dateTimeFormatter = SimpleDateFormat(dateFormat)

    /**
     * MM = Only month number
     * MMM = Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec (1st 3 letter of Month)
     * MMMM = Jaunary, February, March ..... (full month name)
     *
     * EEE = Sat, Sun, Mon ..... ( 1st 3 letters of day)
     * EEEE = Saturday, Sunday, Monday....(full day)
     *
     * HH = 24 hours format
     * hh = 12 hours format
     *
     * */

    fun format(
        fromFormat: String = "yyyy-MM-dd HH:mm:ss", // 2020-04-22 22:40:12
        dateToFormat: String,
        toFormat: String = "dd MMM, yyyy, hh:mm:a" // 22 Apr, 2020 10:40 PM
    ): String {
        val inFormat = SimpleDateFormat(fromFormat, Locale.ENGLISH)
        val outFormat = SimpleDateFormat(toFormat, Locale.ENGLISH)
        var parsedDate: Date? = null
        try {
            parsedDate = inFormat.parse(dateToFormat)
        } catch (e: Exception) {
        }
        return if (parsedDate == null) "" else outFormat.format(parsedDate)
    }

    fun formatUTCtime(dateToFormat: String): Pair<String, String> {
        val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
        inFormat.timeZone = TimeZone.getTimeZone("UTC")
        var parsedDate: Date? = null
        try {
            parsedDate = inFormat.parse(dateToFormat)
        } catch (e: Exception) {
        }
        if (parsedDate == null) return Pair("", "")

        var outFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val date = outFormat.format(parsedDate)
        outFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val time = outFormat.format(parsedDate)
        return Pair(date, time)
    }

    fun formatUTCtimeToDate(dateToFormat: String): String {
        val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
        inFormat.timeZone = TimeZone.getTimeZone("UTC")
        var parsedDate: Date? = null
        try {
            parsedDate = inFormat.parse(dateToFormat)
        } catch (e: Exception) {
        }
        val outFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
        return outFormat.format(parsedDate)
    }

    fun format(
        fromFormat: String = "yyyy-MM-dd HH:mm:ss",
        dateToFormat: String
    ): Pair<String, String> {
        val inFormat = SimpleDateFormat(fromFormat, Locale.ENGLISH)

        var parsedDate: Date? = null
        try {
            parsedDate = inFormat.parse(dateToFormat)
        } catch (e: Exception) {
        }
        if (parsedDate == null) return Pair("", "")

        var outFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
        val date = outFormat.format(parsedDate)
        outFormat = SimpleDateFormat("hh:mm:a", Locale.ENGLISH)
        val time = outFormat.format(parsedDate)
        return Pair(date, time)
    }

    fun convertUnixTimeToDate(unixSeconds: Long, toFormat: String = "hh:mm a"): String {
        val date = Date(unixSeconds * 1000)
        val outFormat = SimpleDateFormat(toFormat, Locale.getDefault())  // 12:00:00 pm
        return outFormat.format(date)
    }

    fun convertMilliSecondsToDate(milliSeconds: Long, toFormat: String = "hh:mm a"): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        val outFormat = SimpleDateFormat(toFormat, Locale.getDefault())  // 12:00:00 pm
        outFormat.timeZone = TimeZone.getTimeZone("UTC")
        return outFormat.format(calendar.timeInMillis)
    }

    fun convertToDateTimeArray(
        fromFormat: String = "dd-MM-yyyy HH:mm:ss",
        toDateFormat: String = "dd MMM, yyy", // 31 Jan, 2018
        toTimeFormat: String = "hh:mm a",  // 12:00:00 pm
        dateToFormat: String
    ): Array<String> {
        val inFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
        val outFormatDate = SimpleDateFormat(toDateFormat, Locale.getDefault())
        val outFormatTime = SimpleDateFormat(toTimeFormat, Locale.getDefault())
        var parsedDate: Date? = null
        try {
            parsedDate = inFormat.parse(dateToFormat)
        } catch (e: ParseException) {
        }
        return if (parsedDate != null)
            arrayOf(outFormatDate.format(parsedDate), outFormatTime.format(parsedDate))
        else
            arrayOf("", "")
    }

    fun getTodayName(toFormat: String = "EEE"): String { // EEE = Sat, Sun, Mon ...
        val outFormat = SimpleDateFormat(toFormat, Locale.getDefault())
        return outFormat.format(Calendar.getInstance())
    }

    fun getCurrentMonthName(toFormat: String = "MMM"): String { // MMM = Jan, Feb ...
        val outFormat = SimpleDateFormat(toFormat, Locale.getDefault())
        return outFormat.format(Calendar.getInstance())
    }


    fun getTime(dateStr: String): String {
        val newFormatTime = SimpleDateFormat("hh:mm a", Locale.getDefault())  // 12:00:00 pm
        var time = ""
        try {
            time = newFormatTime.format(preFormat.parse(dateStr))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return time
    }

    fun getDate(dateStr: String): String {
        val newFormatDate =
            SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) // Format 31 Jan, 2018
        var date = ""
        try {
            date = newFormatDate.format(preFormat.parse(dateStr))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    fun convertMilliSecondsToTime(milliSeconds: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        val ssFormatter = SimpleDateFormat("hh:mm")  // 12:00:00 pm
        ssFormatter.timeZone = TimeZone.getTimeZone("UTC")
        return ssFormatter.format(calendar.timeInMillis)
    }

    fun convertUnixToTime(unixSeconds: Long): String {
        val date = Date(unixSeconds * 1000)
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())  // 12:00:00 pm
        // sdf.timeZone = TimeZone.getTimeZone("GMT+06:00")
        return sdf.format(date)
    }
}
