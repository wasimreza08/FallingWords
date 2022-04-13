package com.example.featurefallingwords.view

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core.delegate.viewBinding
import com.example.core.ext.safeNavigate
import com.example.featurefallingwords.R
import com.example.featurefallingwords.databinding.FragmentResultBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment(R.layout.fragment_result) {
    private val binding: FragmentResultBinding by viewBinding(FragmentResultBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val safeArgs: ResultFragmentArgs by navArgs()
        binding.tvResult.text = safeArgs.result
        binding.btnReturn.setOnClickListener {
            navigateToGame()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateToGame()
        }
    }

    private fun navigateToGame() {
        findNavController().safeNavigate(ResultFragmentDirections.actionResultFragmentToGameFragment())
    }
}
