package com.example.customermanager.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PreferencesManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "license_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveLicenseKey(key: String) {
        sharedPreferences.edit().putString("license_key", key).apply()
    }

    fun getLicenseKey(): String? {
        return sharedPreferences.getString("license_key", null)
    }

    fun clearLicense() {
        sharedPreferences.edit().remove("license_key").apply()
    }

    fun setLanguage(language: String) {
        sharedPreferences.edit().putString("language", language).apply()
    }

    fun getLanguage(): String {
        return sharedPreferences.getString("language", "en") ?: "en"
    }

    fun isLicenseActivated(): Boolean {
        return sharedPreferences.getString("license_key", null) != null
    }
}
