package com.example.testcreatelibrary.ui.view.data

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.UUID
import java.util.concurrent.TimeUnit
private val TIME_OUT = 2L
private val MAX_RETRY = 3

object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "http://135.181.38.185:8001/api/"
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createOkHttpClient())
                    .build()
            }
            return retrofit
        }

}
private fun createOkHttpClient(): OkHttpClient {
    var retryIndex = 0
    val i = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        requestBuilder.addHeader("Accept", "application/json")
        requestBuilder.addHeader("Authorization", "Bearer 123")
        requestBuilder.addHeader("platform", "android")
        val request = requestBuilder.build()
        try {

            val response = chain.proceed(request)

            if(response.isSuccessful){
                return@Interceptor response

            }else{
                Thread.sleep(2000)
                if (retryIndex < MAX_RETRY) {
                    retryIndex++
                    return@Interceptor chain.call().clone().execute()
                } else {
                    retryIndex = 0
                    return@Interceptor response

                }
            }


        } catch (e: Exception) {
            if(e is UnknownHostException || e is SocketTimeoutException ||e is ConnectException){
                Thread.sleep(2000)
                if (retryIndex < MAX_RETRY) {
                    retryIndex++
                    return@Interceptor chain.call().clone().execute()
                } else {
                    retryIndex = 0
                    throw SocketTimeoutException()
                }
            }  else  {
                throw Exception(e)
            }


        }
    }


    return OkHttpClient.Builder().writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS).connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(i)
        .build()
}
