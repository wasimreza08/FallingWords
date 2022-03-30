package com.example.domainfallingwords.domain.usecase

import com.example.core.usecase.BaseInputLessUseCase
import com.example.domainfallingwords.domain.domainmodel.WordModel

interface LoadDataUseCase : BaseInputLessUseCase<LoadDataUseCase.Output> {
    sealed class Output : BaseInputLessUseCase.Output {
        data class Success(val wordList: List<WordModel>) : Output()
        data class UnknownError(val message: String) : Output()
    }
}
