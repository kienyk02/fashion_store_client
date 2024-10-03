package com.example.fashionstoreapp


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fashionstoreapp.databinding.ItemProductBinding

class ProductAdapter(private var listProduct: List<Product>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((Product) -> Unit)? = null
    var onAddCartClick: (() -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as ViewHolder).onBind(listProduct[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(listProduct[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Product>) {
        this.listProduct = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(product: Product) {
            binding.apply {
                productName.text = product.name
                productPrice.text = formatPrice(product.price)

                productName.isSelected = true

                btnAddCart.setOnClickListener {
                    onAddCartClick?.invoke()
                }
            }
        }
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + "đ"
    }
}
