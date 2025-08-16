package com.forceupdatekit.service.apiError

import com.joon.fm.data.source.remote.ApiError

sealed class NetworkResult<out T> {
    data class Success<out T>(val value: T?): NetworkResult<T>()
    data class Error( val error: ApiError? = null): NetworkResult<Nothing>()
}