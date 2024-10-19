package com.example.fashionstoreapp.screen.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fashionstoreapp.data.model.Product
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

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<Product>) {
        val mutableList = this.listProduct.toMutableList()
        mutableList.addAll(list)
        this.listProduct = mutableList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortProductsByPriceAscending() {
        listProduct = listProduct.sortedBy { it.price }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortProductsByPriceDescending() {
        listProduct = listProduct.sortedByDescending { it.price }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(product: Product) {
            binding.apply {
                productName.text = product.name
                productPrice.text = formatPrice(product.price)

                productName.isSelected = true

                Glide.with(binding.root.context)
                    .load(product.colors[0].images[0])
                    .into(productImage)

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
