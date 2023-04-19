package com.sample.quizapp.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
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

        val quizRecyclerView = binding.root.findViewById<RecyclerView>(R.id.list_view)
        quizAdapter = QuizHomeAdapter(emptyList()) { quiz ->
            // Implement your onQuizClicked method here
        }
        quizRecyclerView.adapter = quizAdapter
        quizRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        FirebaseFirestore.getInstance().collection("QuizList")
            .get()
            .addOnSuccessListener { documents ->
                val quizzes = documents.mapNotNull { doc ->
                    doc.toObject(QuizModel.UserModel::class.java)
                }
                quizAdapter.setQuizzes(quizzes)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error occurred: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        return binding.root
    }

    private fun logout() = lifecycleScope.launch {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        viewModel.logoutUser()
        findNavController().navigate(R.id.loginFragment)
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