package com.sample.quizapp.presentation.fragment

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.sample.quizapp.R
import com.sample.quizapp.data.models.QuizModel
import com.sample.quizapp.databinding.FragmentHomeBinding
import com.sample.quizapp.databinding.FragmentLoginBinding
import com.sample.quizapp.presentation.adapter.QuizHomeAdapter
import com.sample.quizapp.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var quizAdapter: QuizHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        val quizRecyclerView = binding.listView
        quizAdapter = QuizHomeAdapter(emptyList()) { quiz ->
            // Implement your onQuizClicked method here
        }
        quizRecyclerView.adapter = quizAdapter
        quizRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.quizzes.observe(viewLifecycleOwner) { quizzes ->
            quizAdapter.setQuizzes(quizzes)
            binding.listProgress.visibility = View.GONE
            binding.listView.visibility = View.VISIBLE
        }

        viewModel.getQuizzes()

        binding.listProgress.indeterminateDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.progress_indeterminate)

        val anim = ObjectAnimator.ofFloat(binding.listProgress, "rotation", 0f, 360f)
        anim.duration = 2000
        anim.repeatCount = ValueAnimator.INFINITE
        anim.interpolator = LinearInterpolator()
        anim.start()

        return binding.root
}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.positive_button) { _, _ ->
                    requireActivity().finish()
                    System.exit(0)
                }
                .setNegativeButton(R.string.negative_button, null)
                .show()
        }
    }

    private fun logout() = lifecycleScope.launch {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        viewModel.logoutUser().observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.listView.adapter = null
        viewModel.quizzes.removeObservers(viewLifecycleOwner)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu_item -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.are_you_sure_log_out)
                    .setMessage(R.string.log_out_message)
                    .setPositiveButton(R.string.log_out) { _, _ ->
                        logout()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}

