package com.example.fashionstoreapp.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.databinding.FragmentOrderHistoryBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.MyPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OrderHistoryFragment : Fragment() {
    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        headerBinding.txtTitle.text = getString(R.string.myorders)

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        val fragments: List<Fragment> =
            listOf(OrderActiveFragment(), OrderCompleteFragment(), RatingFragment())
        val pagerAdapter = MyPagerAdapter(requireActivity(), fragments)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Active"
                1 -> tab.text = "Completed"
                2 -> tab.text = "Rating"
            }
        }.attach()

        return binding.root
    }
}