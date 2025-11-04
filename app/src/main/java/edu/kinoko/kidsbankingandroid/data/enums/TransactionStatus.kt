package edu.kinoko.kidsbankingandroid.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class TransactionStatus {
    OK,
    FAIL,
    FORBIDDEN;

    companion object {
        fun parce(str: String): TransactionStatus {
            return when {
                str.uppercase() == "OK" -> OK
                str.uppercase() == "FAIL" -> FAIL
                str.uppercase() == "FORBIDDEN" -> FORBIDDEN
                else -> throw IllegalArgumentException("Статус $str не существует")
            }
        }
    }
}
