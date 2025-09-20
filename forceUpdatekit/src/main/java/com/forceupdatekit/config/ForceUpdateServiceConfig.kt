package com.forceupdatekit.config

import com.forceupdatekit.view.config.ForceUpdateViewConfig

data class ForceUpdateServiceConfig(
    var viewConfig: ForceUpdateViewConfig = ForceUpdateViewConfig(),
    var version: String,
    var appId: String,
    var deviceId: String = "1",
    var skipException: Boolean = false,
    var timeOut: Long = 5000L,
    var timeRetryThreadSleep: Long = 1000L,
    var maxRetry: Int = 5,
    var canDismissRetryView: Boolean = false,
    var lang: String= "en"
)