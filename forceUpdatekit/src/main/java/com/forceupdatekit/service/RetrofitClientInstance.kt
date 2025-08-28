package com.forceupdatekit.service

import com.forceupdatekit.service.RetrofitClientInstance.logging
import com.forceupdatekit.service.apiError.ThrowOnHttpErrorCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

private val TIME_OUT = 2L
private val MAX_RETRY = 3

object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://tauri.ir/api/"
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createOkHttpClient())
                    .addCallAdapterFactory(ThrowOnHttpErrorCallAdapterFactory())
                    .build()
            }
            return retrofit
        }

}


private fun createOkHttpClient(): OkHttpClient {
    val retryInterceptor = Interceptor { chain ->
        var tryCount = 0
        var lastException: Exception? = null

        val original = chain.request()
        val request = original.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer 123")
            .addHeader("platform", "android")
            .build()

        while (tryCount <= MAX_RETRY) {
            try {

                val respons = chain.proceed(request)

                return@Interceptor respons

            } catch (e: Exception) {
                if (e is UnknownHostException || e is SocketTimeoutException || e is ConnectException) {
                    lastException = e
                    tryCount++
                    if (tryCount > MAX_RETRY) break
                    Thread.sleep(2000)
                } else {
                    throw e
                }
            }
        }

        throw lastException ?: SocketTimeoutException("Request failed after $MAX_RETRY retries")
    }

    return OkHttpClient.Builder()
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(retryInterceptor)
        .addInterceptor(logging)
        .build()
}

