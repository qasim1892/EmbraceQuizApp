package com.embrace.quizapp.network.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code") val code: Int, @SerializedName("message") val message: Any?
)