package edu.kinoko.kidsbankingandroid.data.store

import edu.kinoko.kidsbankingandroid.data.dto.Transaction

object TransactionHistoryStore {
    var allTransactions: List<Transaction> = listOf()
    var lastTransactions: List<Transaction> = listOf()
}