package com.joon.fm.core.base.errorEntity

class ApiErrorEntity {
        data class Main(
            val success: Boolean,
            val message: String,
        )
}