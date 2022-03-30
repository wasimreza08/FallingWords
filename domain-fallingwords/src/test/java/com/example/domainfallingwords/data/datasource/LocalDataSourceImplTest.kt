package com.example.domainfallingwords.data.datasource

import android.content.Context
import app.cash.turbine.test
import com.example.domainfallingwords.data.dto.WordDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalDataSourceImplTest {
    private val context: Context = mockk()
    private val dataSource = LocalDataSourceImpl(context)

    private val mockData: String = """
    [
  {
    "text_eng": "primary school",
    "text_spa": "escuela primaria"
  },
  {
    "text_eng": "teacher",
    "text_spa": "profesor / profesora"
  } 
  ]
    """

    private val returnData = listOf(
        WordDto(engWord = "primary school", spaWord = "escuela primaria"),
        WordDto(engWord = "teacher", spaWord = "profesor / profesora"),
    )

    @Test
    fun `test valid data emission`() = runTest {
        //  getClass().getResourceAsStream("testFile.txt")
        coEvery {
            context.resources
                .openRawResource(any())
                .bufferedReader()
                .use { it.readText() }
        } returns mockData

        dataSource.getWords().test {
            assertEquals(returnData, awaitComplete())
            awaitComplete()
        }
    }
}
