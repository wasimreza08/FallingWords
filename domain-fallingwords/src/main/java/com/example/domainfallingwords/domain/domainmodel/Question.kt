package com.example.domainfallingwords.domain.domainmodel

data class Question(
    val word: String,
    val providedTranslation: String,
    val answer: Boolean? = null,
    val index: Int
)
