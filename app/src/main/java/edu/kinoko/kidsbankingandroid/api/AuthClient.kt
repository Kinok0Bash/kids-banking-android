package edu.kinoko.kidsbankingandroid.api

import edu.kinoko.kidsbankingandroid.api.request.AuthenticationRequest
import edu.kinoko.kidsbankingandroid.api.request.RegistrationRequest
import edu.kinoko.kidsbankingandroid.api.response.AuthenticationResponse
import edu.kinoko.kidsbankingandroid.data.dto.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthClient {

    @POST("auth/authorization")
    suspend fun authorization(@Body req: AuthenticationRequest): AuthenticationResponse

    @POST("auth/registration")
    suspend fun registration(@Body req: RegistrationRequest): AuthenticationResponse

    @POST("auth/logout")
    suspend fun logout(): Map<String, String>

    @GET("auth/refresh")
    suspend fun refresh(): AuthenticationResponse

    @GET("auth/who-am-i")
    suspend fun whoAmI(): User
}
