package edu.kinoko.kidsbankingandroid.data.enums

enum class TransactionType {
    QR_PAY,
    TRANSFER;

    companion object {
        fun parce(str: String) = when {
            str == QR_PAY.name -> QR_PAY
            str == TRANSFER.name -> TRANSFER
            else -> throw IllegalArgumentException("Тип транзакции $str не существует")
        }
    }
}