package com.example.domainfallingwords.data.repository

import com.example.domainfallingwords.data.datasource.LocalDataSource
import com.example.domainfallingwords.data.mapper.DtoToDomainMapper
import com.example.domainfallingwords.domain.domainmodel.WordModel
import com.example.domainfallingwords.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : WordRepository {
    override fun getWordList(): Flow<List<WordModel>> {
        val mapper = DtoToDomainMapper()
        return localDataSource.getWords().map { list ->
            list.map { mapper.map(it) }
        }
    }
}
