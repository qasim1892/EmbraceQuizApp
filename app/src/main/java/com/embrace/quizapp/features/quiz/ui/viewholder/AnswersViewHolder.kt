package com.embrace.quizapp.features.quiz.ui.viewholder

import androidx.core.content.ContextCompat
import com.embrace.quizapp.R
import com.embrace.quizapp.databinding.LayoutItemAnswerBinding
import com.embrace.quizapp.extensions.safeUnwrap
import com.embrace.quizapp.features.quiz.data.model.AnswerOptions
import com.embrace.quizapp.features.quiz.data.model.enums.AnswerStatus


class AnswersViewHolder(
    private val binding: LayoutItemAnswerBinding,
) : BaseViewHolder<AnswerOptions>(binding) {

    override fun bind(item: AnswerOptions) {
        with(binding) {
            val answers = "${item.key}. ${item.value}"
            answer.text = answers.safeUnwrap()
            if (item.isCorrect == AnswerStatus.CORRECT) {
                answer.backgroundTintList =
                    (ContextCompat.getColorStateList(context, R.color.green))
            } else if (item.isCorrect == AnswerStatus.WRONG) {

                answer.backgroundTintList = (ContextCompat.getColorStateList(context, R.color.red))
            }

        }
    }

}