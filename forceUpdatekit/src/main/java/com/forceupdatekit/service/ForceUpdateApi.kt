package com.forceupdatekit.service

import com.forceupdatekit.service.apiError.NetworkResult
import com.forceupdatekit.service.apiError.handleApi
import com.forceupdatekit.service.model.CheckUpdateResponse
import com.forceupdatekit.service.ApiService
import com.forceupdatekit.service.model.ApiCheckUpdateResponse


class ForceUpdateApi(private val apiService: ApiService) {

    suspend fun getForceUpdateData(
        route: String,
        appId: String,
        version: String,
        deviceId: String,
        sdkVersion: String
    ): NetworkResult<ApiCheckUpdateResponse> {
        return handleApi {
            apiService.getData(route, appId, version, deviceId,sdkVersion)
        }
    }
}
