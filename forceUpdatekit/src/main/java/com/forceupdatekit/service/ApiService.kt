package com.example.testcreatelibrary.ui.view.data

import com.example.demoapp.models.CheckUpdateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET()
    suspend fun getData(
        @Url url: String,
//        @Query("lastForceUpdateId") lastId: String?,
        @Header("x-app-id") appId: String?,
        @Header("x-version") version: String,
        @Header("x-lang") language: String?,
        @Header("x-device-uuid") deviceId: String?,
    ): Response<CheckUpdateResponse>
}