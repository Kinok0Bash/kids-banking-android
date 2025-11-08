package edu.kinoko.kidsbankingandroid.data.service

import edu.kinoko.kidsbankingandroid.api.BalanceClient
import edu.kinoko.kidsbankingandroid.data.store.BalanceStore

class BalanceService(
    private val api: BalanceClient,
) {

    suspend fun getParentBalance(): Pair<Int, Int?> {
        val resp = api.getParentBalance()
        BalanceStore.parentBalance = resp.parentBalance
        BalanceStore.childBalance = resp.childBalance ?: 0
        return resp.parentBalance to resp.childBalance
    }

    suspend fun getChildBalance(): Int {
        val resp = api.getChildBalance()
        BalanceStore.parentBalance = 0
        BalanceStore.childBalance = resp.balance
        return resp.balance
    }
}