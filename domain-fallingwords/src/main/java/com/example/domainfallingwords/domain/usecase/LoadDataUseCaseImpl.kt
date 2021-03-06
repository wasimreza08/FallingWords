package com.example.domainfallingwords.domain.usecase

import com.example.core.dispatcher.BaseDispatcherProvider
import com.example.domainfallingwords.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadDataUseCaseImpl @Inject constructor(
    private val repository: WordRepository,
    private val dispatcherProvider: BaseDispatcherProvider
) : LoadDataUseCase {
    override fun execute(): Flow<LoadDataUseCase.Output> {
        return repository.getWordList().map {
            val shuffleList = it.shuffled().take(WORD_AMOUNT)
            LoadDataUseCase.Output.Success(shuffleList) as LoadDataUseCase.Output
        }.catch { exception ->
            emit(LoadDataUseCase.Output.UnknownError(exception.message.orEmpty()))
        }.flowOn(dispatcherProvider.io())
    }

    companion object {
        private const val WORD_AMOUNT = 15
    }
}
