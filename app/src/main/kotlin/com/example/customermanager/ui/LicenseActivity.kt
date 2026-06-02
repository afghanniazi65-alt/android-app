package com.example.customermanager.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.customermanager.databinding.ActivityLicenseBinding
import com.example.customermanager.data.LicenseValidator
import com.example.customermanager.data.LicenseResult
import com.example.customermanager.data.PreferencesManager
import kotlinx.coroutines.launch

class LicenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLicenseBinding
    private lateinit var licenseValidator: LicenseValidator
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        licenseValidator = LicenseValidator(this)
        preferencesManager = PreferencesManager(this)

        binding.activateButton.setOnClickListener {
            val licenseKey = binding.licenseKeyEditText.text.toString().trim()
            if (licenseKey.isEmpty()) {
                Toast.makeText(this, "Please enter license key", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            validateLicense(licenseKey)
        }
    }

    private fun validateLicense(licenseKey: String) {
        binding.activateButton.isEnabled = false
        binding.activateButton.text = "Validating..."

        lifecycleScope.launch {
            val result = licenseValidator.validateLicense(licenseKey)
            when (result) {
                is LicenseResult.Valid -> {
                    preferencesManager.saveLicenseKey(licenseKey)
                    Toast.makeText(
                        this@LicenseActivity,
                        "License activated! Welcome ${result.license.businessName}",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@LicenseActivity, MainActivity::class.java))
                    finish()
                }
                is LicenseResult.Invalid -> {
                    Toast.makeText(this@LicenseActivity, result.message, Toast.LENGTH_SHORT).show()
                    resetButton()
                }
                is LicenseResult.Blocked -> {
                    Toast.makeText(this@LicenseActivity, "License Blocked: ${result.reason}", Toast.LENGTH_LONG).show()
                    resetButton()
                }
                is LicenseResult.Expired -> {
                    Toast.makeText(this@LicenseActivity, "License has expired", Toast.LENGTH_SHORT).show()
                    resetButton()
                }
                is LicenseResult.DeviceNotAllowed -> {
                    Toast.makeText(this@LicenseActivity, result.message, Toast.LENGTH_SHORT).show()
                    resetButton()
                }
                is LicenseResult.Error -> {
                    Toast.makeText(this@LicenseActivity, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                    resetButton()
                }
            }
        }
    }

    private fun resetButton() {
        binding.activateButton.isEnabled = true
        binding.activateButton.text = "Activate License"
    }
}
