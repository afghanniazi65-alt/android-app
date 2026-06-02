package com.example.customermanager.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE status = 'active' ORDER BY createdAt DESC")
    fun getActiveOrders(): LiveData<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE status = 'completed' ORDER BY sentAt DESC")
    fun getCompletedOrders(): LiveData<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: Int): OrderEntity?

    @Query("UPDATE orders SET status = 'completed', sentAt = :sentAt WHERE id = :id")
    suspend fun completeOrder(id: Int, sentAt: Long)
}
