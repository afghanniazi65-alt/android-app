package com.example.customermanager.data

import android.content.Context
import android.provider.Settings
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LicenseValidator(private val context: Context) {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun validateLicense(licenseKey: String): LicenseResult {
        return try {
            val document = firestore.collection("licenses")
                .document(licenseKey)
                .get()
                .await()

            if (!document.exists()) {
                return LicenseResult.Invalid("License key not found")
            }

            val license = document.toObject(License::class.java) ?: return LicenseResult.Invalid("Invalid license data")

            // Check if active
            if (!license.isActive) {
                return LicenseResult.Blocked(license.blockedReason)
            }

            // Check expiration
            val currentTime = System.currentTimeMillis()
            if (license.expiresAt > 0 && currentTime > license.expiresAt) {
                return LicenseResult.Expired
            }

            // Check device
            val deviceId = getDeviceId()
            if (license.allowedDevices.isNotEmpty() && !license.allowedDevices.contains(deviceId)) {
                return LicenseResult.DeviceNotAllowed("This device is not registered for this license")
            }

            LicenseResult.Valid(license)
        } catch (e: Exception) {
            LicenseResult.Error(e.message ?: "Unknown error")
        }
    }

    private fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}

data class License(
    val licenseKey: String = "",
    val isActive: Boolean = false,
    val expiresAt: Long = 0,
    val blockedReason: String = "",
    val allowedDevices: List<String> = emptyList(),
    val createdAt: Long = 0,
    val businessName: String = ""
)

sealed class LicenseResult {
    data class Valid(val license: License) : LicenseResult()
    data class Invalid(val message: String) : LicenseResult()
    data class Blocked(val reason: String) : LicenseResult()
    object Expired : LicenseResult()
    data class DeviceNotAllowed(val message: String) : LicenseResult()
    data class Error(val message: String) : LicenseResult()
}
