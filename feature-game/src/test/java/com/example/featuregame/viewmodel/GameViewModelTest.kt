package com.example.featuregame.viewmodel

import com.example.domainfallingwords.domain.domainmodel.Question
import com.example.domainfallingwords.domain.domainmodel.Score
import com.example.domainfallingwords.domain.domainmodel.WordModel
import com.example.domainfallingwords.domain.usecase.GameUseCase
import com.example.domainfallingwords.domain.usecase.LoadDataUseCase
import com.example.featuregame.model.WordUiModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GameViewModelTest {
    private val loadDataUseCase: LoadDataUseCase = mockk()
    private val gamUseCase: GameUseCase = mockk()
    private lateinit var viewModel: GameViewModel
    private lateinit var testDispatcher: TestDispatcher

    private fun createViewModel() {
        viewModel = GameViewModel(loadDataUseCase, gamUseCase)
    }

    @Before
    fun setUp() {
        testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    private val wordList = listOf(
        WordModel(word = "primary school", translation = "escuela primaria"),
        WordModel(word = "teacher", translation = "profesor / profesora")
    )

    private val input: GameUseCase.Input = GameUseCase.Input(
        currentQuestion = Question(
            word = "primary school",
            providedTranslation = "escuela primaria",
            answer = true,
            index = 0
        ),
        score = Score(
            points = 0,
            lives = 3
        ),
        wordList = listOf(
            WordModel(word = "primary school", translation = "escuela primaria"),
            WordModel(word = "teacher", translation = "profesor / profesora")
        )
    )

    private val output: GameUseCase.Output = GameUseCase.Output.Success(
        currentQuestion = Question(
            word = "teacher",
            providedTranslation = "escuela primaria",
            answer = true,
            index = 1
        ),
        score = Score(
            points = 10,
            lives = 3
        )
    )

    private val state = GameContract.State(
        points = 10,
        lives = 3,
        wordUiModel = WordUiModel(
            "teacher",
            "escuela primaria"
        )

    )

    @Test
    fun `test load and init game with success state`() = runTest {
        coEvery {
            loadDataUseCase.execute()
        } returns flow { emit(LoadDataUseCase.Output.Success(wordList)) }

        coEvery {
            gamUseCase.execute(any())
        } returns flow { emit(output) }

        createViewModel()

        viewModel.viewState.take(1).collectLatest {
            assertEquals(state, it)
        }
    }

    @Test
    fun `test load data error and init game return error state`() = runTest {
        val message = "test"
        coEvery {
            loadDataUseCase.execute()
        } returns flow { emit(LoadDataUseCase.Output.UnknownError(message)) }

        coEvery {
            gamUseCase.execute(any())
        } returns flow { emit(output) }

        createViewModel()

        viewModel.effect.take(1).collectLatest {
            assertEquals(GameContract.Effect.UnknownErrorEffect(message), it)
        }
    }

    @Test
    fun `test load data success and init game error return error state`() = runTest {
        val message = "test"
        coEvery {
            loadDataUseCase.execute()
        } returns flow { emit(LoadDataUseCase.Output.Success(wordList)) }

        coEvery {
            gamUseCase.execute(any())
        } returns flow { emit(GameUseCase.Output.UnknownError(message)) }

        createViewModel()

        viewModel.effect.take(1).collectLatest {
            assertEquals(GameContract.Effect.UnknownErrorEffect(message), it)
        }
    }

    @Test
    fun `test event true clicked with success state`() = runTest {
        coEvery {
            loadDataUseCase.execute()
        } returns flow { emit(LoadDataUseCase.Output.Success(wordList)) }

        coEvery {
            gamUseCase.execute(any())
        } returnsMany listOf(flow { emit(output) }, flow { emit(output) })

        createViewModel()
        viewModel.onEvent(GameContract.Event.OnCorrectClicked)

        viewModel.viewState.take(1).collectLatest {
            assertEquals(state, it)
        }
    }

    @Test
    fun `test event false clicked with success state`() = runTest {
        coEvery {
            loadDataUseCase.execute()
        } returns flow { emit(LoadDataUseCase.Output.Success(wordList)) }

        coEvery {
            gamUseCase.execute(any())
        } returnsMany listOf(flow { emit(output) }, flow { emit(output) })

        createViewModel()
        viewModel.onEvent(GameContract.Event.OnWrongClicked)

        viewModel.viewState.take(1).collectLatest {
            assertEquals(state, it)
        }
    }

    @Test
    fun `test event word fallen with success state`() = runTest {
        coEvery {
            loadDataUseCase.execute()
        } returns flow { emit(LoadDataUseCase.Output.Success(wordList)) }

        coEvery {
            gamUseCase.execute(any())
        } returnsMany listOf(flow { emit(output) }, flow { emit(output) })

        createViewModel()
        viewModel.onEvent(GameContract.Event.OnWordFallen)

        viewModel.viewState.take(1).collectLatest {
            assertEquals(state, it)
        }
    }

    @Test
    fun `test error in loop game`() = runTest {
        val message = "test"
        coEvery {
            loadDataUseCase.execute()
        } returns flow { emit(LoadDataUseCase.Output.Success(wordList)) }

        coEvery {
            gamUseCase.execute(any())
        } returnsMany listOf(
            flow { emit(output) },
            flow { emit(GameUseCase.Output.UnknownError(message)) }
        )

        createViewModel()
        viewModel.onEvent(GameContract.Event.OnWordFallen)

        viewModel.effect.take(1).collectLatest {
            assertEquals(GameContract.Effect.UnknownErrorEffect(message), it)
        }
    }

    @Test
    fun `test game end with success state`() = runTest {
        val output2 = GameUseCase.Output.Success(
            currentQuestion = null,
            score = Score(
                points = 10,
                lives = 3
            )
        )
        val stateEnd = state.copy(wordUiModel = null)
        coEvery {
            loadDataUseCase.execute()
        } returns flow { emit(LoadDataUseCase.Output.Success(wordList)) }

        coEvery {
            gamUseCase.execute(any())
        } returnsMany listOf(flow { emit(output) }, flow { emit(output2) })

        createViewModel()
        viewModel.onEvent(GameContract.Event.OnWordFallen)

        viewModel.viewState.take(1).collectLatest {
            assertEquals(stateEnd, it)
        }
    }

    @org.junit.After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
