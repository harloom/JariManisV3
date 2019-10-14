package com.app.jarimanis.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import androidx.annotation.RequiresApi
import com.snov.timeagolibrary.PrettyTimeAgo
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object TimeFormater {
    @SuppressLint("SimpleDateFormat")
    fun parse(d : Date) : String{
        val format = SimpleDateFormat("HH:mm")
        return  format.format(d)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    fun StringToDate(_s:String): String {
        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter =  SimpleDateFormat("dd-MM-yyyy")
        return formatter.format(parser.parse(_s))
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun converToLocalOreo() :String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)
        return  formatted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringToDate(timeStamp : String): LocalDate? {
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(timeStamp, formatter)
        return date
    }

//    @SuppressLint("SimpleDateFormat")
//    fun timeAgo(timeStamp: Long): String? {
//        val desing = SimpleDateFormat("h:mm a")
//
//        val dateMilisecond = PrettyTimeAgo.timestampToMilli(timeStamp,desing)
//        return PrettyTimeAgo.getTimeAgo(dateMilisecond)
//    }
}