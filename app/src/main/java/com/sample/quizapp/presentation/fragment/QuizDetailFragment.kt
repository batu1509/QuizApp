package com.sample.quizapp.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.sample.quizapp.R
import com.sample.quizapp.databinding.FragmentQuizDetailBinding
import com.sample.quizapp.presentation.viewmodel.QuizDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizDetailFragment : Fragment() {

    private lateinit var binding: FragmentQuizDetailBinding
    private val viewModel: QuizDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.quiz.observe(viewLifecycleOwner) { quiz ->
            Glide.with(binding.detailsImage.context)
                .load(quiz.image)
                .into(binding.detailsImage)
            binding.detailsTitle.text = quiz.name
            binding.detailsDesc.text = quiz.desc
            binding.detailsDifficultyText.text = quiz.level
            binding.detailsQuestionText.text = quiz.questions.toString()

            binding.detailsStartBtn.setOnClickListener {
                findNavController().navigate(R.id.action_quizDetailFragment_to_questionsFragment, bundleOf("quizId" to quiz.quiz_id))
            }
        }
    }
}


