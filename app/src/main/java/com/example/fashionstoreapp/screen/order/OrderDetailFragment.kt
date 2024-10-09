package com.example.fashionstoreapp.screen.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.ItemOrder
import com.example.fashionstoreapp.data.model.Order
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.databinding.FragmentOrderDetailBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.ItemOrderAdapter

class OrderDetailFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    private val listItem = mutableListOf<ItemOrder>()
    private lateinit var itemOrderAdapter: ItemOrderAdapter
    private lateinit var order: Order

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        headerBinding.txtTitle.text = getString(R.string.order_detail)

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        setUpData()
        initFakeData()
        setUpItemOrderRecycleView()
        setUpStageShipping()
        return binding.root
    }

    private fun setUpData() {
        order = arguments?.getParcelable("order")!!
    }

    private fun setUpItemOrderRecycleView() {
        itemOrderAdapter = ItemOrderAdapter(listOf())
        binding.rvItemOrder.adapter = itemOrderAdapter
        binding.rvItemOrder.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        itemOrderAdapter.setData(listItem)
    }


    private fun setUpStageShipping() {
        binding.stage1.circleIcon.setImageResource(R.drawable.circle_complete)
        binding.stage1.line.setBackgroundResource(R.drawable.line_complete)

        binding.stage2.circleIcon.setImageResource(R.drawable.circle_complete)
        binding.stage3.line.setBackgroundResource(R.drawable.line_complete)

        binding.stage3.line.visibility = View.GONE
    }

    private fun initFakeData() {
        listItem.add(ItemOrder(1, Product(1, "test", 1, ""), 10))
        listItem.add(ItemOrder(1, Product(1, "test", 1, ""), 10))
        listItem.add(ItemOrder(1, Product(1, "test", 1, ""), 10))
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }
}