package com.midstatesrecycling.ktkalculator.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.Interceptor
import okhttp3.Response
import java.net.InetAddress

class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {


    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isConnectionOn()) {
            throw NoConnectivityException()
        } else if(!isInternetAvailable()) {
            throw NoInternetException()
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            !InetAddress.getByName("google.com").equals("")
        } catch (e: Exception) {
            false
        }
        //return try {
        //    val timeoutMs = 1500
        //    val sock = Socket()
        //    val sockaddr = InetSocketAddress("8.8.8.8", 53)
        //    sock.connect(sockaddr, timeoutMs)
        //    sock.close()
        //    true
        //} catch (e: IOException) {
        //    false
        //}
    }

    private fun isConnectionOn(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as
                    ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            postAndroidMInternetCheck(connectivityManager)
        } else {
            preAndroidMInternetCheck(connectivityManager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun postAndroidMInternetCheck(
        connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val connection =
            connectivityManager.getNetworkCapabilities(network)

        return connection != null && (
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    @Suppress("DEPRECATION")
    private fun preAndroidMInternetCheck(
        connectivityManager: ConnectivityManager): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null) {
            return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
        }
        return false
    }

}