package com.example.fashionstoreapp.screen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Address
import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.databinding.ItemAddressBinding
import com.example.fashionstoreapp.databinding.ItemCategoryBinding

class AddressAdapter(var listAddress: List<Address>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((Address) -> Unit)? = null
    var selectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding =
            ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return listAddress.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as ViewHolder).onBind(listAddress[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(listAddress[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Address>) {
        this.listAddress = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(address: Address) {
            binding.apply {
                txtAddress.text = address.address
                txtWard.text =
                    "${address.wardName}, ${address.districtName}, ${address.provinceName}"

                if (address.active == 1) binding.txtDefaultCheck.visibility = View.VISIBLE
                else binding.txtDefaultCheck.visibility = View.GONE
            }
        }
    }
}