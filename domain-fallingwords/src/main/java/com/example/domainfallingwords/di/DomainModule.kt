package com.example.domainfallingwords.di

import com.example.core.dispatcher.BaseDispatcherProvider
import com.example.core.dispatcher.MainDispatcherProvider
import com.example.domainfallingwords.domain.usecase.GameUseCase
import com.example.domainfallingwords.domain.usecase.GameUseCaseImpl
import com.example.domainfallingwords.domain.usecase.LoadDataUseCase
import com.example.domainfallingwords.domain.usecase.LoadDataUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    companion object {
        @Reusable
        @Provides
        fun provideDispatcher(): BaseDispatcherProvider {
            return MainDispatcherProvider()
        }
    }

    @Binds
    fun bindFetchData(useCase: LoadDataUseCaseImpl): LoadDataUseCase

    @Binds
    fun bindGame(useCase: GameUseCaseImpl): GameUseCase
}
