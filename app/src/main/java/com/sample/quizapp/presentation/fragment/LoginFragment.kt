package com.sample.quizapp.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sample.quizapp.R
import com.sample.quizapp.databinding.FragmentLoginBinding
import com.sample.quizapp.presentation.viewmodel.LoginViewModel
import com.sample.quizapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        val navController = findNavController()

        lifecycleScope.launch {
            viewModel.checkLoggedIn().collect { isLoggedIn ->
                if (isLoggedIn) {
                    navController.navigate(R.id.homeFragment)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initObservers(){
        viewModel.loginState.observe(viewLifecycleOwner){state->
            when(state){
                is Resource.Success -> {
                    handleLoading(isLoading = false)
                    val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment(state.data.uid)
                    findNavController().navigate(action)
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
            loginButton.setOnClickListener { handleLogin() }
            signUpButton.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registerFragment) }
            bPasswordRecovery.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_passwordRecoveryFragment) }
        }
    }

    private fun handleLogin(){
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        viewModel.login(email, password)
    }

    private fun handleLoading(isLoading:Boolean){
        with(binding){
            if (isLoading) {
                loginButton.text = ""
                loginButton.isEnabled = false
                loginPb.visibility = View.VISIBLE
            }else{
                loginPb.visibility = View.GONE
                loginButton.text = getString(R.string.login__signup_button)
                loginButton.isEnabled = true
            }
        }
    }
}