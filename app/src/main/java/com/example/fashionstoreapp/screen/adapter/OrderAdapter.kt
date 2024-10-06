package com.example.fashionstoreapp.screen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionstoreapp.data.model.Order
import com.example.fashionstoreapp.databinding.ItemOrderBinding

class OrderAdapter(private var list: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    var onItemClick: ((Order) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(
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

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<Order>) {
        this.list = newData
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {

            }
        }

        private fun formatPrice(price: Int): String {
            return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
        }
    }
}