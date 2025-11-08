package edu.kinoko.kidsbankingandroid.ui.pay.util

import androidx.core.net.toUri
import edu.kinoko.kidsbankingandroid.api.request.PayRequest
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun parsePayRequest(raw: String?): PayRequest? {
    if (raw.isNullOrBlank()) return null

    runCatching { return json.decodeFromString<PayRequest>(raw) }.onFailure { /* ignore */ }

    return runCatching {
        val uri = raw.toUri()
        val shopId = uri.getQueryParameter("shopId")?.toInt()
        val sum = uri.getQueryParameter("sum")?.toInt()
        if (shopId != null && sum != null) PayRequest(shopId, sum) else null
    }.getOrNull()
}