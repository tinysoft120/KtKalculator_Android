package com.midstatesrecycling.ktkalculator.util

import android.util.Log

object LogU {
    fun NSLog(msgFormat: String, vararg args: Any?) {
        val msg = String.format(msgFormat, *args)
        Log.d("CalcApp::Print", msg)
    }

    fun d(tag: String, msgFormat: String, vararg args: Any?) {
        val msg = String.format(msgFormat, *args)
        Log.d("CalcApp::$tag", msg)
    }

    fun e(tag: String, msgFormat: String, vararg args: Any?) {
        val msg = String.format(msgFormat, *args)
        Log.e("CalcApp::$tag", msg)
    }
}