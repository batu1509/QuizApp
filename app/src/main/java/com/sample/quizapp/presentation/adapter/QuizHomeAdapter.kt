package com.sample.quizapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.quizapp.R
import com.sample.quizapp.data.models.QuizModel
import com.sample.quizapp.presentation.fragment.HomeFragmentDirections

class QuizHomeAdapter(
    private var quizzes: List<QuizModel.UserModel>,
    private val onQuizClicked: (QuizModel.UserModel) -> Unit
) : RecyclerView.Adapter<QuizHomeAdapter.QuizViewHolder>() {
    fun setQuizzes(newQuizzes: List<QuizModel.UserModel>) {
        quizzes = newQuizzes
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_detail_item, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizzes[position], onQuizClicked)
    }

    override fun getItemCount(): Int = quizzes.size

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quizImage = itemView.findViewById<ImageView>(R.id.quiz_image)
        private val quizTitle = itemView.findViewById<TextView>(R.id.quiz_title)
        private val quizDesc = itemView.findViewById<TextView>(R.id.quiz_desc)
        private val quizDifficulty = itemView.findViewById<TextView>(R.id.quiz_difficulty)
        private val quizBtn = itemView.findViewById<Button>(R.id.quiz_btn)

        fun bind(quiz: QuizModel.UserModel, onQuizClicked: (QuizModel.UserModel) -> Unit) {
            itemView.apply {
                Glide.with(context)
                    .load(quiz.image)
                    .into(quizImage)

                quizTitle.text = quiz.name
                quizDifficulty.text = quiz.level
                quizDesc.text = if (quiz.desc.length > 150) {
                    "${quiz.desc.substring(0, 150)}..."
                } else {
                    quiz.desc
                }
                quizBtn.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_quizDetailFragment, bundleOf("quizId" to quiz.quiz_id))
                }
            }
        }
    }
}


