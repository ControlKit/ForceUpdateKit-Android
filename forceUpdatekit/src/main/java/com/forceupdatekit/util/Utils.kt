package com.forceupdatekit.util

import androidx.compose.ui.platform.UriHandler
import com.forceupdatekit.service.model.LocalizedText
import java.util.Locale

object Utils {


     fun openLink(url: String?, uriHandler: UriHandler) {
        url?.let { uriHandler.openUri(it) }
    }
    fun List<LocalizedText>?.getContentBySystemLang(lang: String? = null): String? {
        if (this.isNullOrEmpty()) return null

        lang?.let { inputLang ->
            this.firstOrNull { it.language == inputLang }?.content?.let { return it }
        }

        val systemLang = Locale.getDefault().language
        this.firstOrNull { it.language == systemLang }?.content?.let { return it }

        return this.firstOrNull { it.language == "en" }?.content
    }

}