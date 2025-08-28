package com.forceupdatekit.service.apiError

import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class ApiHandlerTest {

    @Test
    fun `handleApi returns Success on 2xx`() = runTest {
        val result = handleApi<String> {
            Response.success("ok")
        }
        assertTrue(result is NetworkResult.Success)
        assertEquals("ok", (result as NetworkResult.Success).value)
    }

    @Test
    fun `handleApi returns Error on http exception`() = runTest {
        val media = "application/json".toMediaType()
        val rb = """{"error":"bad"}""".toResponseBody(media)
        val response = Response.error<String>(400, rb)
        val exception = HttpException(response)

        val result = handleApi<String> {
            throw exception
        }

        assertTrue(result is NetworkResult.Error)
        val error = (result as NetworkResult.Error).error
        assertEquals(ApiError.ErrorStatus.DATA_ERROR, error?.errorStatus)
    }

    @Test
    fun `handleApi returns Error on IOException`() = runTest {
        val result = handleApi<String> {
            throw IOException("network down")
        }

        assertTrue(result is NetworkResult.Error)
        val error = (result as NetworkResult.Error).error
        assertEquals(ApiError.ErrorStatus.NO_CONNECTION, error?.errorStatus)
    }

    @Test
    fun `handleApi returns Error on SocketTimeoutException`() = runTest {
        val result = handleApi<String> {
            throw SocketTimeoutException("timeout!")
        }

        assertTrue(result is NetworkResult.Error)
        val error = (result as NetworkResult.Error).error
        assertEquals(ApiError.ErrorStatus.TIMEOUT, error?.errorStatus)
    }

    @Test
    fun `handleApi returns Error on unknown exception`() = runTest {
        val result = handleApi<String> {
            throw RuntimeException("weird")
        }

        assertTrue(result is NetworkResult.Error)
        val error = (result as NetworkResult.Error).error
        assertEquals(ApiError.ErrorStatus.UNKNOWN_ERROR, error?.errorStatus)
    }
}
