package com.midstatesrecycling.ktkalculator.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class KaratValue(
    @SerializedName("value")
    val value: Double?) : Parcelable