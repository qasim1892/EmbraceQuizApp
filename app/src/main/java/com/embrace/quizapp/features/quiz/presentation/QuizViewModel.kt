package com.embrace.quizapp.features.quiz.presentation

import com.embrace.quizapp.base.BaseViewModel
import com.embrace.quizapp.commons.FetchState
import com.embrace.quizapp.commons.SingleLiveEvent
import com.embrace.quizapp.database.model.HighestScore
import com.embrace.quizapp.extensions.*
import com.embrace.quizapp.features.quiz.data.QuizRepository

open class QuizViewModel(private val quizRepository: QuizRepository) : BaseViewModel() {

    val fetchQuizState = SingleLiveEvent<FetchState>()

    fun getQuizData() {
        ioJob {
            doNetworkCallAsync {
                FetchState.Loading()
                quizRepository.getQuizResult()
            }.awaitForResult().onSuccessResult { quizResponse ->
                fetchQuizState.postValue(FetchState.Success(quizResponse))
            }.onFailureResult { errorResponse ->
                fetchQuizState.postValue(FetchState.ErrorApi(errorResponse))
            }
        }
    }

    fun updateScore(score: Int) {
        quizRepository.updateScore(
            HighestScore(highestScore = score)
        )
    }

    fun getHighestScore(): Int? {
        return quizRepository.getHighScore()
    }
}