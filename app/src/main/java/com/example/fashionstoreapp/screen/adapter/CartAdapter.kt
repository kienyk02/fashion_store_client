package com.example.fashionstoreapp.screen.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionstoreapp.data.model.Cart
import com.example.fashionstoreapp.databinding.ItemCartBinding

class CartAdapter(private var listCart: List<Cart>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((Cart) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return listCart.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        var isRightPartVisible = false

        (holder as ViewHolder).onBind(listCart[position])

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(listCart[position])
            if (isRightPartVisible) {
                holder.binding.rightPart.visibility = View.GONE
                holder.binding.leftPart.animate().translationX(0f).setDuration(250).start()
                isRightPartVisible = false
            }
        }

        holder.binding.rightPart.setOnClickListener {
            Log.d("deletecart", "delete cart")
        }

        holder.itemView.setOnLongClickListener {
            holder.binding.rightPart.visibility = View.VISIBLE
            holder.binding.leftPart.animate().translationX(-100f).setDuration(250).start()
            isRightPartVisible = true
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Cart>) {
        this.listCart = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(cart: Cart) {
            binding.apply {
                txtNameP.text = cart.product.name
                txtPriceP.text = cart.product.price.toString()
                edtQuantity.setText(cart.quantity.toString())

            }
        }
    }
}