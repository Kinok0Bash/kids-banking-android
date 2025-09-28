package edu.kinoko.kidsbankingandroid.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class TransactionStatus {
    OK,
    FAIL,
    FORBIDDEN
}
