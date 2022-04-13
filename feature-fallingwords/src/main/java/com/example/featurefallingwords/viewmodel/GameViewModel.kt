package com.example.featurefallingwords.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.core.ext.exhaustive
import com.example.core.viewmodel.BaseViewModel
import com.example.domainfallingwords.domain.domainmodel.Question
import com.example.domainfallingwords.domain.domainmodel.Score
import com.example.domainfallingwords.domain.domainmodel.WordModel
import com.example.domainfallingwords.domain.usecase.GameUseCase
import com.example.domainfallingwords.domain.usecase.LoadDataUseCase
import com.example.featurefallingwords.model.WordUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val loadDataUseCase: LoadDataUseCase,
    private val gameUseCase: GameUseCase
) : BaseViewModel<GameContract.Event, GameContract.State, GameContract.Effect>() {
    private val wordModelList: MutableList<WordModel> = mutableListOf()
    private var currentIndex: Int = -1

    init {
        loadData()
    }

    override fun provideInitialState(): GameContract.State {
        return GameContract.State()
    }

    private fun loadData() {
        viewModelScope.launch {
            loadDataUseCase.execute().collect { output ->
                when (output) {
                    is LoadDataUseCase.Output.Success -> {
                        wordModelList.addAll(output.wordList)
                        initGameEngine(output.wordList)
                    }
                    is LoadDataUseCase.Output.UnknownError -> {
                        sendEffect { GameContract.Effect.UnknownErrorEffect(output.message) }
                    }
                }
            }
        }
    }

    private fun initGameEngine(wordList: List<WordModel>) {
        runGame(
            GameUseCase.Input(
                currentQuestion = null,
                score = Score(
                    points = viewState.value.points,
                    lives = viewState.value.lives
                ),
                wordList = wordList
            )
        )
    }

    private fun loopGameEngine(answer: Boolean?) {
        viewState.value.wordUiModel?.let {
            runGame(
                GameUseCase.Input(
                    currentQuestion =
                    Question(
                        word = it.word,
                        providedTranslation = it.fallingTranslation,
                        answer = answer,
                        index = currentIndex
                    ),
                    score = Score(
                        points = viewState.value.points,
                        lives = viewState.value.lives
                    ),
                    wordList = wordModelList
                )
            )
        }
    }

    private fun runGame(input: GameUseCase.Input) {
        viewModelScope.launch {
            gameUseCase.execute(input).collect { output ->
                when (output) {
                    is GameUseCase.Output.Success -> {
                        if (output.score.lives < 0) {
                            sendEffect { GameContract.Effect.OnGameFinished }
                        } else {
                            handleSuccess(output)
                        }
                    }
                    is GameUseCase.Output.UnknownError -> {
                        sendEffect { GameContract.Effect.UnknownErrorEffect(output.message) }
                    }
                }
            }
        }
    }

    private fun handleSuccess(output: GameUseCase.Output.Success) {
        output.currentQuestion?.let {
            currentIndex = it.index
            updateState {
                copy(
                    points = output.score.points,
                    lives = output.score.lives,
                    wordUiModel = WordUiModel(
                        word = it.word,
                        fallingTranslation = it.providedTranslation
                    )
                )
            }
        } ?: gameEnd(output.score)
    }

    private fun gameEnd(score: Score) {
        updateState {
            copy(
                points = score.points,
                lives = score.lives,
                wordUiModel = null
            )
        }
        sendEffect { GameContract.Effect.OnGameFinished }
    }

    override fun handleEvent(event: GameContract.Event) {
        when (event) {
            is GameContract.Event.OnCorrectClicked -> {
                loopGameEngine(true)
            }
            is GameContract.Event.OnWrongClicked -> {
                loopGameEngine(false)
            }
            is GameContract.Event.OnWordFallen -> {
                loopGameEngine(null)
            }
        }.exhaustive
    }
}
