package com.example.fashionstoreapp.screen

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.fashionstoreapp.screen.adapter.CategoryAdapter
import com.example.fashionstoreapp.screen.adapter.ProductAdapter
import com.example.fashionstoreapp.screen.adapter.SlideAdapter
import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val controller by lazy {
        findNavController()
    }

    private var listSlide = mutableListOf<String>()
    private var listProduct = mutableListOf<Product>()
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

        initFakeDate()
        setUpSlideRecycleView()
        setUpCategoryRecycleView()
        setUpProductRecycleView()
        setUpBestProductRecycleView()
        handleBestSellerClick()
        handleBestDiscountClick()


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
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
        }
        categoryAdapter.setData(listCategory)
    }

    private fun setUpProductRecycleView() {
        productAdapter = ProductAdapter(listOf())
        binding.rvProduct.adapter = productAdapter
        binding.rvProduct.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        productAdapter.onItemClick = {
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
        }
        productAdapter.onAddCartClick = {
            Toast.makeText(requireContext(), "Clicked Add Cart", Toast.LENGTH_SHORT).show()
        }
        productAdapter.setData(listProduct)
    }

    private fun setUpBestProductRecycleView() {
        bestProductAdapter = ProductAdapter(listOf())
        binding.rvBestProduct.adapter = bestProductAdapter
        binding.rvBestProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        bestProductAdapter.onItemClick = {
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
        }
        bestProductAdapter.onAddCartClick = {
            Toast.makeText(requireContext(), "Clicked Add Cart", Toast.LENGTH_SHORT).show()
        }
        bestProductAdapter.setData(listProduct)
    }

    private fun initFakeDate() {
        listProduct.add(Product(1, "Áo sơ mi nam ngắn thoang mat mac mua he", 240000, ""))
        listProduct.add(Product(1, "Tagerine Shirt", 199000, ""))
        listProduct.add(Product(1, "Quần tây nam Fashion sieu dep vip pro", 300000, ""))
        listProduct.add(Product(1, "Tagerine Shirt", 290000, ""))
        listProduct.add(Product(1, "Áo sơ mi nam ngắn thoang mat mac mua he", 240000, ""))
        listProduct.add(Product(1, "Tagerine Shirt", 199000, ""))
        listProduct.add(Product(1, "Quần tây nam Fashion sieu dep vip pro", 300000, ""))
        listProduct.add(Product(1, "Tagerine Shirt", 290000, ""))

        listCategory.add(Category(1, "All"))
        listCategory.add(Category(1, "Áo nam"))
        listCategory.add(Category(1, "Quần nam"))
        listCategory.add(Category(1, "Mũ nam"))
        listCategory.add(Category(1, "Phụ kiện"))

        listSlide.add("")
        listSlide.add("")
        listSlide.add("")
        listSlide.add("")
    }

    private fun handleBestSellerClick() {
        binding.btnBestSeller.setOnClickListener {
            binding.btnBestSeller.setTypeface(null, Typeface.BOLD)
            binding.btnBestDiscount.setTypeface(null, Typeface.NORMAL)
        }
    }

    private fun handleBestDiscountClick() {
        binding.btnBestDiscount.setOnClickListener {
            binding.btnBestSeller.setTypeface(null, Typeface.NORMAL)
            binding.btnBestDiscount.setTypeface(null, Typeface.BOLD)
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(requireContext(), "Home Fragment", Toast.LENGTH_SHORT).show()
    }

}