package com.example.customermanager.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val phoneNumber: String = "",
    val billNumber: String = "",
    val countryCode: String = "+971",
    val createdAt: Long = System.currentTimeMillis()
)
