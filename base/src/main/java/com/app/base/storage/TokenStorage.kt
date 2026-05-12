package com.app.base.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

interface TokenStorage {
    fun getToken(): String?
    fun saveToken(token: String)
    fun clearToken()
}

@Singleton
class TokenStorageImpl @Inject constructor(
    @ApplicationContext context: Context
) : TokenStorage {

    private val prefs = context.getSharedPreferences("shiphub_prefs", Context.MODE_PRIVATE)

    override fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    override fun saveToken(token: String) {
        prefs.edit { putString(KEY_TOKEN, token) }
    }

    override fun clearToken() {
        prefs.edit { remove(KEY_TOKEN) }
    }

    companion object {
        private const val KEY_TOKEN = "token"
    }
}
