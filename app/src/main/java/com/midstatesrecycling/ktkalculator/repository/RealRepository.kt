package com.midstatesrecycling.ktkalculator.repository

import android.content.Context
import com.midstatesrecycling.ktkalculator.model.KaratValue
import com.midstatesrecycling.ktkalculator.network.ApiResult
import com.midstatesrecycling.ktkalculator.network.RestApiService

class RealRepository(
    private val context: Context,
    private val apiService: RestApiService) {

//    suspend fun connectToNetwork(): Boolean {
//        return try {
//            val response = connectApi.connectToGoogle()
//            true
//        } catch (e: Exception) {
//            println(e)
//            false
//        }
//    }

    suspend fun karatValue(): ApiResult<Double> {
        return try {
            val response: KaratValue = apiService.getKarateValue()
            if (response.value != null) {
                ApiResult.Success(response.value)
            } else {
                ApiResult.Error(NullPointerException())
            }
        } catch (e: Exception) {
            println(e)
            ApiResult.Error(e)
        }
    }
}