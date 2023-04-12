package com.embrace.quizapp.features.home.presentation

import com.embrace.quizapp.base.BaseViewModel
import com.embrace.quizapp.features.quiz.data.QuizRepository

class HomeViewModel(private val quizRepository: QuizRepository) : BaseViewModel() {

    fun getHighestScore(): Int? {
        return quizRepository.getHighScore()
    }
}