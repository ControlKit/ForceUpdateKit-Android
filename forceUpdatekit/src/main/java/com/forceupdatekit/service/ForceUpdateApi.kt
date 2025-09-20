package com.forceupdatekit.service

import com.forceupdatekit.service.apiError.NetworkResult
import com.forceupdatekit.service.apiError.handleApi
import com.forceupdatekit.service.model.CheckUpdateResponse
import com.forceupdatekit.service.ApiService
import com.forceupdatekit.service.model.ApiCheckUpdateResponse
import com.forceupdatekit.service.model.SendActionResponse


class ForceUpdateApi(private val apiService: ApiService) {

    suspend fun getForceUpdateData(
        route: String,
        appId: String,
        version: String,
        deviceId: String,
        sdkVersion: String,
    ): NetworkResult<ApiCheckUpdateResponse> {
        return handleApi {
            apiService.getData(route,appId= appId,version= version, deviceId=deviceId, sdkVersion = sdkVersion)
        }
    }

    suspend fun setAction(
        route: String,
        appId: String,
        version: String,
        deviceId: String,
        sdkVersion: String,
        action: String,
    ): NetworkResult<SendActionResponse> {
        return handleApi {
            apiService.setAction(
                url = route,
                appId = appId,
                version = version,
                deviceId = deviceId,
                sdkVersion = sdkVersion,
                action = action,
            )
        }
    }
}
