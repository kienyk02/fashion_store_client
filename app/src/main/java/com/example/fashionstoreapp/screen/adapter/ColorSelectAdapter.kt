package com.example.fashionstoreapp.screen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Color
import com.example.fashionstoreapp.databinding.ItemColorBinding

class ColorSelectAdapter(private var list: List<Color>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((Color) -> Unit)? = null
    var selectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding =
            ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as ViewHolder).onBind(list[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
            this.selectedPosition = position
            notifyDataSetChanged()
        }
        if (selectedPosition == position) {
            holder.binding.imgColorWrapper.strokeColor = ContextCompat.getColor(
                holder.itemView.context,
                R.color.primary_color
            )

        } else {
            holder.binding.imgColorWrapper.strokeColor = ContextCompat.getColor(
                holder.itemView.context,
                R.color.white
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Color>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(color: Color) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(color.images[0])
                    .into(imgColor)
            }
        }
    }
}