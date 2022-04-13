package com.example.domainfallingwords.domain.usecase

import app.cash.turbine.test
import com.example.domainfallingwords.domain.domainmodel.WordModel
import com.example.domainfallingwords.domain.repository.WordRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class LoadDataUseCaseImplTest {
    private val repository: WordRepository = mockk()
    private val useCase = LoadDataUseCaseImpl(repository)

    private val expectedData = listOf(
        WordModel(word = "primary school", translation = "escuela primaria"),
        WordModel(word = "teacher", translation = "profesor / profesora"),
        WordModel(word = "primary school", translation = "escuela primaria"),
        WordModel(word = "teacher", translation = "profesor / profesora"),
        WordModel(word = "primary school", translation = "escuela primaria"),
        WordModel(word = "teacher", translation = "profesor / profesora"),
        WordModel(word = "primary school", translation = "escuela primaria"),
        WordModel(word = "teacher", translation = "profesor / profesora"),
        WordModel(word = "primary school", translation = "escuela primaria"),
        WordModel(word = "teacher", translation = "profesor / profesora"),
        WordModel(word = "primary school", translation = "escuela primaria"),
        WordModel(word = "teacher", translation = "profesor / profesora"),
        WordModel(word = "primary school", translation = "escuela primaria"),
        WordModel(word = "teacher", translation = "profesor / profesora"),
        WordModel(word = "primary school", translation = "escuela primaria"),
        WordModel(word = "teacher", translation = "profesor / profesora"),
    )

    @Test
    fun `test loadData provide valid list`() = runTest {
        coEvery {
            repository.getWordList()
        } returns flow { emit(expectedData) }

        useCase.execute().test {
            val item = awaitItem()
            assert(item is LoadDataUseCase.Output.Success)
            assert((item as LoadDataUseCase.Output.Success).wordList.size == expectedData.size - 1)
            awaitComplete()
        }
    }

    @Test
    fun `test loadData provide error list`() = runTest {
        val provided = IOException("test")
        val expected = LoadDataUseCase.Output.UnknownError("test")
        coEvery {
            repository.getWordList()
        } returns flow { emit(throw provided) }

        useCase.execute().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
