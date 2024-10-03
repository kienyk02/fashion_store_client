package com.example.fashionstoreapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fashionstoreapp.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding

    private val controller by lazy {
        findNavController()
    }

    private var listCart = mutableListOf<Cart>()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)


        initFakeDate()
        setUpCartAdapter()
        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        return binding.root
    }

    private fun setUpCartAdapter() {
        cartAdapter = CartAdapter(listOf())
        binding.rvCart.adapter = cartAdapter
        binding.rvCart.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartAdapter.setData(listCart)
        cartAdapter.onItemClick = {
            Toast.makeText(requireContext(), it.product.name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initFakeDate() {
        listCart.add(Cart(1, Product(1, "Premium Tagerine Shirt", 240000, ""), 2))
        listCart.add(Cart(1, Product(1, "Áo sơ mi nam ngắn thoang mat mac mua he", 240000, ""), 2))
        listCart.add(Cart(1, Product(1, "Premium Tagerine Shirt", 240000, ""), 1))
    }
}