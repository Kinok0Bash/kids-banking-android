package edu.kinoko.kidsbankingandroid.api

import edu.kinoko.kidsbankingandroid.data.dto.Limit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface LimitClient {

    @GET("limit")
    suspend fun getUserLimitList(): List<Limit>

    @PUT("limit")
    suspend fun setUserLimitList(@Body req: List<Limit>): List<Limit>
}