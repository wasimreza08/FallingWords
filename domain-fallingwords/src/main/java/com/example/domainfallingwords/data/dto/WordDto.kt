package com.example.domainfallingwords.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WordDto(
    @SerialName(value = "text_eng") val engWord: String,
    @SerialName(value = "text_spa") val spaWord: String
)
