package com.example.fashionstoreapp.screen.cart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Cart
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.databinding.FragmentCartBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.CartAdapter
import com.example.fashionstoreapp.screen.viewmodel.CartViewModel

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(
            this,
            CartViewModel.CartViewModelFactory(requireActivity().application)
        )[CartViewModel::class.java]
    }

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader

        headerBinding.txtTitle.text = getString(R.string.cart)

        setUpCartAdapter()
        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.btnCheckout.setOnClickListener {
            if (cartViewModel.listCart.value!!.isNotEmpty()) {
                controller.navigate(R.id.action_cartFragment_to_checkoutFragment)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Vui lòng thêm sản phẩm vào giỏ hàng!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    private fun setUpCartAdapter() {
        cartViewModel.fetchAllCart()
        cartAdapter = CartAdapter(listOf())
        binding.rvCart.adapter = cartAdapter
        binding.rvCart.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartViewModel.listCart.observe(viewLifecycleOwner) {
            cartAdapter.setData(it)
            var totalPrice = 0
            for (cart in it) {
                totalPrice += cart.price * cart.quantity
            }
            binding.txtSubtotal.text = formatPrice(totalPrice)
            binding.txtTotal.text = formatPrice(totalPrice)
            onDataLoaded(cartAdapter)
        }
        cartAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putParcelable("product", it.product)
            }
            controller.navigate(R.id.action_cartFragment_to_detailFragment, bundle)
        }
        cartAdapter.onDeleteClick = {
            deleteCartById(it.id!!)
        }
        cartAdapter.onDecreaseClick = {
            if (it.quantity > 1) {
                updateCartQuantity(it.id!!, it.quantity - 1)
            }
        }
        cartAdapter.onIncreaseClick = {
            if (it.quantity < 99) {
                updateCartQuantity(it.id!!, it.quantity + 1)
            }
        }
        cartAdapter.onUpdateClick = { id, quantity ->
            updateCartQuantity(id, quantity)
        }
    }

    private fun updateCartQuantity(id: Int, quantity: Int) {
        cartViewModel.updateCartQuantity(id, quantity)
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun deleteCartById(id: Int) {
        cartViewModel.deleteCartById(id)
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onDataLoaded(cartAdapter: CartAdapter) {
        if (cartAdapter.itemCount > 0) {
            binding.progressBar.visibility = View.GONE
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.progressBar.visibility = View.GONE
        }, 3000)
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }
}