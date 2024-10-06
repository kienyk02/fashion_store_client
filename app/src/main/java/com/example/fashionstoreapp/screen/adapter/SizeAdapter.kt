package com.example.fashionstoreapp.screen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.databinding.ItemCategoryBinding
import com.example.fashionstoreapp.databinding.ItemSizeBinding

class SizeAdapter(private var listSize: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null
    var selectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding =
            ItemSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return listSize.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as ViewHolder).onBind(listSize[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(listSize[position])
            this.selectedPosition = position
            notifyDataSetChanged()
        }
        if (selectedPosition == position) {
            holder.binding.sizeWrapper.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.txt_color2
                )
            )
            holder.binding.sizeName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        } else {
            holder.binding.sizeWrapper.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
            holder.binding.sizeName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.txt_color1
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<String>) {
        this.listSize = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSizeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(size: String) {
            binding.apply {
                sizeName.text = size
            }
        }
    }
}