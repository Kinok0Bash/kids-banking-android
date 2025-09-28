package edu.kinoko.kidsbankingandroid.data.service

import edu.kinoko.kidsbankingandroid.api.AuthClient
import edu.kinoko.kidsbankingandroid.api.request.AuthenticationRequest
import edu.kinoko.kidsbankingandroid.api.request.RegistrationRequest
import edu.kinoko.kidsbankingandroid.api.response.AuthenticationResponse
import edu.kinoko.kidsbankingandroid.data.store.AccessTokenStore
import edu.kinoko.kidsbankingandroid.data.dto.User

class AuthService(
    private val api: AuthClient,
    private val tokens: AccessTokenStore
) {
    suspend fun login(req: AuthenticationRequest): AuthenticationResponse {
        val resp = api.authorization(req)
        tokens.set(resp.token)
        return resp
    }

    suspend fun register(req: RegistrationRequest): AuthenticationResponse {
        val resp = api.registration(req)
        tokens.set(resp.token)
        return resp
    }

    suspend fun refresh(): AuthenticationResponse {
        val resp = api.refresh()
        tokens.set(resp.token)
        return resp
    }

    suspend fun logout() {
        runCatching { api.logout() }
        tokens.set(null)
    }

    suspend fun whoAmI(): User = api.whoAmI()
}
