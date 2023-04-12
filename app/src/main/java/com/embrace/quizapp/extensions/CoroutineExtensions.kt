package com.embrace.quizapp.extensions

import androidx.lifecycle.viewModelScope
import com.embrace.quizapp.BuildConfig
import com.embrace.quizapp.base.BaseViewModel
import com.embrace.quizapp.network.response.ApiResult
import com.embrace.quizapp.network.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Response
import java.net.UnknownHostException

suspend fun <R> BaseViewModel.doNetworkCallAsync(block: suspend () -> Response<R>): Deferred<Response<R>> {
    return viewModelScope.async(coroutineContextProvider.IO) { block.invoke() }
}

suspend fun <R> Deferred<Response<R>>.awaitForResult(): ApiResult<R> {
    val result = ApiResult<R>()
    try {
        val response = await()

        val request = response.raw().request


        if (response.code() == 401 && request.url.toString() != BuildConfig.SERVER_URL) {
            cancel("unauthorized")
        }

        result.response = response
        result.data = response.body()
    } catch (ex: Exception) {
        result.exception = ex
    } finally {
        return result
    }
}
fun <T> Response<T>.errorResponse(): ErrorResponse? {
    val stringError = errorBody()?.string()
    if (stringError.isNullOrEmpty()) return null
    return try {
        val errorResult = Gson().fromJson(stringError, ErrorResponse::class.java)
        errorResult.let {
            ErrorResponse(
                it.code,
                it.message,
            )
        }
    } catch (exception: Exception) {
        ErrorResponse(
            code(), exception.message
        )
    }
}

fun <R> ApiResult<R>.onSuccessResult(block: (R) -> Unit): ApiResult<R> {
    if (this.isSuccess) {
        response?.body()?.notNull { block(it) }
    }
    return this
}

fun <R> ApiResult<R>.onFailureResult(block: (ErrorResponse) -> Unit) {
    if (!this.isSuccess) {
        if (exception is CancellationException) return
        exception.notNull {
            if (exception is UnknownHostException) {
                val errorMessage = "An error occurred"
                val errorCode = response?.code() ?: 0
                block(ErrorResponse(errorCode, errorMessage))
            } else {
                val errorMessage = it.localizedMessage ?: "Error found"
                val errorCode = response?.code() ?: 0
                block(ErrorResponse(errorCode, errorMessage))
            }
        }
    } else {
        response?.errorResponse().notNull {
            block(it)
        }
    }
}

fun BaseViewModel.ioJob(block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(coroutineContextProvider.IO) {
        block()
    }
}

