package com.example.domainfallingwords.data.datasource
import android.content.Context
import com.example.domainfallingwords.R
import com.example.domainfallingwords.data.dto.WordDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalDataSource {
    override fun getWords(): Flow<List<WordDto>> {
        val json = context.resources
            .openRawResource(R.raw.words_dataset)
            .bufferedReader()
            .use { it.readText() }
        val data = Json.decodeFromString<List<WordDto>>(json)
        return flow {
            emit(data)
        }
    }
}
