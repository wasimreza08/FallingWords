package com.example.domainfallingwords.domain.usecase

import app.cash.turbine.test
import com.example.domainfallingwords.domain.domainmodel.Question
import com.example.domainfallingwords.domain.domainmodel.Score
import com.example.domainfallingwords.domain.domainmodel.WordModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GameUseCaseImplTest {
    private val useCase = GameUseCaseImpl()
    private val input: GameUseCase.Input = GameUseCase.Input(
        currentQuestion = Question(
            word = "primary school",
            providedTranslation = "escuela primaria",
            answer = true,
            index = 0
        ),
        score = Score(
            points = 0,
            lives = 2
        ),
        wordList = listOf(
            WordModel(word = "primary school", translation = "escuela primaria"),
            WordModel(word = "teacher", translation = "profesor / profesora")
        )
    )

    @Test
    fun `test correct answer with true return next question with increased score`() = runTest {
        val expected = Score(
            points = 10,
            lives = 2
        )
        useCase.execute(input).test {
            val item = awaitItem()
            assert(item is GameUseCase.Output.Success)
            assertEquals(expected, (item as GameUseCase.Output.Success).score)
            awaitComplete()
        }
    }

    @Test
    fun `test wrong answer with true return next question with decreased lives`() = runTest {
        val provided = input.copy(
            currentQuestion = Question(
                word = "primary school",
                providedTranslation = "profesor / profesora",
                answer = true,
                index = 0
            )
        )
        val expected = Score(
            points = 0,
            lives = 1
        )
        useCase.execute(provided).test {
            val item = awaitItem()
            assert(item is GameUseCase.Output.Success)
            assertEquals(expected, (item as GameUseCase.Output.Success).score)
            awaitComplete()
        }
    }

    @Test
    fun `test correct answer with false return next question with increased score`() = runTest {
        val provided = input.copy(
            currentQuestion = Question(
                word = "primary school",
                providedTranslation = "escuela primaria",
                answer = false,
                index = 0
            )
        )
        val expected = Score(
            points = 0,
            lives = 1
        )
        useCase.execute(provided).test {
            val item = awaitItem()
            assert(item is GameUseCase.Output.Success)
            assertEquals(expected, (item as GameUseCase.Output.Success).score)
            awaitComplete()
        }
    }

    @Test
    fun `test wrong answer with false return next question with decreased score`() = runTest {
        val provided = input.copy(
            currentQuestion = Question(
                word = "primary school",
                providedTranslation = "profesor / profesora",
                answer = false,
                index = 0
            )
        )
        val expected = Score(
            points = 10,
            lives = 2
        )
        useCase.execute(provided).test {
            val item = awaitItem()
            assert(item is GameUseCase.Output.Success)
            assertEquals(expected, (item as GameUseCase.Output.Success).score)
            awaitComplete()
        }
    }

    @Test
    fun `test game end`() = runTest {
        val provided = input.copy(
            currentQuestion = Question(
                word = "primary school",
                providedTranslation = "profesor / profesora",
                answer = false,
                index = 1
            )
        )

        useCase.execute(provided).test {
            val item = awaitItem()
            assert(item is GameUseCase.Output.Success)
            assertEquals(null, (item as GameUseCase.Output.Success).currentQuestion)
            awaitComplete()
        }
    }
}
