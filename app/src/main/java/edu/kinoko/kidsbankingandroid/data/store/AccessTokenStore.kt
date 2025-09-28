package edu.kinoko.kidsbankingandroid.data.store

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.util.Base64

class AccessTokenStore(private val context: Context) {
    private val KEY_CT = stringPreferencesKey("access_token_ct")
    private val KEY_IV = stringPreferencesKey("access_token_iv")

    suspend fun get(): String? {
        val prefs = context.secureDataStore.data.first()
        val ctB64 = prefs[KEY_CT] ?: return null
        val ivB64 = prefs[KEY_IV] ?: return null
        val ct = Base64.decode(ctB64, Base64.DEFAULT)
        val iv = Base64.decode(ivB64, Base64.DEFAULT)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), GCMParameterSpec(128, iv))
        return String(cipher.doFinal(ct), Charsets.UTF_8)
    }

    suspend fun set(token: String?) {
        if (token == null) {
            context.secureDataStore.edit { it.remove(KEY_CT); it.remove(KEY_IV) }
            return
        }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val ct = cipher.doFinal(token.toByteArray(Charsets.UTF_8))
        val iv = cipher.iv
        context.secureDataStore.edit {
            it[KEY_CT] = Base64.encodeToString(ct, Base64.NO_WRAP)
            it[KEY_IV] = Base64.encodeToString(iv, Base64.NO_WRAP)
        }
    }

    private fun getOrCreateKey(): SecretKey {
        val ks = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        (ks.getEntry(ALIAS, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }

        val gen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(
            ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)
            .build()
        gen.init(spec)
        return gen.generateKey()
    }

    companion object {
        val Context.secureDataStore by preferencesDataStore(name = DS_NAME)

        private const val ALIAS = "access_token_key"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val DS_NAME = "secure_store"
    }
}
