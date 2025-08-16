package com.forceupdatekit.service.apiError

import android.util.Log
import com.joon.fm.convertErrorBody
import com.joon.fm.core.base.baseUsecase.traceErrorException
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any> handleApi(execute: suspend () -> Response<T>?): NetworkResult<T> {
    return try {
        val response = execute()
        if (response?.isSuccessful == true) {
            NetworkResult.Success(response.body())
        } else {
            NetworkResult.Error(convertErrorBody(response?.errorBody()?.string(), response?.code()))
        }

    } catch (e: Exception) {

        NetworkResult.Error(traceErrorException(e))

    }

}