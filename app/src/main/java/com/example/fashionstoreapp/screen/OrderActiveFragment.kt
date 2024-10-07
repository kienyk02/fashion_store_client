package com.example.fashionstoreapp.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Order
import com.example.fashionstoreapp.databinding.FragmentOrderActiveBinding
import com.example.fashionstoreapp.screen.adapter.OrderAdapter

class OrderActiveFragment : Fragment() {
    private lateinit var binding: FragmentOrderActiveBinding

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
        binding = FragmentOrderActiveBinding.inflate(inflater, container, false)

        initFakeDate()
        setUpOrderRecycleView()

        return binding.root
    }

    private fun setUpOrderRecycleView() {
        orderAdapter = OrderAdapter(listOf())
        binding.rvActive.adapter = orderAdapter
        binding.rvActive.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        orderAdapter.setData(listOrder)

        orderAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putParcelable("order", it)
            controller.navigate(R.id.action_orderHistoryFragment_to_orderDetailFragment, bundle)
        }
    }

    private fun initFakeDate() {
        listOrder.add(Order(1))
        listOrder.add(Order(1))
        listOrder.add(Order(1))
        listOrder.add(Order(1))
    }
}