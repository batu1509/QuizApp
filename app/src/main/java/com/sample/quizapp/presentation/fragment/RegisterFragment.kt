package com.sample.quizapp.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
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

    private fun initObservers(){
        viewModel.signUpState.observe(viewLifecycleOwner){state ->
            when(state){
                is Resource.Success -> {
                    handleLoading(isLoading = false)
                    activity?.onBackPressed()
                    Toast.makeText(
                        requireContext(),
                        "Sign up success",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Error -> {
                    handleLoading(isLoading = false)
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> handleLoading(isLoading = true)
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
                activity?.onBackPressed()
            }
        }
    }


    private fun handleSignUp(){
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        viewModel.signUp(email, password)
    }

    private fun handleLoading(isLoading:Boolean){
        with(binding){
            if (isLoading) {
                signUpButton.text = ""
                signUpButton.isEnabled = false
                signUpPb.visibility = View.VISIBLE
            }else{
                signUpPb.visibility = View.GONE
                signUpButton.text = getString(R.string.login__signup_button)
                signUpButton.isEnabled = true
            }
        }
    }



}