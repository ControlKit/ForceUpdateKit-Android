package com.forceupdatekit.service.apiError

import com.joon.fm.data.source.remote.ApiError


interface UseCaseResponse<Type> {

    fun onSuccess(result: Type)

    fun onError(apiError: ApiError?)
}
