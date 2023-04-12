package com.embrace.quizapp.features.quiz.data

import com.embrace.quizapp.database.dao.HighestScoreDao
import com.embrace.quizapp.database.model.HighestScore
import com.embrace.quizapp.features.quiz.data.model.QuizResponse
import com.embrace.quizapp.network.QuizApiService
import retrofit2.Response

interface QuizRepository {
    suspend fun getQuizResult(): Response<QuizResponse>
    fun updateScore(score: HighestScore)
    fun getHighScore(): Int?
}

class QuizRepositoryImpl(
    private val apiService: QuizApiService, private val scoreDao: HighestScoreDao
) : QuizRepository {

    override suspend fun getQuizResult(): Response<QuizResponse> {
        return apiService.getQuizData()
    }

    override fun updateScore(score: HighestScore) {
        scoreDao.updateScore(score)
    }

    override fun getHighScore(): Int? {
        return scoreDao.getHighScore()
    }

}