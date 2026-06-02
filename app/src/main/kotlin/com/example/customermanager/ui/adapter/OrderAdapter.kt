package com.example.customermanager.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.customermanager.databinding.ItemOrderBinding
import com.example.customermanager.data.database.OrderEntity
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter : ListAdapter<OrderEntity, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderViewHolder(
        private val binding: ItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderEntity) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.apply {
                customerNameTextView.text = order.customerName
                messageTextView.text = order.message
                dateTextView.text = dateFormat.format(Date(order.sentAt))
            }
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<OrderEntity>() {
        override fun areItemsTheSame(oldItem: OrderEntity, newItem: OrderEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: OrderEntity, newItem: OrderEntity) =
            oldItem == newItem
    }
}
