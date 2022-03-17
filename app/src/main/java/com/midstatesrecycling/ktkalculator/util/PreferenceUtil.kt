package com.midstatesrecycling.ktkalculator.util

import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.midstatesrecycling.ktkalculator.App

object PreferenceUtil {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext())

    var futureGoldPrice: String
        get() {
            return sharedPreferences.getString("FUTURE_GOLD_PRICE", "0.0") ?: "0.0"
        }
        set(value) {
            sharedPreferences.edit {
                putString("FUTURE_GOLD_PRICE", value)
            }
        }

}