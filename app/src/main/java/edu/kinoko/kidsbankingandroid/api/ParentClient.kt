package edu.kinoko.kidsbankingandroid.api

import edu.kinoko.kidsbankingandroid.api.request.RegistrationRequest
import edu.kinoko.kidsbankingandroid.data.dto.User
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ParentClient {

    @POST("parent/new-child")
    suspend fun createChildAccount(@Body req: RegistrationRequest): User

    @PUT("parent/salary")
    suspend fun getSalary(): Map<String, String>
}