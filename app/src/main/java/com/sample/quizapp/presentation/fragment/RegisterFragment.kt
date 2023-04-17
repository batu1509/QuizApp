package com.sample.quizapp.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sample.quizapp.R
import com.sample.quizapp.databinding.FragmentRegisterBinding
import com.sample.quizapp.presentation.viewmodel.RegisterViewModel
import com.sample.quizapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.signUpState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {
                    handleLoading(false)
                    findNavController().popBackStack()
                    Toast.makeText(requireContext(), "Sign up success", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    handleLoading(false)
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> handleLoading(true)
                else -> Unit
            }
        }
    }


    private fun initListeners(){
        with(binding){
            signUpButton.setOnClickListener {
                handleSignUp()
            }
            bBack.setOnClickListener {
                findNavController().popBackStack()
            }
            textView.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    private fun handleSignUp(){
        val name = binding.editName.text.toString()
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        val confirmPassword = binding.editPasswordConfirm.text.toString()

        if (name.isBlank()) {
            // Handle name is blank error
            return
        }
        viewModel.signUp(email, password, confirmPassword, name)
    }


    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            signUpButton.isEnabled = !isLoading
            signUpPb.isVisible = isLoading
            signUpButton.text = if (isLoading) "" else getString(R.string.login__signup_button)
        }
    }



}