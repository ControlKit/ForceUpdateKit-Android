package com.forceupdatekit.view.viewmodel

import app.cash.turbine.test
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.service.ForceUpdateApi
import com.forceupdatekit.service.apiError.NetworkResult
import com.forceupdatekit.view.viewmodel.state.ForceUpdateState
import com.forceupdatekit.service.model.ApiCheckUpdateResponse
import com.forceupdatekit.service.model.ApiData
import com.forceupdatekit.service.model.LocalizedText
import com.sepanta.errorhandler.ApiError
import com.sepanta.errorhandler.IErrorEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForceUpdateViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ForceUpdateViewModel
    private val api: ForceUpdateApi = mockk()

    private val baseConfig = ForceUpdateServiceConfig(
        appId = "app-id",
        version = "1.0.0",
        deviceId = "device-id"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = ForceUpdateViewModel(api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test 
    fun `dismissDialog closes and emits forceUpdateEvent`() = runTest(dispatcher) {
        viewModel.showDialog()
        assertTrue(viewModel.openDialog.value)

        viewModel.forceUpdateEvent.test {
            viewModel.dismissDialog()
            assertFalse(viewModel.openDialog.value)
            advanceUntilIdle()
            assertEquals(Unit, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test 
    fun `getData success with update`() = runTest(dispatcher) {
        val fakeApiResponse = ApiCheckUpdateResponse(
            data = ApiData(
                id = "1",
                title = listOf(LocalizedText("en", "title")),
                description = listOf(LocalizedText("en", "description")),
                force = true,
                icon = null,
                link = null,
                button_title = null,
                cancel_button_title = null,
                version = listOf(LocalizedText("en", "1.0.0")),
                sdk_version = 1,
                minimum_version = "1.0.0",
                maximum_version = "2.0.0",
                created_at = null
            )
        )

        coEvery { api.getForceUpdateData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(fakeApiResponse)
        coEvery { api.setAction(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Success(null)

        viewModel.setConfig(baseConfig)
        viewModel.getData()
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state is ForceUpdateState.ShowView)
    }

    @Test
    fun `getData success with no update`() = runTest(dispatcher) {
        coEvery { api.getForceUpdateData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(null)

        viewModel.setConfig(baseConfig)
        viewModel.getData()
        advanceUntilIdle()

        assertEquals(ForceUpdateState.NoUpdate, viewModel.state.value)
    }

    @Test
    fun `getData error with skipException false returns Error`() = runTest(dispatcher) {
        val apiError = ApiError<TestErrorEntity>(0, com.sepanta.errorhandler.ApiError.ErrorStatus.UNKNOWN_ERROR, TestErrorEntity("Something went wrong"))
        coEvery { api.getForceUpdateData(any(), any(), any(), any(), any()) } returns NetworkResult.Error(apiError)

        val cfg = baseConfig.copy(skipException = false)
        viewModel.setConfig(cfg)
        viewModel.getData()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is ForceUpdateState.ShowViewError)
    }

    @Test
    fun `getData error with skipException true returns SkipError`() = runTest(dispatcher) {
        val apiError = ApiError<TestErrorEntity>(0, com.sepanta.errorhandler.ApiError.ErrorStatus.NO_CONNECTION, TestErrorEntity("Network down"))
        coEvery { api.getForceUpdateData(any(), any(), any(), any(), any()) } returns NetworkResult.Error(apiError)

        val cfg = baseConfig.copy(skipException = true)
        viewModel.setConfig(cfg)
        viewModel.getData()
        advanceUntilIdle()

        assertEquals(ForceUpdateState.SkipError, viewModel.state.value)
    }

    @Test
    fun `tryAgain should reset state and call getData`() = runTest(dispatcher) {
        coEvery { api.getForceUpdateData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(null)

        viewModel.setConfig(baseConfig)
        viewModel.getData()
        advanceUntilIdle()

        viewModel.tryAgain()
        advanceUntilIdle()

        assertEquals(ForceUpdateState.NoUpdate, viewModel.state.value)
    }

    @Test
    fun `clearState should reset to Initial`() {
        viewModel.clearState()
        assertEquals(ForceUpdateState.Initial, viewModel.state.value)
    }

    @Test
    fun `showDialog and dismissDialog should update openDialog`() = runTest(dispatcher) {
        viewModel.showDialog()
        assertTrue(viewModel.openDialog.value)

        viewModel.dismissDialog()
        assertFalse(viewModel.openDialog.value)
    }

    @Test
    fun `triggerForceUpdate should send event`() = runTest(dispatcher) {
        viewModel.forceUpdateEvent.test {
            viewModel.triggerForceUpdate()
            advanceUntilIdle()
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sendAction with UPDATE should set state to Update`() = runTest(dispatcher) {
        // First set itemId by getting data
        val fakeApiResponse = ApiCheckUpdateResponse(
            data = ApiData(
                id = "1",
                title = listOf(LocalizedText("en", "title")),
                description = listOf(LocalizedText("en", "description")),
                force = true,
                icon = null,
                link = null,
                button_title = null,
                cancel_button_title = null,
                version = listOf(LocalizedText("en", "1.0.0")),
                sdk_version = 1,
                minimum_version = "1.0.0",
                maximum_version = "2.0.0",
                created_at = null
            )
        )
        
        coEvery { api.getForceUpdateData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(fakeApiResponse)
        coEvery { api.setAction(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Success(null)

        viewModel.setConfig(baseConfig)
        viewModel.getData()
        advanceUntilIdle()
        
        // Now test sendAction
        viewModel.sendAction("UPDATE")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is ForceUpdateState.Update)
    }

    @Test
    fun `sendAction with UPDATE error should set state to UpdateError`() = runTest(dispatcher) {
        // First set itemId by getting data
        val fakeApiResponse = ApiCheckUpdateResponse(
            data = ApiData(
                id = "1",
                title = listOf(LocalizedText("en", "title")),
                description = listOf(LocalizedText("en", "description")),
                force = true,
                icon = null,
                link = null,
                button_title = null,
                cancel_button_title = null,
                version = listOf(LocalizedText("en", "1.0.0")),
                sdk_version = 1,
                minimum_version = "1.0.0",
                maximum_version = "2.0.0",
                created_at = null
            )
        )
        
        val apiError = ApiError<TestErrorEntity>(500, com.sepanta.errorhandler.ApiError.ErrorStatus.INTERNAL_SERVER_ERROR, TestErrorEntity("Update failed"))
        coEvery { api.getForceUpdateData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(fakeApiResponse)
        coEvery { api.setAction(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Error(apiError)

        viewModel.setConfig(baseConfig)
        viewModel.getData()
        advanceUntilIdle()
        
        // Now test sendAction
        viewModel.sendAction("UPDATE")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is ForceUpdateState.UpdateError)
    }

    @Test
    fun `submit should call sendAction with UPDATE and close dialog`() = runTest(dispatcher) {
        // First set itemId by getting data
        val fakeApiResponse = ApiCheckUpdateResponse(
            data = ApiData(
                id = "1",
                title = listOf(LocalizedText("en", "title")),
                description = listOf(LocalizedText("en", "description")),
                force = true,
                icon = null,
                link = null,
                button_title = null,
                cancel_button_title = null,
                version = listOf(LocalizedText("en", "1.0.0")),
                sdk_version = 1,
                minimum_version = "1.0.0",
                maximum_version = "2.0.0",
                created_at = null
            )
        )
        
        coEvery { api.getForceUpdateData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(fakeApiResponse)
        coEvery { api.setAction(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Success(null)

        viewModel.setConfig(baseConfig)
        viewModel.getData()
        advanceUntilIdle()
        
        viewModel.showDialog()
        assertTrue(viewModel.openDialog.value)

        viewModel.submit()
        advanceUntilIdle()

        assertFalse(viewModel.openDialog.value)
        coVerify { api.setAction(any(), any(), any(), any(), any(), "UPDATE") }
    }
}

// Test helper class for ApiError
data class TestErrorEntity(override val message: String) : IErrorEntity
