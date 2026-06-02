package com.example.customermanager.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.customermanager.databinding.ItemCustomerBinding
import com.example.customermanager.data.database.CustomerEntity

class CustomerAdapter(
    private val onItemClick: (CustomerEntity) -> Unit
) : ListAdapter<CustomerEntity, CustomerAdapter.CustomerViewHolder>(CustomerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding = ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CustomerViewHolder(
        private val binding: ItemCustomerBinding,
        private val onItemClick: (CustomerEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(customer: CustomerEntity) {
            binding.apply {
                nameTextView.text = customer.name
                phoneTextView.text = "${customer.countryCode} ${customer.phoneNumber}"
                billTextView.text = "Bill: ${customer.billNumber}"
                root.setOnClickListener { onItemClick(customer) }
            }
        }
    }

    class CustomerDiffCallback : DiffUtil.ItemCallback<CustomerEntity>() {
        override fun areItemsTheSame(oldItem: CustomerEntity, newItem: CustomerEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CustomerEntity, newItem: CustomerEntity) =
            oldItem == newItem
    }
}
