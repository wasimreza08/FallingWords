package com.example.domainfallingwords.domain.repository

import com.example.domainfallingwords.domain.domainmodel.WordModel
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun getWordList(): Flow<List<WordModel>>
}
