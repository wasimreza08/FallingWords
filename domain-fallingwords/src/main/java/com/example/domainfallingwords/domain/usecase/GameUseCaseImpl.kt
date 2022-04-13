package com.example.domainfallingwords.domain.usecase

import com.example.domainfallingwords.domain.domainmodel.Question
import com.example.domainfallingwords.domain.domainmodel.Score
import com.example.domainfallingwords.domain.domainmodel.WordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GameUseCaseImpl @Inject constructor() : GameUseCase {
    override fun execute(input: GameUseCase.Input): Flow<GameUseCase.Output> = flow {
        emit(
            GameUseCase.Output.Success(
                currentQuestion = selectQuestion(input.wordList, input.currentQuestion),
                score = calculateScore(
                    input.wordList,
                    input.score,
                    input.currentQuestion
                )
            ) as GameUseCase.Output
        )
    }.catch { exception ->
        emit(GameUseCase.Output.UnknownError(exception.message.orEmpty()))
    }

    private fun calculateScore(
        wordList: List<WordModel>,
        score: Score,
        question: Question?
    ): Score {
        return if (question == null) {
            score
        } else {
            if (
                question.answer == null ||
                ((wordList[question.index].translation == question.providedTranslation) != question.answer)
            ) {
                score.copy(lives = score.lives - DECREASED_LIFE)
            } else {
                score.copy(points = score.points + INCREASED_POINT)
            }
        }
    }

    private fun selectQuestion(wordList: List<WordModel>, question: Question?): Question? {
        val index = question?.let {
            it.index + 1
        } ?: 0
        if (index > wordList.lastIndex) return null

        val randomNumber = (0..wordList.lastIndex).random()
        return if (randomNumber % 2 == 0) {
            Question(
                word = wordList[index].word,
                providedTranslation = wordList[index].translation,
                answer = null,
                index = index
            )
        } else {
            Question(
                word = wordList[index].word,
                providedTranslation = wordList[randomNumber].translation,
                answer = null,
                index = index
            )
        }
    }

    companion object {
        private const val INCREASED_POINT = 10
        private const val DECREASED_LIFE = 1
    }
}
