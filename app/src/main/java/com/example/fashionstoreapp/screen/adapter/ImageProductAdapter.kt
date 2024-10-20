package com.example.fashionstoreapp.screen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fashionstoreapp.databinding.ItemImageBinding
import com.example.fashionstoreapp.databinding.ItemProductSlideBinding

class ImageProductAdapter(private var list: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding =
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(image: String) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(image)
                    .into(imageProduct)
            }
        }
    }
}