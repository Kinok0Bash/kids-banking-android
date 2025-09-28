package edu.kinoko.kidsbankingandroid.data.config

import edu.kinoko.kidsbankingandroid.api.AuthClient
import edu.kinoko.kidsbankingandroid.data.store.AccessTokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val api: AuthClient,
    private val store: AccessTokenStore
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.priorResponse != null) {
            return null // уже ретраили
        }

        val newToken = try {
            val refreshed = runBlocking { api.refresh() } // refreshCookie поедет из CookieJar
            refreshed.token
        } catch (_: Exception) {
            return null
        }

        runBlocking { store.set(newToken) }
        return response.request
            .newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }
}
