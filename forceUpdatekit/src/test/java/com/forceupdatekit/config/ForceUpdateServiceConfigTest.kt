package com.forceupdatekit.config

import com.forceupdatekit.view.config.ForceUpdateViewConfig
import org.junit.Assert.*
import org.junit.Test

class ForceUpdateServiceConfigTest {

    @Test
    fun `default values should be set correctly`() {
        val config = ForceUpdateServiceConfig(
            version = "1.0.0",
            appId = "test-app-id"
        )

        assertEquals("1.0.0", config.version)
        assertEquals("test-app-id", config.appId)
        assertEquals("1", config.deviceId)
        assertEquals(false, config.skipException)
        assertEquals(5000L, config.timeOut)
        assertEquals(1000L, config.timeRetryThreadSleep)
        assertEquals(5, config.maxRetry)
        assertEquals(false, config.canDismissRetryView)
        assertEquals("en", config.lang)
        assertNotNull(config.viewConfig)
    }

    @Test
    fun `custom values should be set correctly`() {
        val viewConfig = ForceUpdateViewConfig()
        val config = ForceUpdateServiceConfig(
            viewConfig = viewConfig,
            version = "2.0.0",
            appId = "custom-app-id",
            deviceId = "custom-device-id",
            skipException = true,
            timeOut = 10000L,
            timeRetryThreadSleep = 2000L,
            maxRetry = 3,
            canDismissRetryView = true,
            lang = "fa"
        )

        assertEquals(viewConfig, config.viewConfig)
        assertEquals("2.0.0", config.version)
        assertEquals("custom-app-id", config.appId)
        assertEquals("custom-device-id", config.deviceId)
        assertEquals(true, config.skipException)
        assertEquals(10000L, config.timeOut)
        assertEquals(2000L, config.timeRetryThreadSleep)
        assertEquals(3, config.maxRetry)
        assertEquals(true, config.canDismissRetryView)
        assertEquals("fa", config.lang)
    }

    @Test
    fun `copy should create new instance with updated values`() {
        val originalConfig = ForceUpdateServiceConfig(
            version = "1.0.0",
            appId = "test-app-id"
        )

        val copiedConfig = originalConfig.copy(
            version = "2.0.0",
            skipException = true
        )

        assertEquals("2.0.0", copiedConfig.version)
        assertEquals("test-app-id", copiedConfig.appId)
        assertEquals(true, copiedConfig.skipException)
        assertEquals(false, originalConfig.skipException) // Original should remain unchanged
    }
}
