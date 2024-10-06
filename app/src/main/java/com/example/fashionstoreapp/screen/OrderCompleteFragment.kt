package com.example.fashionstoreapp.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fashionstoreapp.data.model.Order
import com.example.fashionstoreapp.databinding.FragmentOrderCompleteBinding
import com.example.fashionstoreapp.screen.adapter.OrderAdapter

class OrderCompleteFragment : Fragment() {
    private lateinit var binding: FragmentOrderCompleteBinding

    private val controller by lazy {
        findNavController()
    }

    private val listOrder = mutableListOf<Order>()
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderCompleteBinding.inflate(inflater, container, false)

        initFakeDate()
        setUpOrderRecycleView()

        return binding.root
    }

    private fun setUpOrderRecycleView() {
        orderAdapter = OrderAdapter(listOf())
        binding.rvComplete.adapter = orderAdapter
        binding.rvComplete.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        orderAdapter.setData(listOrder)

        orderAdapter.onItemClick = {
            Toast.makeText(requireContext(), "Click item", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initFakeDate() {
        listOrder.add(Order(1))
        listOrder.add(Order(1))
        listOrder.add(Order(1))
        listOrder.add(Order(1))
    }
}