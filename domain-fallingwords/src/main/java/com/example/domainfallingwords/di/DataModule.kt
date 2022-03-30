package com.example.domainfallingwords.di

import com.example.domainfallingwords.data.datasource.LocalDataSource
import com.example.domainfallingwords.data.datasource.LocalDataSourceImpl
import com.example.domainfallingwords.data.repository.WordRepositoryImpl
import com.example.domainfallingwords.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindLocalDataSource(dataSource: LocalDataSourceImpl): LocalDataSource

    @Binds
    fun bindRepository(repo: WordRepositoryImpl): WordRepository
}
