package com.example.domainfallingwords.data.mapper

import com.example.core.mapper.Mapper
import com.example.domainfallingwords.data.dto.WordDto
import com.example.domainfallingwords.domain.domainmodel.WordModel

class DtoToDomainMapper : Mapper<WordDto, WordModel> {
    override fun map(from: WordDto): WordModel {
        return WordModel(
            word = from.engWord,
            translation = from.spaWord
        )
    }
}
