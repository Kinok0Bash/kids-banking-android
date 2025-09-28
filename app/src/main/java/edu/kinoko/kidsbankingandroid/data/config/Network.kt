package edu.kinoko.kidsbankingandroid.data.config

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import edu.kinoko.kidsbankingandroid.api.AuthClient
import edu.kinoko.kidsbankingandroid.data.store.AccessTokenStore
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.UUID
import java.util.concurrent.TimeUnit

object Network {
    lateinit var access: AccessTokenStore
        private set
    lateinit var cookieJar: PersistCookieJar
        private set

    lateinit var baseClient: OkHttpClient
        private set
    lateinit var authedClient: OkHttpClient
        private set

    lateinit var retrofit: Retrofit
        private set
    lateinit var retrofitAuthed: Retrofit
        private set

    // API для «публичных» auth-эндпоинтов (login/registration/refresh/logout)
    lateinit var authClient: AuthClient
        private set

    // Тот же интерфейс, но за ним клиент с Authenticator (для whoAmI и всего защищённого)
    lateinit var authClientAuthed: AuthClient
        private set

    private val serializers = SerializersModule {
        contextual(UUID::class, UUIDSerializer)
    }

    private val json = Json {
        serializersModule = serializers
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = false
    }

    private val contentType = "application/json".toMediaType()

    fun init(appContext: Context, baseUrl: String) {
        access = AccessTokenStore(appContext)
        cookieJar = PersistCookieJar(appContext)

        baseClient = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(AuthInterceptor(access))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(baseClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        // Публичный клиент только для вызова /auth/*
        authClient = retrofit.create(AuthClient::class.java)

        // Клиент с авто-рефрешем: 401 -> GET /auth/refresh (по cookie) -> retry с новым Bearer
        authedClient = baseClient.newBuilder()
            .authenticator(TokenAuthenticator(authClient, access))
            .build()

        retrofitAuthed = retrofit.newBuilder()
            .client(authedClient)
            .build()

        // Тот же интерфейс, но за ним клиент с авто-рефрешем
        authClientAuthed = retrofitAuthed.create(AuthClient::class.java)
    }
}
