package edu.kinoko.kidsbankingandroid.api

import edu.kinoko.kidsbankingandroid.api.request.PayRequest
import edu.kinoko.kidsbankingandroid.api.request.TransferRequest
import edu.kinoko.kidsbankingandroid.api.response.TransactionResponse
import edu.kinoko.kidsbankingandroid.data.dto.Transaction
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface TransactionClient {

    @GET("transaction/last")
    suspend fun getLastTransactions(): List<Transaction>

    @GET("transaction/history")
    suspend fun getAllTransactions(): List<Transaction>

    @PUT("transaction/transfer")
    suspend fun transfer(@Body req: TransferRequest): TransactionResponse

    @POST("transaction/pay")
    suspend fun pay(@Body req: PayRequest): TransactionResponse
}