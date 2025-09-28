package edu.kinoko.kidsbankingandroid.api.response

import kotlinx.serialization.Serializable

@Serializable
data class ChildBalanceResponse(
    val balance: Int
)
