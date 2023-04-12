package com.embrace.quizapp.features.quiz.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizResult(
    val totalQuestion: Int,
    val rightAnswer: Int,
    val wrongAnswer: Int
) : Parcelable
