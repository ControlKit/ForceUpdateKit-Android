package com.forceupdatekit.view.config

import com.forceupdatekit.view.ui.ForceUpdateViewFullScreen1
import com.forceupdatekit.view.ui.ForceUpdateViewFullScreen2
import com.forceupdatekit.view.ui.ForceUpdateViewFullScreen3
import com.forceupdatekit.view.ui.ForceUpdateViewFullScreen4
import com.forceupdatekit.view.ui.ForceUpdateViewPopover1
import com.forceupdatekit.view.ui.ForceUpdateViewPopover2
import org.junit.Assert.*
import org.junit.Test

class ForceUpdateViewStyleTest {

    @Test
    fun `checkViewStyle should return correct view for FullScreen1`() {
        val result = ForceUpdateViewStyle.checkViewStyle(ForceUpdateViewStyle.FullScreen1)
        
        assertTrue(result is ForceUpdateViewFullScreen1)
    }

    @Test
    fun `checkViewStyle should return correct view for FullScreen2`() {
        val result = ForceUpdateViewStyle.checkViewStyle(ForceUpdateViewStyle.FullScreen2)
        
        assertTrue(result is ForceUpdateViewFullScreen2)
    }

    @Test
    fun `checkViewStyle should return correct view for FullScreen3`() {
        val result = ForceUpdateViewStyle.checkViewStyle(ForceUpdateViewStyle.FullScreen3)
        
        assertTrue(result is ForceUpdateViewFullScreen3)
    }

    @Test
    fun `checkViewStyle should return correct view for FullScreen4`() {
        val result = ForceUpdateViewStyle.checkViewStyle(ForceUpdateViewStyle.FullScreen4)
        
        assertTrue(result is ForceUpdateViewFullScreen4)
    }

    @Test
    fun `checkViewStyle should return correct view for Popover1`() {
        val result = ForceUpdateViewStyle.checkViewStyle(ForceUpdateViewStyle.Popover1)
        
        assertTrue(result is ForceUpdateViewPopover1)
    }

    @Test
    fun `checkViewStyle should return correct view for Popover2`() {
        val result = ForceUpdateViewStyle.checkViewStyle(ForceUpdateViewStyle.Popover2)
        
        assertTrue(result is ForceUpdateViewPopover2)
    }

    @Test
    fun `checkViewStyle should return different instances for different styles`() {
        val result1 = ForceUpdateViewStyle.checkViewStyle(ForceUpdateViewStyle.FullScreen1)
        val result2 = ForceUpdateViewStyle.checkViewStyle(ForceUpdateViewStyle.FullScreen2)
        
        assertNotEquals(result1, result2)
        assertNotEquals(result1::class.java, result2::class.java)
    }

    @Test
    fun `checkViewStyle should return same type for same style`() {
        val result1 = ForceUpdateViewStyle.checkViewStyle(ForceUpdateViewStyle.Popover1)
        val result2 = ForceUpdateViewStyle.checkViewStyle(ForceUpdateViewStyle.Popover1)
        
        assertEquals(result1::class.java, result2::class.java)
    }

    @Test
    fun `checkViewStyle should return ForceUpdateViewContract implementation`() {
        val allStyles = listOf(
            ForceUpdateViewStyle.FullScreen1,
            ForceUpdateViewStyle.FullScreen2,
            ForceUpdateViewStyle.FullScreen3,
            ForceUpdateViewStyle.FullScreen4,
            ForceUpdateViewStyle.Popover1,
            ForceUpdateViewStyle.Popover2
        )
        
        allStyles.forEach { style ->
            val result = ForceUpdateViewStyle.checkViewStyle(style)
            assertTrue(result is ForceUpdateViewContract)
        }
    }

    @Test
    fun `enum values should have correct string values`() {
        assertEquals("FullScreen1", ForceUpdateViewStyle.FullScreen1.name)
        assertEquals("FullScreen2", ForceUpdateViewStyle.FullScreen2.name)
        assertEquals("FullScreen3", ForceUpdateViewStyle.FullScreen3.name)
        assertEquals("FullScreen4", ForceUpdateViewStyle.FullScreen4.name)
        assertEquals("Popover1", ForceUpdateViewStyle.Popover1.name)
        assertEquals("Popover2", ForceUpdateViewStyle.Popover2.name)
    }

    @Test
    fun `enum should have all expected values`() {
        val expectedValues = listOf(
            ForceUpdateViewStyle.FullScreen1,
            ForceUpdateViewStyle.FullScreen2,
            ForceUpdateViewStyle.FullScreen3,
            ForceUpdateViewStyle.FullScreen4,
            ForceUpdateViewStyle.Popover1,
            ForceUpdateViewStyle.Popover2
        )
        
        val actualValues = ForceUpdateViewStyle.values().toList()
        
        assertEquals(expectedValues.size, actualValues.size)
        expectedValues.forEach { expected ->
            assertTrue(actualValues.contains(expected))
        }
    }
}
