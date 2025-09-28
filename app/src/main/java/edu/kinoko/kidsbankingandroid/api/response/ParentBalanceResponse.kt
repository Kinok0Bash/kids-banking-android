package edu.kinoko.kidsbankingandroid.api.response

import kotlinx.serialization.Serializable

@Serializable
data class ParentBalanceResponse(
    val parentBalance: Int,
    val childBalance: Int? = null
)
