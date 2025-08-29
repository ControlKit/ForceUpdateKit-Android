package com.forceupdatekit.view.viewmodel

import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.service.ForceUpdateApi
import com.forceupdatekit.service.apiError.ApiError
import com.forceupdatekit.service.apiError.NetworkResult
import com.forceupdatekit.view.viewmodel.state.ForceUpdateState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class ForceUpdateViewModelParamTest(private val status: ApiError.ErrorStatus) {

    private val testDispatcher = StandardTestDispatcher()
    private val api: ForceUpdateApi = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val baseConfig = ForceUpdateServiceConfig(
        route = "test/route",
        appId = "app-id",
        version = "1.0.0",
        deviceId = "device-id",
        skipException = true
    )

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "ErrorStatus = {0}")
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf(ApiError.ErrorStatus.BAD_REQUEST),
            arrayOf(ApiError.ErrorStatus.UNAUTHORIZED),
            arrayOf(ApiError.ErrorStatus.FORBIDDEN),
            arrayOf(ApiError.ErrorStatus.NOT_FOUND),
            arrayOf(ApiError.ErrorStatus.METHOD_NOT_ALLOWED),
            arrayOf(ApiError.ErrorStatus.CONFLICT),
            arrayOf(ApiError.ErrorStatus.DATA_ERROR),
            arrayOf(ApiError.ErrorStatus.INTERNAL_SERVER_ERROR),
            arrayOf(ApiError.ErrorStatus.TIMEOUT),
            arrayOf(ApiError.ErrorStatus.NO_CONNECTION),
            arrayOf(ApiError.ErrorStatus.UNKNOWN_ERROR),
        )
    }

    @Test
    fun `getData error with skipException true should always return SkipError`() = runTest {
        val apiError = ApiError("some error", 500, status)
        coEvery { api.getForceUpdateData(any(), any(), any(), any(), any()) } returns NetworkResult.Error(apiError)

        val viewModel = ForceUpdateViewModel(api)
        viewModel.setConfig(baseConfig)

        // حالا coroutine ها اجرا میشن
        advanceUntilIdle()

        assertEquals(ForceUpdateState.SkipError, viewModel.state.value)
    }
}