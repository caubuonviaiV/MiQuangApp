package com.example.mrsmuarestaurant.helper

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    fun convertToVN(string: String): String {
        return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(Integer.valueOf(string))
    }

    fun convertToVN(value: Int): String {
        return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(value)
    }

    fun convertToVN(value: Boolean): String {
        return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(value)
    }

    fun setToolbar(activity: Activity, toolbar: Toolbar, title: String,) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun convertDate(tgl: String, format: String, fromatOld: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") :String{
        val dateFormat = SimpleDateFormat(fromatOld)
        val convert = dateFormat.parse(tgl)
        dateFormat.applyPattern(format)
        return dateFormat.format(convert)
    }
}