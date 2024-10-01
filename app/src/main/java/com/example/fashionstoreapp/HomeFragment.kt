package com.example.fashionstoreapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fashionstoreapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val controller by lazy {
        findNavController()
    }

    private var listProduct = mutableListOf<Product>()

    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        initFakeDate()
        productAdapter = ProductAdapter(listProduct)
        binding.rvProduct.adapter = productAdapter
        binding.rvProduct.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    private fun initFakeDate() {
        listProduct.add(Product(1, "Áo sơ mi nam ngắn thoang mat mac mua he", 240000,""))
        listProduct.add(Product(1, "Tagerine Shirt", 199000,""))
        listProduct.add(Product(1, "Quần tây nam Fashion sieu dep vip pro", 300000,""))
        listProduct.add(Product(1, "Tagerine Shirt", 290000,""))
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(requireContext(), "Home Fragment", Toast.LENGTH_SHORT).show()
    }
}