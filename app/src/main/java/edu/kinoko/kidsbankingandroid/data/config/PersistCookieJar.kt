package edu.kinoko.kidsbankingandroid.data.config

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

class PersistCookieJar(private val context: Context) : CookieJar {
    private val memory = mutableMapOf<String, MutableList<Cookie>>() // ключ: host
    private var restored = false

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.isEmpty()) return
        val hostKey = url.host // НЕ topPrivateDomain: host-only кука должна жить на точном хосте
        val list = memory.getOrPut(hostKey) { mutableListOf() }
        cookies.forEach { c ->
            list.removeAll { it.name == c.name && it.matches(url) }
            list += c
        }
        persist()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        if (!restored) restore()
        // Берём все куки из памяти и фильтруем стандартной логикой OkHttp
        return memory.values
            .asSequence()
            .flatten()
            .filter { it.matches(url) }
            .toList()
    }

    private fun persist() = runBlocking {
        // Сохраняем ВМЕСТЕ с хостом, таб-разделитель
        val all = buildString {
            memory.forEach { (host, list) ->
                list.forEach { c -> append(host).append('\t').append(c.toString()).append('\n') }
            }
        }
        context.cookieDataStore.edit { it[KEY] = all }
    }

    private fun restore() = runBlocking {
        val raw = context.cookieDataStore.data.first()[KEY].orEmpty()
        memory.clear()
        if (raw.isNotBlank()) {
            val now = System.currentTimeMillis()
            raw.lineSequence()
                .filter { it.isNotBlank() && it.contains('\t') }
                .mapNotNull { line ->
                    val i = line.indexOf('\t')
                    val host = line.substring(0, i)
                    val cookieStr = line.substring(i + 1)
                    val url = ("https://$host").toHttpUrl()
                    Cookie.parse(url, cookieStr)?.takeIf { it.expiresAt >= now }
                        ?.let { host to it }
                }
                .groupBy({ it.first }, { it.second })
                .forEach { (host, cs) -> memory[host] = cs.toMutableList() }
        }
        restored = true
    }

    companion object {
        private const val DS_NAME = "cookie_store"
        private val KEY = stringPreferencesKey("cookies")
        val Context.cookieDataStore by preferencesDataStore(name = DS_NAME)
    }
}

