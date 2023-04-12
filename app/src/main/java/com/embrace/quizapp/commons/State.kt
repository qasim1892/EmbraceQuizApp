package com.embrace.quizapp.commons

import com.embrace.quizapp.network.response.ErrorResponse

sealed class FetchState {
    data class Success<T>(val data: T?) : FetchState()
    data class ErrorApi(val error: ErrorResponse) : FetchState()
    data class Loading(val show: Boolean = true) : FetchState()
}