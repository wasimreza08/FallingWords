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
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface DomainModule {

    companion object {
        @ViewModelScoped
        @Provides
        fun provideDispatcher(): BaseDispatcherProvider {
            return MainDispatcherProvider()
        }
    }

    @ViewModelScoped
    @Binds
    fun bindFetchData(useCase: LoadDataUseCaseImpl): LoadDataUseCase

    @ViewModelScoped
    @Binds
    fun bindGame(useCase: GameUseCaseImpl): GameUseCase
}
