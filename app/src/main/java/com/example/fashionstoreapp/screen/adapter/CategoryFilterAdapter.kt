package com.example.fashionstoreapp.screen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.databinding.ItemCategoryBinding

class CategoryFilterAdapter(var listCategory: List<Category>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((Category) -> Unit)? = null
    var listCategoryId = mutableListOf<Int>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return listCategory.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val category = listCategory[position]
        (holder as ViewHolder).onBind(category)

        val isSelected = listCategoryId.contains(category.id)
        if (isSelected) {
            holder.binding.categoryWrapper.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.primary_color
                )
            )
            holder.binding.categoryName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        } else {
            holder.binding.categoryWrapper.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
            holder.binding.categoryName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.txt_color2
                )
            )
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(listCategory[position])
            if (isSelected) {
                listCategoryId.remove(category.id)
            } else {
                listCategoryId.add(category.id)
            }
            notifyDataSetChanged()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Category>) {
        this.listCategory = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(category: Category) {
            binding.apply {
                categoryName.text = category.categoryName
                categoryName.isSelected = true
            }
        }
    }
}
