package edu.kinoko.kidsbankingandroid.data.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val name: String,
    val category: String,
    val sum: Int,
    val date: LocalDateTime,
)
