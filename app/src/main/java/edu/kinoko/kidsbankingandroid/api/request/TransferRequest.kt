package edu.kinoko.kidsbankingandroid.api.request

import kotlinx.serialization.Serializable

@Serializable
data class TransferRequest(
    val sum: Int
)
