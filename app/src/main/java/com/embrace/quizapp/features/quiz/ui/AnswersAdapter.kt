package com.embrace.quizapp.features.quiz.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.embrace.quizapp.databinding.LayoutItemAnswerBinding
import com.embrace.quizapp.features.quiz.data.model.AnswerOptions
import com.embrace.quizapp.features.quiz.ui.viewholder.AnswersViewHolder

class AnswersAdapter(private val listener: ((Int, AnswerOptions) -> Unit)?) :
    RecyclerView.Adapter<AnswersViewHolder>() {

     var isClickable: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswersViewHolder {
        val binding = LayoutItemAnswerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AnswersViewHolder(binding = binding)
    }

    private val differCallback = object : DiffUtil.ItemCallback<AnswerOptions>() {
        override fun areItemsTheSame(
            oldItem: AnswerOptions, newItem: AnswerOptions
        ) = oldItem.key == newItem.key

        override fun areContentsTheSame(
            oldItem: AnswerOptions, newItem: AnswerOptions
        ) = oldItem == newItem
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun getItemCount(): Int = differ.currentList.size
    override fun onBindViewHolder(holder: AnswersViewHolder, position: Int) {
        val items = differ.currentList[position]
        holder.bind(item = items)
        holder.itemView.isEnabled = true
        holder.itemView.setOnClickListener {
            if(!isClickable){
                return@setOnClickListener
            }
            listener?.invoke(position, items)
        }
    }
}