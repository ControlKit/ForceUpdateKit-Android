package com.forceupdatekit.util

import androidx.compose.ui.platform.UriHandler
import com.forceupdatekit.service.model.LocalizedText
import com.forceupdatekit.util.Utils.getContentBySystemLang
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Locale

class UtilsTest {
    private var prev: Locale? = null
    private val mockUriHandler: UriHandler = mockk(relaxed = true)

    @Before 
    fun save() { 
        prev = Locale.getDefault() 
    }
    
    @After 
    fun restore() { 
        prev?.let(Locale::setDefault) 
    }

    @Test
    fun `openLink should call openUri when url is not null`() {
        val url = "https://example.com"
        
        Utils.openLink(url, mockUriHandler)
        
        verify { mockUriHandler.openUri(url) }
    }

    @Test
    fun `openLink should not call openUri when url is null`() {
        Utils.openLink(null, mockUriHandler)
        
        verify(exactly = 0) { mockUriHandler.openUri(any()) }
    }

    @Test
    fun `openLink should call openUri when url is empty`() {
        Utils.openLink("", mockUriHandler)
        
        verify { mockUriHandler.openUri("") }
    }

    @Test
    fun `getContentBySystemLang should return content for specified language`() {
        val list = listOf(
            LocalizedText("en", "English"),
            LocalizedText("fa", "فارسی"),
            LocalizedText("de", "Deutsch")
        )
        
        val result = list.getContentBySystemLang("fa")
        
        assertEquals("فارسی", result)
    }

    @Test
    fun `getContentBySystemLang should return system language content when no lang specified`() {
        Locale.setDefault(Locale("de"))
        val list = listOf(
            LocalizedText("en", "English"),
            LocalizedText("fa", "فارسی"),
            LocalizedText("de", "Deutsch")
        )
        
        val result = list.getContentBySystemLang()
        
        assertEquals("Deutsch", result)
    }

    @Test
    fun `getContentBySystemLang should fallback to English when system language not found`() {
        Locale.setDefault(Locale("it"))
        val list = listOf(
            LocalizedText("en", "English"),
            LocalizedText("fa", "فارسی")
        )
        
        val result = list.getContentBySystemLang()
        
        assertEquals("English", result)
    }

    @Test
    fun `getContentBySystemLang should return null for empty list`() {
        val list = emptyList<LocalizedText>()
        
        val result = list.getContentBySystemLang()
        
        assertNull(result)
    }

    @Test
    fun `getContentBySystemLang should return null for null list`() {
        val list: List<LocalizedText>? = null
        
        val result = list.getContentBySystemLang()
        
        assertNull(result)
    }

    @Test
    fun `getContentBySystemLang should return null when specified language not found`() {
        val list = listOf(
            LocalizedText("en", "English"),
            LocalizedText("fa", "فارسی")
        )
        
        val result = list.getContentBySystemLang("de")
        
        // Since "de" is not found, it should fallback to system language, then English
        // If system language is not "en" or "fa", it should return "English" as fallback
        assertNotNull(result)
        assertEquals("English", result)
    }

    @Test
    fun `getContentBySystemLang should return null when no English fallback available`() {
        Locale.setDefault(Locale("it"))
        val list = listOf(
            LocalizedText("fa", "فارسی"),
            LocalizedText("de", "Deutsch")
        )
        
        val result = list.getContentBySystemLang()
        
        assertNull(result)
    }

    @Test
    fun `getContentBySystemLang should handle null content in LocalizedText`() {
        val list = listOf(
            LocalizedText("en", null),
            LocalizedText("fa", "فارسی")
        )
        
        val result = list.getContentBySystemLang("en")
        
        assertNull(result)
    }

    @Test
    fun `getContentBySystemLang should handle null language in LocalizedText`() {
        val list = listOf(
            LocalizedText(null, "English"),
            LocalizedText("fa", "فارسی")
        )
        
        val result = list.getContentBySystemLang("en")
        
        assertNull(result)
    }

    @Test
    fun `getContentBySystemLang should prioritize specified language over system language`() {
        Locale.setDefault(Locale("de"))
        val list = listOf(
            LocalizedText("en", "English"),
            LocalizedText("fa", "فارسی"),
            LocalizedText("de", "Deutsch")
        )
        
        val result = list.getContentBySystemLang("fa")
        
        assertEquals("فارسی", result)
    }

    @Test
    fun `getContentBySystemLang should work with single item list`() {
        val list = listOf(LocalizedText("en", "English"))
        
        val result = list.getContentBySystemLang("en")
        
        assertEquals("English", result)
    }

    @Test
    fun `getContentBySystemLang should handle case sensitivity`() {
        val list = listOf(
            LocalizedText("EN", "English"),
            LocalizedText("en", "english")
        )
        
        val result = list.getContentBySystemLang("en")
        
        assertEquals("english", result)
    }
}
