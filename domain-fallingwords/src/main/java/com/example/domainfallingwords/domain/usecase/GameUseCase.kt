package com.example.domainfallingwords.domain.usecase

import com.example.core.usecase.BaseUseCase
import com.example.domainfallingwords.domain.domainmodel.Question
import com.example.domainfallingwords.domain.domainmodel.Score
import com.example.domainfallingwords.domain.domainmodel.WordModel

interface GameUseCase : BaseUseCase<GameUseCase.Input, GameUseCase.Output> {
    data class Input(
        val currentQuestion: Question? = null,
        val score: Score,
        val wordList: List<WordModel>
    ) : BaseUseCase.Input

    sealed class Output : BaseUseCase.Output {
        data class Success(
            val currentQuestion: Question?,
            val score: Score
        ) : Output()

        data class UnknownError(val message: String) : Output()
    }
}
