package com.joon.fm

import com.google.gson.Gson
import com.joon.fm.core.base.errorEntity.ApiErrorEntity
import com.joon.fm.data.source.remote.ApiError
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