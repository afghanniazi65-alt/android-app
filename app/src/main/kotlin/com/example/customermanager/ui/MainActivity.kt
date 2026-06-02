package com.example.customermanager.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customermanager.databinding.ActivityMainBinding
import com.example.customermanager.data.database.CustomerDatabase
import com.example.customermanager.ui.adapter.CustomerAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: CustomerDatabase
    private lateinit var customerAdapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = CustomerDatabase.getInstance(this)
        setupRecyclerView()
        loadCustomers()

        binding.addCustomerButton.setOnClickListener {
            startActivity(Intent(this, CustomerDetailActivity::class.java))
        }

        binding.ordersButton.setOnClickListener {
            startActivity(Intent(this, OrderActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        customerAdapter = CustomerAdapter { customer ->
            val intent = Intent(this, CustomerDetailActivity::class.java)
            intent.putExtra("customer_id", customer.id)
            startActivity(intent)
        }
        binding.customersRecyclerView.apply {
            adapter = customerAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun loadCustomers() {
        database.customerDao().getAllCustomers().observe(this) { customers ->
            customerAdapter.submitList(customers)
        }
    }

    override fun onResume() {
        super.onResume()
        loadCustomers()
    }
}
