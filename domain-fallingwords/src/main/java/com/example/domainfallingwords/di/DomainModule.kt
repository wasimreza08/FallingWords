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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface DomainModule {

    companion object {
        @Reusable
        @Provides
        fun provideDispatcher(): BaseDispatcherProvider {
            return MainDispatcherProvider()
        }
    }

    @Binds
    @ViewModelScoped
    fun bindFetchData(useCase: LoadDataUseCaseImpl): LoadDataUseCase

    @Binds
    @ViewModelScoped
    fun bindGame(useCase: GameUseCaseImpl): GameUseCase
}
