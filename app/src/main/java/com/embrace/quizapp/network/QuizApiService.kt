package com.embrace.quizapp.network

import com.embrace.quizapp.features.quiz.data.model.QuizResponse
import retrofit2.Response
import retrofit2.http.GET

interface QuizApiService {
    @GET("v1/3acef828-7f8f-4905-a12e-1b057db45f48")
    suspend fun getQuizData(): Response<QuizResponse>
}