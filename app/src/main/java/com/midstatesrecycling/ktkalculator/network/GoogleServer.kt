package com.midstatesrecycling.ktkalculator.network

import retrofit2.Call
import retrofit2.http.POST

interface GoogleServer {

    @POST("/")
    suspend fun connectToGoogle(): Call<String>
}