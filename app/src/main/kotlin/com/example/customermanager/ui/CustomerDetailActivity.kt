package com.example.customermanager.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.customermanager.databinding.ActivityCustomerDetailBinding
import com.example.customermanager.data.database.CustomerDatabase
import com.example.customermanager.data.database.CustomerEntity
import com.example.customermanager.data.database.OrderEntity
import kotlinx.coroutines.launch

class CustomerDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerDetailBinding
    private lateinit var database: CustomerDatabase
    private var customerId: Int = -1
    private var currentCustomer: CustomerEntity? = null

    private val countryCodes = mapOf(
        "UAE" to "+971",
        "KSA" to "+966",
        "Kuwait" to "+965",
        "Qatar" to "+974",
        "Bahrain" to "+973",
        "Oman" to "+968",
        "Egypt" to "+20",
        "Jordan" to "+962"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = CustomerDatabase.getInstance(this)
        customerId = intent.getIntExtra("customer_id", -1)

        setupCountryCodeSpinner()
        setupListeners()

        if (customerId != -1) {
            loadCustomer()
        }
    }

    private fun setupCountryCodeSpinner() {
        val countries = countryCodes.keys.toList()
        binding.countryCodeSpinner.adapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            countries
        )
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener { saveCustomer() }
        binding.sendWhatsAppButton.setOnClickListener { sendWhatsApp() }
        binding.deleteButton.setOnClickListener { deleteCustomer() }
    }

    private fun loadCustomer() {
        lifecycleScope.launch {
            currentCustomer = database.customerDao().getCustomerById(customerId)
            currentCustomer?.let { customer ->
                binding.nameEditText.setText(customer.name)
                binding.phoneEditText.setText(customer.phoneNumber)
                binding.billNumberEditText.setText(customer.billNumber)

                val countryName = countryCodes.entries.find { it.value == customer.countryCode }?.key ?: "UAE"
                binding.countryCodeSpinner.setSelection(
                    (binding.countryCodeSpinner.adapter as android.widget.ArrayAdapter<*>).getPosition(countryName)
                )
                binding.deleteButton.isEnabled = true
            }
        }
    }

    private fun saveCustomer() {
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val billNumber = binding.billNumberEditText.text.toString().trim()
        val country = binding.countryCodeSpinner.selectedItem.toString()
        val countryCode = countryCodes[country] ?: "+971"

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            if (customerId == -1) {
                database.customerDao().insertCustomer(
                    CustomerEntity(
                        name = name,
                        phoneNumber = phone,
                        billNumber = billNumber,
                        countryCode = countryCode
                    )
                )
                Toast.makeText(this@CustomerDetailActivity, "Customer added", Toast.LENGTH_SHORT).show()
            } else {
                database.customerDao().updateCustomer(
                    CustomerEntity(
                        id = customerId,
                        name = name,
                        phoneNumber = phone,
                        billNumber = billNumber,
                        countryCode = countryCode
                    )
                )
                Toast.makeText(this@CustomerDetailActivity, "Customer updated", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun sendWhatsApp() {
        val phone = binding.phoneEditText.text.toString().trim()
        val country = binding.countryCodeSpinner.selectedItem.toString()
        val countryCode = countryCodes[country] ?: "+971"
        val message = binding.messageEditText.text.toString().trim()

        if (phone.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please enter phone and message", Toast.LENGTH_SHORT).show()
            return
        }

        val fullPhone = if (phone.startsWith("+")) phone else "$countryCode${phone.replaceFirst("^0+".toRegex(), "")}"
        val whatsappUri = "https://wa.me/$fullPhone?text=${java.net.URLEncoder.encode(message, "UTF-8")}"

        lifecycleScope.launch {
            if (customerId != -1) {
                database.orderDao().insertOrder(
                    OrderEntity(
                        customerId = customerId,
                        customerName = binding.nameEditText.text.toString(),
                        phoneNumber = phone,
                        message = message,
                        status = "completed",
                        sentAt = System.currentTimeMillis()
                    )
                )
            }

            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(whatsappUri))
            startActivity(intent)
        }
    }

    private fun deleteCustomer() {
        if (customerId != -1) {
            lifecycleScope.launch {
                database.customerDao().deleteCustomerById(customerId)
                Toast.makeText(this@CustomerDetailActivity, "Customer deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
