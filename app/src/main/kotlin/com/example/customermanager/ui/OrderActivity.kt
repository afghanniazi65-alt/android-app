package com.example.customermanager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customermanager.databinding.ActivityOrderBinding
import com.example.customermanager.data.database.CustomerDatabase
import com.example.customermanager.ui.adapter.OrderAdapter

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var database: CustomerDatabase
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = CustomerDatabase.getInstance(this)
        setupRecyclerView()
        loadOrders()
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter()
        binding.ordersRecyclerView.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(this@OrderActivity)
        }
    }

    private fun loadOrders() {
        database.orderDao().getCompletedOrders().observe(this) { orders ->
            orderAdapter.submitList(orders)
        }
    }
}
