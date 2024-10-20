package com.example.fashionstoreapp.screen.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fashionstoreapp.data.model.Cart
import com.example.fashionstoreapp.databinding.ItemCartBinding

class CartAdapter(private var listCart: List<Cart>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((Cart) -> Unit)? = null
    var onDeleteClick: ((Cart) -> Unit)? = null
    var onDecreaseClick: ((Cart) -> Unit)? = null
    var onIncreaseClick: ((Cart) -> Unit)? = null
    var onUpdateClick: ((Int, Int) -> Unit)? = null

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
            if (!isRightPartVisible) {
                onItemClick?.invoke(listCart[position])
            }else{
                isRightPartVisible = false
                holder.binding.rightPart.visibility = View.GONE
                holder.binding.leftPart.animate().translationX(0f).setDuration(250).start()
            }

        }

        holder.binding.rightPart.setOnClickListener {
            isRightPartVisible = false
            holder.binding.rightPart.visibility = View.GONE
            holder.binding.leftPart.animate().translationX(0f).setDuration(250).start()
            onDeleteClick?.invoke(listCart[position])
        }

        holder.binding.btnAddCart.setOnClickListener {
            onIncreaseClick?.invoke(listCart[position])
        }

        holder.binding.btnMinusCart.setOnClickListener {
            onDecreaseClick?.invoke(listCart[position])
        }

        val edtQuantity = holder.binding.edtQuantity
        edtQuantity.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (edtQuantity.text.toString() == ""
                    || edtQuantity.text.toString() == "0"
                    || edtQuantity.text.toString() == "00"
                ) {
                    edtQuantity.setText("1")
                }
                val value = edtQuantity.text.toString().toInt()
                if (value > 0) {
                    onUpdateClick?.invoke(listCart[position].id!!, value)
                }
                true
            } else {
                false
            }
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
                Glide.with(binding.root.context)
                    .load(cart.color.images[0])
                    .into(imgProduct)
                txtNameP.text = cart.product.name
                txtColorP.text = cart.color.name
                txtSizeP.text = cart.size.name
                txtPriceP.text = formatPrice(cart.price)
                edtQuantity.setText(cart.quantity.toString())
            }
        }
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }
}