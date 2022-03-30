package com.example.domainfallingwords.di

import com.example.domainfallingwords.domain.usecase.GameUseCase
import com.example.domainfallingwords.domain.usecase.GameUseCaseImpl
import com.example.domainfallingwords.domain.usecase.LoadDataUseCase
import com.example.domainfallingwords.domain.usecase.LoadDataUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {
    @Binds
    fun bindFetchData(useCase: LoadDataUseCaseImpl): LoadDataUseCase

    @Binds
    fun bindGame(useCase: GameUseCaseImpl): GameUseCase
}
