package com.example.domainfallingwords.di

import com.example.domainfallingwords.data.datasource.LocalDataSource
import com.example.domainfallingwords.data.datasource.LocalDataSourceImpl
import com.example.domainfallingwords.data.mapper.DtoToDomainMapper
import com.example.domainfallingwords.data.repository.WordRepositoryImpl
import com.example.domainfallingwords.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    companion object {
        @Reusable
        @Provides
        fun provideMapper(): DtoToDomainMapper = DtoToDomainMapper()
    }

    @Binds
    fun bindLocalDataSource(dataSource: LocalDataSourceImpl): LocalDataSource

    @Binds
    fun bindRepository(repo: WordRepositoryImpl): WordRepository
}
