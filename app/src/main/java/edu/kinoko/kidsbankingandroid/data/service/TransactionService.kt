package edu.kinoko.kidsbankingandroid.data.service

import edu.kinoko.kidsbankingandroid.api.TransactionClient
import edu.kinoko.kidsbankingandroid.api.request.PayRequest
import edu.kinoko.kidsbankingandroid.api.request.TransferRequest
import edu.kinoko.kidsbankingandroid.data.dto.Transaction
import edu.kinoko.kidsbankingandroid.data.store.TransactionHistoryStore

class TransactionService(
    private val api: TransactionClient
) {

    suspend fun getLastTransactions(): List<Transaction> {
        val resp = api.getLastTransactions()
        TransactionHistoryStore.lastTransactions = resp
        return resp
    }

    suspend fun getAllTransactions(): List<Transaction> {
        val resp = api.getAllTransactions()
        TransactionHistoryStore.allTransactions = resp
        return resp
    }

    suspend fun transfer(
        req: TransferRequest
    ) = api.transfer(req)

    suspend fun pay(
        req: PayRequest
    ) = api.pay(req)
}