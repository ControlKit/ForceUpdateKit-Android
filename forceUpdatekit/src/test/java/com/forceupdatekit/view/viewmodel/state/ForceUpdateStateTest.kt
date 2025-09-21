package com.forceupdatekit.view.viewmodel.state

import com.forceupdatekit.service.model.CheckUpdateResponse
import com.sepanta.errorhandler.ApiError
import com.sepanta.errorhandler.IErrorEntity
import org.junit.Assert.*
import org.junit.Test

class ForceUpdateStateTest {

    @Test
    fun `Initial state should be correct`() {
        val state = ForceUpdateState.Initial
        assertTrue(state is ForceUpdateState.Initial)
    }

    @Test
    fun `NoUpdate state should be correct`() {
        val state = ForceUpdateState.NoUpdate
        assertTrue(state is ForceUpdateState.NoUpdate)
    }

    @Test
    fun `Update state should be correct`() {
        val state = ForceUpdateState.Update
        assertTrue(state is ForceUpdateState.Update)
    }

    @Test
    fun `SkipError state should be correct`() {
        val state = ForceUpdateState.SkipError
        assertTrue(state is ForceUpdateState.SkipError)
    }

    @Test
    fun `ShowView state should contain data`() {
        val response = CheckUpdateResponse(
            id = "1",
            version = "2.0.0",
            title = "Update Available",
            forceUpdate = true,
            description = "New features available"
        )
        val state = ForceUpdateState.ShowView(response)

        assertTrue(state is ForceUpdateState.ShowView)
        assertEquals(response, (state as ForceUpdateState.ShowView).data)
    }

    @Test
    fun `ShowView state should handle null data`() {
        val state = ForceUpdateState.ShowView(null)

        assertTrue(state is ForceUpdateState.ShowView)
        assertNull((state as ForceUpdateState.ShowView).data)
    }

    @Test
    fun `ShowViewError state should contain error`() {
        val error = ApiError<TestErrorEntity>(500, com.sepanta.errorhandler.ApiError.ErrorStatus.INTERNAL_SERVER_ERROR, TestErrorEntity("Network error"))
        val state = ForceUpdateState.ShowViewError(error)

        assertTrue(state is ForceUpdateState.ShowViewError)
        assertEquals(error, (state as ForceUpdateState.ShowViewError).data)
    }

    @Test
    fun `ShowViewError state should handle null error`() {
        val state = ForceUpdateState.ShowViewError(null)

        assertTrue(state is ForceUpdateState.ShowViewError)
        assertNull((state as ForceUpdateState.ShowViewError).data)
    }

    @Test
    fun `UpdateError state should contain error`() {
        val error = ApiError<TestErrorEntity>(400, com.sepanta.errorhandler.ApiError.ErrorStatus.BAD_REQUEST, TestErrorEntity("Update failed"))
        val state = ForceUpdateState.UpdateError(error)

        assertTrue(state is ForceUpdateState.UpdateError)
        assertEquals(error, (state as ForceUpdateState.UpdateError).data)
    }

    @Test
    fun `UpdateError state should handle null error`() {
        val state = ForceUpdateState.UpdateError(null)

        assertTrue(state is ForceUpdateState.UpdateError)
        assertNull((state as ForceUpdateState.UpdateError).data)
    }

    @Test
    fun `state equality should work correctly`() {
        val state1 = ForceUpdateState.Initial
        val state2 = ForceUpdateState.Initial
        val state3 = ForceUpdateState.NoUpdate

        assertEquals(state1, state2)
        assertNotEquals(state1, state3)
    }

    @Test
    fun `data states equality should work correctly`() {
        val response = CheckUpdateResponse(id = "1", version = "2.0.0")
        val state1 = ForceUpdateState.ShowView(response)
        val state2 = ForceUpdateState.ShowView(response)
        val state3 = ForceUpdateState.ShowView(null)

        assertEquals(state1, state2)
        assertNotEquals(state1, state3)
    }
}

// Test helper class for ApiError
data class TestErrorEntity(override val message: String) : IErrorEntity
