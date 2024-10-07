package com.example.fashionstoreapp.screen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionstoreapp.data.model.ItemOrder
import com.example.fashionstoreapp.databinding.ItemItemorderBinding

class ItemOrderAdapter(private var list: List<ItemOrder>) :
    RecyclerView.Adapter<ItemOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemItemorderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<ItemOrder>) {
        this.list = newList
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemItemorderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(itemOrder: ItemOrder) {
            binding.apply {

            }
        }
        private fun formatPrice(price: Int): String {
            return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
        }
    }
}