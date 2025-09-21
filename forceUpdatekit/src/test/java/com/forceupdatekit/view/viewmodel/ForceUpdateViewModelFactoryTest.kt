package com.forceupdatekit.view.viewmodel

import androidx.lifecycle.ViewModel
import com.forceupdatekit.service.ForceUpdateApi
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ForceUpdateViewModelFactoryTest {

    private val api: ForceUpdateApi = mockk()
    private val factory = ForceUpdateViewModelFactory(api)

    @Test
    fun `create should return ForceUpdateViewModel when modelClass is ForceUpdateViewModel`() {
        val viewModel = factory.create(ForceUpdateViewModel::class.java)

        assertEquals(ForceUpdateViewModel::class.java, viewModel::class.java)
    }

    @Test
    fun `create should throw IllegalArgumentException for unknown class`() {
        class DummyViewModel : ViewModel()

        assertThrows(IllegalArgumentException::class.java) {
            factory.create(DummyViewModel::class.java)
        }
    }
}