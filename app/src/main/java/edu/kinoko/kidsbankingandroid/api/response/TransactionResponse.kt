package edu.kinoko.kidsbankingandroid.api.response

import edu.kinoko.kidsbankingandroid.data.enums.TransactionStatus
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    val status: TransactionStatus,
    val sum: Int
)
