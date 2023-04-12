package com.embrace.quizapp.features.quiz.data.model

import com.embrace.quizapp.features.quiz.data.model.enums.AnswerStatus

data class AnswerOptions(
    var isCorrect: AnswerStatus = AnswerStatus.NONE,
    val key: String,
    val value: String,
)