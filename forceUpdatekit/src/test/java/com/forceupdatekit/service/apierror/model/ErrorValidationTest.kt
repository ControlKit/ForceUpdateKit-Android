package com.forceupdatekit.service.apiError.model

import com.sepanta.errorhandler.IErrorEntity
import org.junit.Assert.*
import org.junit.Test

class ErrorValidationTest {

    @Test
    fun `ErrorValidation should implement IErrorEntity`() {
        val errorValidation = ErrorValidation(
            message = "Test error",
            success = false,
            error_code = "TEST_ERROR"
        )
        
        assertTrue(errorValidation is IErrorEntity)
    }

    @Test
    fun `ErrorValidation should have correct properties`() {
        val errorValidation = ErrorValidation(
            message = "Validation failed",
            success = false,
            error_code = "VALIDATION_ERROR",
            errors = ValidationErrors(
                email = listOf("Email is required"),
                subject = listOf("Subject is too short"),
                message = listOf("Message is required")
            )
        )
        
        assertEquals("Validation failed", errorValidation.message)
        assertEquals(false, errorValidation.success)
        assertEquals("VALIDATION_ERROR", errorValidation.error_code)
        assertNotNull(errorValidation.errors)
    }

    @Test
    fun `ErrorValidation should work with null errors`() {
        val errorValidation = ErrorValidation(
            message = "Simple error",
            success = false,
            error_code = "SIMPLE_ERROR",
            errors = null
        )
        
        assertEquals("Simple error", errorValidation.message)
        assertEquals(false, errorValidation.success)
        assertEquals("SIMPLE_ERROR", errorValidation.error_code)
        assertNull(errorValidation.errors)
    }

    @Test
    fun `ErrorValidation should work with empty errors`() {
        val errorValidation = ErrorValidation(
            message = "Error with empty validation",
            success = false,
            error_code = "EMPTY_VALIDATION",
            errors = ValidationErrors()
        )
        
        assertEquals("Error with empty validation", errorValidation.message)
        assertNotNull(errorValidation.errors)
        assertNull(errorValidation.errors?.email)
        assertNull(errorValidation.errors?.subject)
        assertNull(errorValidation.errors?.message)
    }

    @Test
    fun `ValidationErrors should handle all properties`() {
        val validationErrors = ValidationErrors(
            email = listOf("Email is invalid", "Email is required"),
            subject = listOf("Subject is too short"),
            message = listOf("Message is required", "Message is too long")
        )
        
        assertEquals(2, validationErrors.email?.size)
        assertEquals(1, validationErrors.subject?.size)
        assertEquals(2, validationErrors.message?.size)
        
        assertTrue(validationErrors.email?.contains("Email is invalid") == true)
        assertTrue(validationErrors.email?.contains("Email is required") == true)
        assertTrue(validationErrors.subject?.contains("Subject is too short") == true)
        assertTrue(validationErrors.message?.contains("Message is required") == true)
        assertTrue(validationErrors.message?.contains("Message is too long") == true)
    }

    @Test
    fun `ValidationErrors should handle partial properties`() {
        val validationErrors = ValidationErrors(
            email = listOf("Email is invalid"),
            subject = null,
            message = listOf("Message is required")
        )
        
        assertNotNull(validationErrors.email)
        assertNull(validationErrors.subject)
        assertNotNull(validationErrors.message)
        
        assertEquals(1, validationErrors.email?.size)
        assertEquals(1, validationErrors.message?.size)
    }

    @Test
    fun `ErrorValidation should be equal when properties are same`() {
        val error1 = ErrorValidation(
            message = "Test error",
            success = false,
            error_code = "TEST_ERROR"
        )
        
        val error2 = ErrorValidation(
            message = "Test error",
            success = false,
            error_code = "TEST_ERROR"
        )
        
        assertEquals(error1, error2)
        assertEquals(error1.hashCode(), error2.hashCode())
    }

    @Test
    fun `ErrorValidation should not be equal when properties are different`() {
        val error1 = ErrorValidation(
            message = "Test error 1",
            success = false,
            error_code = "TEST_ERROR_1"
        )
        
        val error2 = ErrorValidation(
            message = "Test error 2",
            success = true,
            error_code = "TEST_ERROR_2"
        )
        
        assertNotEquals(error1, error2)
        assertNotEquals(error1.hashCode(), error2.hashCode())
    }

    @Test
    fun `ValidationErrors should be equal when properties are same`() {
        val errors1 = ValidationErrors(
            email = listOf("Email is invalid"),
            subject = listOf("Subject is required"),
            message = listOf("Message is required")
        )
        
        val errors2 = ValidationErrors(
            email = listOf("Email is invalid"),
            subject = listOf("Subject is required"),
            message = listOf("Message is required")
        )
        
        assertEquals(errors1, errors2)
        assertEquals(errors1.hashCode(), errors2.hashCode())
    }

    @Test
    fun `ValidationErrors should not be equal when properties are different`() {
        val errors1 = ValidationErrors(
            email = listOf("Email is invalid"),
            subject = listOf("Subject is required"),
            message = listOf("Message is required")
        )
        
        val errors2 = ValidationErrors(
            email = listOf("Email is required"),
            subject = listOf("Subject is too short"),
            message = listOf("Message is too long")
        )
        
        assertNotEquals(errors1, errors2)
        assertNotEquals(errors1.hashCode(), errors2.hashCode())
    }

    @Test
    fun `ErrorValidation should handle empty message`() {
        val errorValidation = ErrorValidation(
            message = "",
            success = false,
            error_code = "EMPTY_MESSAGE"
        )
        
        assertEquals("", errorValidation.message)
        assertEquals(false, errorValidation.success)
        assertEquals("EMPTY_MESSAGE", errorValidation.error_code)
    }

    @Test
    fun `ErrorValidation should handle success true`() {
        val errorValidation = ErrorValidation(
            message = "Success message",
            success = true,
            error_code = "SUCCESS"
        )
        
        assertEquals("Success message", errorValidation.message)
        assertEquals(true, errorValidation.success)
        assertEquals("SUCCESS", errorValidation.error_code)
    }
}
