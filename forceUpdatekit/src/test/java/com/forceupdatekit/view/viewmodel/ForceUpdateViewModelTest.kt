package com.forceupdatekit.view.viewmodel

import app.cash.turbine.test
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.service.ForceUpdateApi
import com.forceupdatekit.service.apiError.NetworkResult
import com.forceupdatekit.view.viewmodel.state.ForceUpdateState
import com.forceupdatekit.service.apiError.ApiError
import com.forceupdatekit.service.model.ApiCheckUpdateResponse
import com.forceupdatekit.service.model.ApiData
import com.forceupdatekit.service.model.CheckUpdateResponse
import com.forceupdatekit.service.model.LocalizedText
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForceUpdateViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ForceUpdateViewModel
    private val api: ForceUpdateApi = mockk()

    private val config = ForceUpdateServiceConfig(
        route = "test/route",
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

    @Test fun `dismissDialog closes and emits forceUpdateEvent`() = runTest(dispatcher) {
        // اول show
        viewModel.showDialog()
        assertTrue(viewModel.openDialog.value)

        // تست event با Turbine
        viewModel.forceUpdateEvent.test {
            viewModel.dismissDialog()
            assertFalse(viewModel.openDialog.value)
            advanceUntilIdle()
            assertEquals(Unit, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `setConfig fetches once and returns Update`() = runTest(dispatcher) {
        val resp = ApiCheckUpdateResponse(
            ApiData(
                id = "1",
                title = listOf(LocalizedText("en", "Title")),
                description = listOf(LocalizedText("en", "Desc")),
                force = true,
                icon = null,
                link = null,
                button_title = null,
                cancel_button_title = null,
                version = listOf(LocalizedText("en","1.0.0")),
                sdk_version = 33,
                minimum_version = "1.0.0",
                maximum_version = "2.0.0",
                created_at = null
            )
        )
        coEvery { api.getForceUpdateData(any(), any(), any(), any(),any()) } returns NetworkResult.Success(resp)

        viewModel.setConfig(config)
        advanceUntilIdle()

        val s1 = viewModel.state.value
        assert(s1 is ForceUpdateState.Update)

        // call again: چون state دیگه Initial نیست نباید دوباره API صدا بخوره
        viewModel.setConfig(config)
        advanceUntilIdle()
        coVerify(exactly = 1) { api.getForceUpdateData(any(), any(), any(), any(),any()) }
    }
    @Test fun `success with null body  NoUpdate`() = runTest(dispatcher) {
        coEvery { api.getForceUpdateData(any(), any(), any(), any(),any()) } returns NetworkResult.Success(null)
        viewModel.setConfig(config)
        advanceUntilIdle()
        assertEquals(ForceUpdateState.NoUpdate, viewModel.state.value)
    }

    @Test
    fun `setConfig should call getData`() = runTest(dispatcher) {
        coEvery { api.getForceUpdateData(any(), any(), any(), any(),any()) } returns NetworkResult.Success(null)

        viewModel.setConfig(config)
        advanceUntilIdle()

        assertEquals(ForceUpdateState.NoUpdate, viewModel.state.value)
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

        coEvery { api.getForceUpdateData(any(), any(), any(), any(),any()) } returns NetworkResult.Success(fakeApiResponse)


        viewModel.setConfig(config)
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state is ForceUpdateState.Update)
    }

    @Test
    fun `getData success with no update`() = runTest(dispatcher) {
        coEvery { api.getForceUpdateData(any(), any(), any(), any(),any()) } returns NetworkResult.Success(null)

        viewModel.setConfig(config)
        advanceUntilIdle()

        assertEquals(ForceUpdateState.NoUpdate, viewModel.state.value)
    }

    @Test
    fun `getData error`() = runTest(dispatcher) {
        val apiError = ApiError("Something went wrong", 0, ApiError.ErrorStatus.UNKNOWN_ERROR)
        coEvery { api.getForceUpdateData(any(), any(), any(), any(),any()) } returns NetworkResult.Error(apiError)

        viewModel.setConfig(config)
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state is ForceUpdateState.Error)
    }

    @Test
    fun `tryAgain should reset state and call getData`() = runTest(dispatcher) {
        coEvery { api.getForceUpdateData(any(), any(), any(), any(),any()) } returns NetworkResult.Success(null)

        viewModel.setConfig(config)
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
//    Integration
@Test
fun `api returns valid update should set state to Update`() = runTest(dispatcher) {
    val apiResponse = ApiCheckUpdateResponse(
        data = ApiData(
            id = "1",
            title = listOf(LocalizedText("en", "New Version")),
            description = listOf(LocalizedText("en", "Update available")),
            force = true,
            icon = null,
            link = null,
            button_title = listOf(LocalizedText("en", "Update Now")),
            cancel_button_title = listOf(LocalizedText("en", "Later")),
            version = listOf(LocalizedText("en", "2.0.0")),
            sdk_version = 33,
            minimum_version = "1.0.0",
            maximum_version = "3.0.0",
            created_at = "2025-08-28"
        )
    )

    coEvery { api.getForceUpdateData(any(), any(), any(), any(),any()) } returns NetworkResult.Success(apiResponse)

    viewModel.setConfig(config)
    advanceUntilIdle()

    val state = viewModel.state.value
    assertTrue(state is ForceUpdateState.Update)

    val updateState = state as ForceUpdateState.Update
    assertEquals("New Version", updateState.data?.title) // اینجا data همون CheckUpdateResponse شده
}


    @Test
    fun `api returns empty data should set state to Update with null fields`() = runTest(dispatcher) {
        val apiResponse = ApiCheckUpdateResponse(
            data = ApiData(
                id = null,
                title = null,
                description = null,
                force = null,
                icon = null,
                link = null,
                button_title = null,
                cancel_button_title = null,
                version = null,
                sdk_version = null,
                minimum_version = null,
                maximum_version = null,
                created_at = null
            )
        )

        coEvery { api.getForceUpdateData(any(), any(), any(), any(),any()) } returns NetworkResult.Success(apiResponse)

        viewModel.setConfig(config)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is ForceUpdateState.Update)

        val updateState = state as ForceUpdateState.Update
        assertEquals(null, updateState.data?.version)
        assertEquals(null, updateState.data?.title)
        assertEquals(null, updateState.data?.forceUpdate)
    }


}
