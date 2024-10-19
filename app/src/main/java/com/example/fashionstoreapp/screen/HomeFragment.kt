package com.example.fashionstoreapp.screen

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.fashionstoreapp.Contants.LIMIT
import com.example.fashionstoreapp.Contants.PAGE
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.screen.adapter.CategoryAdapter
import com.example.fashionstoreapp.screen.adapter.ProductAdapter
import com.example.fashionstoreapp.screen.adapter.SlideAdapter
import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.databinding.FragmentHomeBinding
import com.example.fashionstoreapp.screen.viewmodel.CategoryViewModel
import com.example.fashionstoreapp.screen.viewmodel.ProductsViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val productsViewModel: ProductsViewModel by lazy {
        ViewModelProvider(requireActivity())[ProductsViewModel::class.java]
    }

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(
            this,
        )[CategoryViewModel::class.java]
    }

    private val controller by lazy {
        findNavController()
    }

    private var listSlide = mutableListOf<String>()
    private var listCategory = mutableListOf<Category>()

    private lateinit var slideAdapter: SlideAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var bestProductAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        productsViewModel.fetchAllProducts(PAGE, LIMIT)
        productsViewModel.fetchProductsBestSeller()
        categoryViewModel.fetchCategoriesList()

        initFakeDate()
        setUpSlideRecycleView()
        setUpCategoryRecycleView()
        setUpProductRecycleView()
        setUpBestProductRecycleView()
        handleBestSellerClick()
        handleBestDiscountClick()
        handleSeeMoreClick()


        return binding.root
    }

    private fun setUpSlideRecycleView() {
        slideAdapter = SlideAdapter(listOf())
        binding.rvProductSlide.adapter = slideAdapter
        binding.rvProductSlide.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        slideAdapter.onItemClick = {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.rvProductSlide)
        slideAdapter.setData(listSlide)
    }

    private fun setUpCategoryRecycleView() {
        categoryAdapter = CategoryAdapter(listOf())
        binding.rvCategory.adapter = categoryAdapter
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter.onItemClick = {
            if (it.id == 0) {
                productsViewModel.fetchAllProducts(PAGE, LIMIT)
            } else {
                productsViewModel.getProductsByCategory(it.id, PAGE, LIMIT)
            }
        }
        categoryAdapter.setData(listCategory)

        categoryViewModel.categories.observe(viewLifecycleOwner) {
            val list = it.toMutableList()
            list.add(0, Category(0, "Tất cả", HashSet()))
            categoryAdapter.setData(list)
        }
    }

    private fun setUpProductRecycleView() {
        productAdapter = ProductAdapter(listOf())
        binding.rvProduct.adapter = productAdapter
        binding.rvProduct.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        productAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }

            controller.navigate(R.id.action_navigationFragment_to_detailFragment, bundle)
        }
        productAdapter.onAddCartClick = {
            Toast.makeText(requireContext(), "Clicked Add Cart", Toast.LENGTH_SHORT).show()
        }
        productsViewModel.homeProducts.observe(viewLifecycleOwner) {
            productAdapter.setData(it)
            if (it.size == LIMIT) {
                binding.seeMoreWrapper.visibility = View.VISIBLE
            } else {
                binding.seeMoreWrapper.visibility = View.GONE
            }
        }
    }

    private fun setUpBestProductRecycleView() {
        bestProductAdapter = ProductAdapter(listOf())
        binding.rvBestProduct.adapter = bestProductAdapter
        binding.rvBestProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        bestProductAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }
            controller.navigate(R.id.action_navigationFragment_to_detailFragment, bundle)
        }
        bestProductAdapter.onAddCartClick = {
            Toast.makeText(requireContext(), "Clicked Add Cart", Toast.LENGTH_SHORT).show()
        }
        productsViewModel.homeProductSort.observe(viewLifecycleOwner) {
            bestProductAdapter.setData(it)
        }
    }

    private fun initFakeDate() {
        listSlide.add("")
        listSlide.add("")
        listSlide.add("")
        listSlide.add("")
    }

    private fun handleBestSellerClick() {
        binding.btnBestSeller.setOnClickListener {
            productsViewModel.fetchProductsBestSeller()
            binding.btnBestSeller.setTypeface(null, Typeface.BOLD)
            binding.btnBestDiscount.setTypeface(null, Typeface.NORMAL)
        }
    }

    private fun handleBestDiscountClick() {
        binding.btnBestDiscount.setOnClickListener {
            productsViewModel.fetchProductsBestDiscount()
            binding.btnBestSeller.setTypeface(null, Typeface.NORMAL)
            binding.btnBestDiscount.setTypeface(null, Typeface.BOLD)
        }
    }

    private fun handleSeeMoreClick() {
        binding.btnSeeMore.setOnClickListener {
            controller.navigate(R.id.action_navigationFragment_to_seeMoreFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(requireContext(), "Home Fragment", Toast.LENGTH_SHORT).show()
    }
}