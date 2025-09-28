package edu.kinoko.kidsbankingandroid.data.config

import android.content.Context
import com.squareup.moshi.Moshi
import edu.kinoko.kidsbankingandroid.api.AuthClient
import edu.kinoko.kidsbankingandroid.data.store.AccessTokenStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

fun provideOkHttp(
    context: Context,
    store: AccessTokenStore
): OkHttpClient {
    val cookieJar = PersistCookieJar(context)
    return OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor(AuthInterceptor(store))
        .authenticator { route, resp -> null } // заглушка, добавим ниже
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}

fun provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()

fun wireAuth(
    retrofit: Retrofit,
    client: OkHttpClient,
    store: AccessTokenStore
): Pair<AuthClient, OkHttpClient> {
    val api = retrofit.create(AuthClient::class.java)
    // собрать новый клиент с настоящим Authenticator, который знает про api
    val newClient = client.newBuilder()
        .authenticator(TokenAuthenticator(api, store))
        .build()
    return api to newClient
}
