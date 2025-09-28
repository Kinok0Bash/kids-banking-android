package edu.kinoko.kidsbankingandroid.data.config

import edu.kinoko.kidsbankingandroid.data.store.AccessTokenStore
import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.runBlocking

class AuthInterceptor(private val store: AccessTokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { store.get() }
        val req = if (token != null) {
            chain.request()
                .newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(req)
    }
}
