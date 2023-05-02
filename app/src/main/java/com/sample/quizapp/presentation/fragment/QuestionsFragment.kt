package com.sample.quizapp.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sample.quizapp.R
import com.sample.quizapp.data.models.QuestionsModel
import com.sample.quizapp.databinding.FragmentQuestionsBinding
import com.sample.quizapp.presentation.viewmodel.QuestionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionsFragment : Fragment() {

    private val viewModel: QuestionsViewModel by viewModels()
    private lateinit var binding: FragmentQuestionsBinding
    private var countDownTimer: CountDownTimer? = null
    private var currentQuestionNumber: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionsBinding.inflate(inflater, container, false)

        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            questions?.let {
                if (it.isNotEmpty()) {
                    binding.quizTitle.text = ""
                    showQuestion(it.first())
                } else {
                    binding.quizTitle.text = "No questions available for this quiz"
                }
            } ?: run {
                //
            }
        }

        binding.quizCloseBtn.setOnClickListener {
            showEndQuizDialog()
        }

        binding.quizNextBtn.setOnClickListener {
            currentQuestionNumber++
            viewModel.questions.value?.let { questions ->
                if (currentQuestionNumber < questions.size) {
                    showQuestion(questions[currentQuestionNumber])
                } else {
                    // Quiz bitirme işlemleri burada yapılacak
                }
            }
        }

        return binding.root
    }

    private fun showQuestion(question: QuestionsModel) {
        stopTimer()
        binding.quizQuestionNumber.text = question.questionNumber.toString()
        binding.quizQuestion.text = question.question
        binding.quizOptionOne.apply {
            text = question.option_a
            setOnClickListener { onOptionButtonClicked(this, question) }
            isSelected = false
            setBackgroundResource(android.R.color.transparent)
        }
        binding.quizOptionTwo.apply {
            text = question.option_b
            setOnClickListener { onOptionButtonClicked(this, question) }
            isSelected = false
            setBackgroundResource(android.R.color.transparent)
        }
        binding.quizOptionThree.apply {
            text = question.option_c
            setOnClickListener { onOptionButtonClicked(this, question) }
            isSelected = false
            setBackgroundResource(android.R.color.transparent)
        }
        binding.questionTime.text = question.timer.toString()
        binding.quizQuestionFeedback.text = "Answer:" // Seçilen butonun cevabını sil
        selectedOptionButton?.isSelected = false // Seçilen butonun seçim durumunu sıfırla
        selectedOptionButton?.setBackgroundResource(android.R.color.transparent) // Seçilen butonun arka plan rengini sıfırla
        selectedOptionButton = null // Seçilen butonu null yap
        startTimer()
    }

    private var selectedOptionButton: Button? = null

    private fun onOptionButtonClicked(button: Button, question: QuestionsModel) {
        // Eğer zaten seçili bir buton varsa, onun seçim durumunu ve arka plan rengini sıfırla
        selectedOptionButton?.isSelected = false
        selectedOptionButton?.setBackgroundResource(android.R.color.transparent)

        // Tıklanan butonun seçim durumunu true yap
        button.isSelected = true
        // Tıklanan butonun arkaplan rengini değiştir
        button.setBackgroundResource(R.color.blue)
        // Seçili butonu güncelle
        selectedOptionButton = button

        binding.quizQuestionFeedback.setOnClickListener {
            val selectedOption = selectedOptionButton?.text.toString()
            val correctAnswer = question.answer
            if (selectedOption == correctAnswer) {
                binding.quizQuestionFeedback.text = "Answer: $correctAnswer - Correct!"
                selectedOptionButton?.setBackgroundResource(R.color.green)
            } else {
                binding.quizQuestionFeedback.text = "Answer: $correctAnswer - Incorrect!"
                selectedOptionButton?.setBackgroundResource(R.color.red)
            }
            selectedOptionButton?.isEnabled = false
        }
    }



    fun showEndQuizDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.end_quiz)
            .setMessage(R.string.are_you_sure)
            .setPositiveButton(R.string.positive_button) { dialog, which ->
                findNavController().navigateUp()
            }
            .setNegativeButton(R.string.negative_button, null)
            .show()
    }


    private fun startTimer() {
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.questionTime.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                findNavController().navigateUp()
            }
        }
        countDownTimer?.start()
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
    }

}
