package com.example.domainfallingwords.data.datasource

import com.example.domainfallingwords.data.dto.WordDto
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getWords(): Flow<List<WordDto>>
}
