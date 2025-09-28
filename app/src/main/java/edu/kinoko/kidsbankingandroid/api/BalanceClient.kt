package edu.kinoko.kidsbankingandroid.api

import edu.kinoko.kidsbankingandroid.api.response.ChildBalanceResponse
import edu.kinoko.kidsbankingandroid.api.response.ParentBalanceResponse
import retrofit2.http.GET

interface BalanceClient {

    @GET("balance/parent")
    suspend fun getParentBalance(): ParentBalanceResponse

    @GET("balance/child")
    suspend fun getChildBalance(): ChildBalanceResponse
}