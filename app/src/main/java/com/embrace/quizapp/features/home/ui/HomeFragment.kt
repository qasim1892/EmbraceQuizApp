package com.embrace.quizapp.features.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.embrace.quizapp.base.BaseFragment
import com.embrace.quizapp.databinding.FragmentHomeBinding
import com.embrace.quizapp.extensions.gone
import com.embrace.quizapp.extensions.visible
import com.embrace.quizapp.features.home.presentation.HomeViewModel
import com.embrace.quizapp.features.quiz.ui.QuizFragment
import com.embrace.quizapp.interfaces.OnClickHandler
import com.embrace.quizapp.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment(), OnClickHandler {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(activity))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }

    private fun init() {
        binding.clickHandler = this
        val higherScore = homeViewModel.getHighestScore()
        if (higherScore != null) {
            if (higherScore > 0) {
                binding.tvHighScore.visible()
                binding.highestScore = higherScore.toString()
            } else {
                binding.tvHighScore.gone()
            }
        }
    }

    override fun onClick(view: View) {
        if (Utils.isConnectionOn(context)) {
            (activity as HomeActivity).replaceAndAddContentFragment(
                QuizFragment()
            )
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}