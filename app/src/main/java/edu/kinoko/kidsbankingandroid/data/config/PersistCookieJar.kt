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
    // простая мапа домен -> cookies
    private val memory = mutableMapOf<String, MutableList<Cookie>>()
    private var restored = false

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.isEmpty()) return
        val list = memory.getOrPut(url.topPrivateDomainOrHost()) { mutableListOf() }
        cookies.forEach { c ->
            list.removeAll { it.name == c.name && it.matches(url) }
            list += c
        }
        persist()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        if (!restored) restore()
        return memory
            .filterKeys { domain -> url.host.endsWith(domain) }
            .values.flatten()
            .filter { it.matches(url) }
    }

    private fun persist() = runBlocking {
        val all = memory.values.flatten().joinToString(";;") { it.toString() }
        context.cookieDataStore.edit { it[KEY] = all }
    }

    private fun restore() = runBlocking {
        val s = context.cookieDataStore.data.first()[KEY].orEmpty()
        if (s.isNotBlank()) {
            val list = s.split(";;")
                .mapNotNull {
                    Cookie.parse("https://dummy".toHttpUrl(), it)
                }
            memory.clear()
            list.groupBy { it.domain }.forEach { (domain, cs) ->
                memory[domain] = cs.toMutableList()
            }
        }
        restored = true
    }

    private fun HttpUrl.topPrivateDomainOrHost(): String = this.topPrivateDomain() ?: host

    companion object {
        private const val DS_NAME = "cookie_store"
        private val KEY = stringPreferencesKey("cookies")
        val Context.cookieDataStore by preferencesDataStore(name = DS_NAME)
    }
}
