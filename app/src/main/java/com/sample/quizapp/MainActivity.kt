package com.sample.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sample.quizapp.databinding.ActivityMainBinding
import com.sample.quizapp.presentation.fragment.QuestionsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2)?.childFragmentManager?.fragments?.get(0)
        if (currentFragment is QuestionsFragment) {
            currentFragment.showEndQuizDialog()
        } else {
            super.onBackPressed()
        }
    }

}