package com.forceupdatekit.service

import com.forceupdatekit.service.apiError.NetworkResult
import com.forceupdatekit.service.model.ApiCheckUpdateResponse
import com.forceupdatekit.service.model.ApiData
import com.forceupdatekit.service.model.LocalizedText
import com.forceupdatekit.service.model.SendActionResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ForceUpdateApiTest {

    private lateinit var api: ForceUpdateApi
    private val apiService: ApiService = mockk()

    @Before
    fun setup() {
        api = ForceUpdateApi(apiService)
    }

    @Test
    fun `getForceUpdateData should call apiService with correct parameters`() = runTest {
        val response = ApiCheckUpdateResponse(
            ApiData(
                id = "1",
                title = listOf(LocalizedText("en", "Title")),
                description = listOf(LocalizedText("en", "Description")),
                force = true,
                icon = null,
                link = null,
                button_title = null,
                cancel_button_title = null,
                version = listOf(LocalizedText("en", "1.0.0")),
                sdk_version = 1,
                minimum_version = null,
                maximum_version = null,
                created_at = null
            )
        )

        coEvery { 
            apiService.getData(
                url = any(),
                appId = any(),
                version = any(),
                sdkVersion = any(),
                deviceId = any()
            ) 
        } returns retrofit2.Response.success(response)

        val result = api.getForceUpdateData(
            route = "test/route",
            appId = "test-app-id",
            version = "1.0.0",
            deviceId = "test-device-id",
            sdkVersion = "0.0.2"
        )

        assertTrue(result is NetworkResult.Success)
        assertEquals(response, (result as NetworkResult.Success).value)

        coVerify { 
            apiService.getData(
                url = "test/route",
                appId = "test-app-id",
                version = "1.0.0",
                sdkVersion = "0.0.2",
                deviceId = "test-device-id"
            ) 
        }
    }

    @Test
    fun `getForceUpdateData should return Error on exception`() = runTest {
        coEvery { 
            apiService.getData(
                url = any(),
                appId = any(),
                version = any(),
                sdkVersion = any(),
                deviceId = any()
            ) 
        } throws RuntimeException("Network error")

        val result = api.getForceUpdateData(
            route = "test/route",
            appId = "test-app-id",
            version = "1.0.0",
            deviceId = "test-device-id",
            sdkVersion = "0.0.2"
        )

        assertTrue(result is NetworkResult.Error)
        val error = (result as NetworkResult.Error).error
        assertNotNull(error)
    }

    @Test
    fun `setAction should call apiService with correct parameters`() = runTest {
        val response = SendActionResponse()

        coEvery { 
            apiService.setAction(
                url = any(),
                appId = any(),
                version = any(),
                sdkVersion = any(),
                deviceId = any(),
                action = any()
            ) 
        } returns retrofit2.Response.success(response)

        val result = api.setAction(
            route = "test/route/123",
            appId = "test-app-id",
            version = "1.0.0",
            deviceId = "test-device-id",
            sdkVersion = "0.0.2",
            action = "UPDATE"
        )

        assertTrue(result is NetworkResult.Success)
        assertEquals(response, (result as NetworkResult.Success).value)

        coVerify { 
            apiService.setAction(
                url = "test/route/123",
                appId = "test-app-id",
                version = "1.0.0",
                sdkVersion = "0.0.2",
                deviceId = "test-device-id",
                action = "UPDATE"
            ) 
        }
    }

    @Test
    fun `setAction should return Error on exception`() = runTest {
        coEvery { 
            apiService.setAction(
                url = any(),
                appId = any(),
                version = any(),
                sdkVersion = any(),
                deviceId = any(),
                action = any()
            ) 
        } throws RuntimeException("Network error")

        val result = api.setAction(
            route = "test/route/123",
            appId = "test-app-id",
            version = "1.0.0",
            deviceId = "test-device-id",
            sdkVersion = "0.0.2",
            action = "UPDATE"
        )

        assertTrue(result is NetworkResult.Error)
        val error = (result as NetworkResult.Error).error
        assertNotNull(error)
    }
}
