package com.embrace.quizapp.network.interceptors

import android.content.Context
import com.embrace.quizapp.utils.Utils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

const val NO_INTERNET = "no_internet"

class NoConnectionInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!Utils.isConnectionOn(context)) {
            throw NoConnectivityException()
        } else {
            chain.proceed(chain.request())
        }
    }

    class NoConnectivityException : IOException() {
        override val message: String
            get() = NO_INTERNET
    }
}