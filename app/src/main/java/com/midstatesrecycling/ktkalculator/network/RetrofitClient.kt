package com.midstatesrecycling.ktkalculator.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.midstatesrecycling.ktkalculator.App
import com.midstatesrecycling.ktkalculator.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

fun provideDefaultCache(): Cache? {
    val cacheDir = File(App.getContext().cacheDir.absolutePath, "/okhttp-restapi/")
    if (cacheDir.mkdirs() || cacheDir.isDirectory) {
        return Cache(cacheDir, 1024 * 1024 * 10)
    }
    return null
}

fun logInterceptor(): Interceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        // disable retrofit log on release
        loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    return loggingInterceptor
}

fun connectionInterceptor(context: Context): Interceptor {
    return NetworkConnectionInterceptor(context)
}

fun headerInterceptor(context: Context): Interceptor {
    return Interceptor {
        val original = it.request()
        val request = original.newBuilder()
            .header("User-Agent", context.packageName)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .method(original.method, original.body)
            .build()
        it.proceed(request)
    }
}

fun provideOkHttp(context: Context, cache: Cache): OkHttpClient {
    return OkHttpClient.Builder()
        .addNetworkInterceptor(logInterceptor())
        .addInterceptor(connectionInterceptor(context))
        //.addInterceptor(headerInterceptor(context))
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .cache(cache)
        .build()
}

fun provideRestApi(client: OkHttpClient): RestApiService {
    val gson = GsonBuilder()
        .setLenient()
        .create()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.midstatesrecycling.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .callFactory { request -> client.newCall(request) }
        .build()
    return retrofit.create(RestApiService::class.java)
}