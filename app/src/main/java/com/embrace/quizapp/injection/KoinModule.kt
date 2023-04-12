package com.embrace.quizapp.injection

import com.embrace.quizapp.BuildConfig
import com.embrace.quizapp.commons.CoroutineContextProvider
import com.embrace.quizapp.database.dao.HighestScoreDao
import com.embrace.quizapp.features.home.presentation.HomeViewModel
import com.embrace.quizapp.features.quiz.data.QuizRepository
import com.embrace.quizapp.features.quiz.data.QuizRepositoryImpl
import com.embrace.quizapp.features.quiz.presentation.QuizViewModel
import com.embrace.quizapp.network.QuizApiService
import com.embrace.quizapp.network.interceptors.NoConnectionInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BUILD_TYPE_RELEASE = "release"

val appModule = module {
    single { CoroutineContextProvider() }
    single { HighestScoreDao() }
}

val apiModule = module {
    single {
        val builder = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(NoConnectionInterceptor(androidContext()))
        if (!BUILD_TYPE_RELEASE.equals(BuildConfig.BUILD_TYPE, true)) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        builder.build()
    }

    factory { params ->
        Retrofit.Builder().client(get()).baseUrl(params.get<String>(0))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()
    }

    single {
        get<Retrofit> { parametersOf(BuildConfig.SERVER_URL) }.create(
            QuizApiService::class.java
        )
    }
}

val viewModelModule = module {
    viewModel { QuizViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}

val repositoryModule = module {
    single<QuizRepository> { QuizRepositoryImpl(get(), get()) }
}

