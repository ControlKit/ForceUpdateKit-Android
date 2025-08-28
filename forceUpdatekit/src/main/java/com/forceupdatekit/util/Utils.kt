package com.forceupdatekit.util

import androidx.compose.ui.platform.UriHandler

object Utils {


     fun openLink(url: String?, uriHandler: UriHandler) {
        url?.let { uriHandler.openUri(it) }
    }


}