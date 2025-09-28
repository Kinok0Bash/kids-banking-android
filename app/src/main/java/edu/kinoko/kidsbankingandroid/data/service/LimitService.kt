package edu.kinoko.kidsbankingandroid.data.service

import edu.kinoko.kidsbankingandroid.api.LimitClient
import edu.kinoko.kidsbankingandroid.data.dto.Limit
import edu.kinoko.kidsbankingandroid.data.store.LimitListStore

class LimitService(
    private val api: LimitClient
) {

    suspend fun getUserLimitList(): List<Limit> {
        val resp = api.getUserLimitList()
        LimitListStore.limitList = resp
        return resp
    }

    suspend fun setUserLimitList(req: List<Limit>): List<Limit> {
        val resp = api.setUserLimitList(req)
        LimitListStore.limitList = resp
        return resp
    }
}