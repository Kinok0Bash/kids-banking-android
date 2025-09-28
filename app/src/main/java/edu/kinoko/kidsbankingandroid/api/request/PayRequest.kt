package edu.kinoko.kidsbankingandroid.api.request

import kotlinx.serialization.Serializable

@Serializable
data class PayRequest(
    val shopId: Int,
    val sum: Int
)
