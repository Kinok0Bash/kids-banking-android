package edu.kinoko.kidsbankingandroid.data.store

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import edu.kinoko.kidsbankingandroid.data.dto.Transaction

object TransactionHistoryStore {
    var allTransactions by mutableStateOf(listOf<Transaction>())
    var lastTransactions by mutableStateOf(listOf<Transaction>())
}