package com.example.featurefallingwords.view

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.core.delegate.viewBinding
import com.example.core.ext.exhaustive
import com.example.core.ext.safeNavigate
import com.example.featurefallingwords.R
import com.example.featurefallingwords.databinding.FragmentGameBinding
import com.example.featurefallingwords.viewmodel.GameContract
import com.example.featurefallingwords.viewmodel.GameViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game) {
    private val viewModel: GameViewModel by viewModels()
    private val binding: FragmentGameBinding by viewBinding(FragmentGameBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewState()
        observeEffects()
        initButtons()
    }

    private fun observeViewState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    startWordFalling(viewState)
                    setLife(viewState.lives.toString())
                    setScore(viewState.points.toString())
                    binding.tvWord.text = viewState.wordUiModel?.word
                }
            }
        }
    }

    private fun observeEffects() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is GameContract.Effect.UnknownErrorEffect -> {
                            showSnackBar(getString(com.example.core.R.string.unknown_error))
                        }
                        is GameContract.Effect.OnGameFinished -> {
                            navigateToResult()
                        }
                    }.exhaustive
                }
            }
        }
    }

    private fun navigateToResult() {
        val result = viewModel.viewState.value.points.toString()
        findNavController().safeNavigate(
            GameFragmentDirections.actionGameFragmentToResultFragment(
                result
            )
        )
    }

    private fun startWordFalling(viewState: GameContract.State) {
        binding.tvFalling.text = viewState.wordUiModel?.fallingTranslation
        val falling: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fall_down)
        falling.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
                // do nothing
            }

            override fun onAnimationEnd(p0: Animation?) {
                goneAnimation(GameContract.Event.OnWordFallen)
            }

            override fun onAnimationStart(p0: Animation?) {
                // do nothing
            }
        })
        binding.tvFalling.startAnimation(falling)
    }

    private fun setScore(score: String) {
        val previousValue = binding.tvScore.text.toString()
        if (previousValue != score) {
            zoomInOut(binding.tvScore, score)
        }
    }

    private fun setLife(lives: String) {
        val previousValue = binding.tvLives.text.toString()
        if (previousValue != lives) {
            zoomInOut(binding.tvLives, lives)
        }
    }

    private fun zoomInOut(textView: TextView, text: String) {
        val zoomInOut: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in_out)
        zoomInOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
                // do nothing
            }

            override fun onAnimationEnd(p0: Animation?) {
                // do nothing
            }

            override fun onAnimationStart(p0: Animation?) {
                textView.text = text
            }
        })
        textView.startAnimation(zoomInOut)
    }

    private fun goneAnimation(event: GameContract.Event) {
        val gone: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_gone)
        gone.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
                // do nothing
            }

            override fun onAnimationEnd(p0: Animation?) {
                viewModel.onEvent(event)
            }

            override fun onAnimationStart(p0: Animation?) {
                // do nothing
            }
        })
        binding.tvFalling.startAnimation(gone)
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(binding.container, text, Snackbar.LENGTH_LONG).show()
    }

    private fun initButtons() {
        binding.btnCorrect.setOnClickListener {
            goneAnimation(GameContract.Event.OnCorrectClicked)
        }
        binding.btnWrong.setOnClickListener {
            goneAnimation(GameContract.Event.OnWrongClicked)
        }
    }
}
