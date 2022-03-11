package com.midstatesrecycling.ktkalculator.network

import com.midstatesrecycling.ktkalculator.model.KaratValue
import retrofit2.http.GET

private const val URL_KARAT_VALUE  = "service/getkaratvalue.php"

interface RestApiService {

    @GET(URL_KARAT_VALUE)
    suspend fun getKarateValue(): KaratValue

//    companion object {
//        const val BASE_URL = "http://www.midstatesrecycling.com/"
//
//        operator fun invoke(
//            client: okhttp3.Call.Factory
//        ): RestApiService {
//            return Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .callFactory(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create()
//        }
//
//        fun createDefaultOkHttpClient(
//            context: Context
//        ): OkHttpClient.Builder = OkHttpClient.Builder()
//            .cache(createDefaultCache(context))
//            .addInterceptor(createCacheControlInterceptor())
//
//        private fun createDefaultCache(
//            context: Context
//        ): Cache? {
//            val cacheDir = File(context.applicationContext.cacheDir.absolutePath, "/okhttp-api/")
//            if (cacheDir.mkdir() or cacheDir.isDirectory) {
//                return Cache(cacheDir, 1024 * 1024 * 10)
//            }
//            return null
//        }
//
//        private fun createCacheControlInterceptor(): Interceptor {
//            return Interceptor { chain ->
//                val modifiedRequest = chain.request().newBuilder()
//                    .addHeader(
//                        "Cache-Control",
//                        String.format(
//                            Locale.getDefault(),
//                            "max-age=31536000, max-stale=31536000"
//                        )
//                    ).build()
//                chain.proceed(modifiedRequest)
//            }
//        }
//    }
}