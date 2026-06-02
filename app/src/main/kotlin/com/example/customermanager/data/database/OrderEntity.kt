package com.example.customermanager.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val customerId: Int = 0,
    val customerName: String = "",
    val phoneNumber: String = "",
    val message: String = "",
    val status: String = "active", // active or completed
    val createdAt: Long = System.currentTimeMillis(),
    val sentAt: Long = 0
)
