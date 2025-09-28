package edu.kinoko.kidsbankingandroid.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Limit(
    val id: Int,
    val name: String,
    var isLimit: Boolean,
)
