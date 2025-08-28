package com.forceupdatekit.config.utils

import com.google.gson.Gson
import com.forceupdatekit.service.apiError.ApiErrorEntity
import com.forceupdatekit.service.apiError.ApiError
import org.json.JSONException
import java.io.IOException


fun convertErrorBody(error: String?,code:Int?): ApiError {
    var message = String()
     try {
        val gson = Gson()
        val jsonObject = gson.fromJson(
            error,
            ApiErrorEntity.Main::class.java
        )
        message = jsonObject.message
    } catch (ex: IOException) {
        ex.printStackTrace()
    } catch (ex: JSONException) {
        ex.printStackTrace()
    }
    return ApiError(
        message,
        code,
        ApiError.ErrorStatus.DATA_ERROR,
    )
}