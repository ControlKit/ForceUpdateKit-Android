package com.forceupdatekit.service
import com.forceupdatekit.service.model.ApiCheckUpdateResponse
import com.forceupdatekit.service.model.ApiData
import com.forceupdatekit.service.model.LocalizedText
import com.forceupdatekit.service.model.getContentBySystemLang
import com.forceupdatekit.service.model.toDomain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale

class LocalizationTest {
    private var prev: Locale? = null

    @Before fun save() { prev = Locale.getDefault() }
    @After fun restore() { prev?.let(Locale::setDefault) }

    @Test fun `pick system language`() {
        Locale.setDefault(Locale("de"))
        val list = listOf(
            LocalizedText("fa", "عنوان"),
            LocalizedText("en", "Title"),
            LocalizedText("de", "Titel")
        )
        assertEquals("Titel", list.getContentBySystemLang())
    }

    @Test fun `fallback to en`() {
        Locale.setDefault(Locale("it"))
        val list = listOf(
            LocalizedText("fa", "عنوان"),
            LocalizedText("en", "Title")
        )
        assertEquals("Title", list.getContentBySystemLang())
    }

    @Test fun `null list null`() {
        val list: List<LocalizedText>? = null
        assertEquals(null, list.getContentBySystemLang())
    }


    @Test fun `toDomain maps fields safely`() {
        Locale.setDefault(Locale.ENGLISH)
        val api = ApiCheckUpdateResponse(
            ApiData(
                id = "1",
                title = null, // عمداً null تا NPE نده
                description = listOf(LocalizedText("en", "Desc")),
                force = false,
                icon = "https://x/y.png",
                link = "https://x",
                button_title = listOf(LocalizedText("en", "OK")),
                cancel_button_title = null,
                version = listOf(LocalizedText("en", "2.0.0")),
                sdk_version = 33,
                minimum_version = null,
                maximum_version = "5.0.0",
                created_at = "2025-01-01"
            )
        )
        val d = api.toDomain()
        assertEquals("1", d.id)
        assertEquals("2.0.0", d.version)
        assertEquals(null, d.title) // چون title=null
        assertEquals("Desc", d.description)
        assertEquals("33", d.sdkVersion) // stringified
        assertEquals("5.0.0", d.maximumVersion)
        assertEquals("2025-01-01", d.createdAt)
    }

}
