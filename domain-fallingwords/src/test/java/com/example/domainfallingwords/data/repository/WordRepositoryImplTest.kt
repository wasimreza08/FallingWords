package com.example.domainfallingwords.data.repository

import app.cash.turbine.test
import com.example.domainfallingwords.data.datasource.LocalDataSource
import com.example.domainfallingwords.data.dto.WordDto
import com.example.domainfallingwords.domain.domainmodel.WordModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class WordRepositoryImplTest {
    private val localDataSource: LocalDataSource = mockk()
    private val repository = WordRepositoryImpl(localDataSource)

    private val returnData = listOf(
        WordDto(engWord = "primary school", spaWord = "escuela primaria"),
        WordDto(engWord = "teacher", spaWord = "profesor / profesora"),
    )
    private val expectedData = listOf(
        WordModel(word = "primary school", translation = "escuela primaria"),
        WordModel(word = "teacher", translation = "profesor / profesora"),
    )

    @Test
    fun `test getWordList return valid list`() = runTest {
        coEvery {
            localDataSource.getWords()
        } returns flow { emit(returnData) }

        repository.getWordList().test {
            assertEquals(expectedData, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `test getWordList return empty list`() = runTest {
        coEvery {
            localDataSource.getWords()
        } returns flow { emit(emptyList()) }

        repository.getWordList().test {
            assertEquals(emptyList<WordModel>(), awaitItem())
            awaitComplete()
        }
    }
}
