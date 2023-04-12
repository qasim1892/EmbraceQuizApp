package com.embrace.quizapp.features.quiz.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.RawValue


data class QuizResponse(
    @SerializedName("questions") val questions: List<Question> = listOf()
) {
    data class Question(
        @SerializedName("answers") val answers: @RawValue Map<String, String>,
        @SerializedName("correctAnswer") val correctAnswer: String = "", // D
        @SerializedName("question") val question: String = "", // When was Embrace-IT founded?
        @SerializedName("questionImageUrl") val questionImageUrl: String? = null, // https://bit.ly/3Da6HGU
        @SerializedName("score") val score: Int = 0, // 200
        @SerializedName("type") val type: String? = null // single-choice
    ) {
        data class Answers(
            @SerializedName("A") val a: String = "", // 1997
            @SerializedName("B") val b: String = "", // 1999
            @SerializedName("C") val c: String? = null, // 2001
            @SerializedName("D") val d: String? = null, // 2004
            @SerializedName("E") val e: String? = null // All of the above
        )
    }
}