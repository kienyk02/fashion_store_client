package com.example.fashionstoreapp.screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.databinding.FragmentSeeMoreBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.CategoryAdapter
import com.example.fashionstoreapp.screen.adapter.ProductAdapter

class SeeMoreFragment : Fragment() {
    private lateinit var binding: FragmentSeeMoreBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }
    private var listProduct = mutableListOf<Product>()
    private var listCategory = mutableListOf<Category>()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSeeMoreBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        headerBinding.txtTitle.text = getString(R.string.app_name)

        initFakeDate()
        setUpCategoryRecycleView()
        setUpProductRecycleView()
        setUpLoadMore()

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }
        return binding.root
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
        binding.rvListProduct.adapter = productAdapter
        binding.rvListProduct.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        productAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }
            controller.navigate(R.id.action_seeMoreFragment_to_detailFragment, bundle)
        }
        productAdapter.onAddCartClick = {
            Toast.makeText(requireContext(), "Clicked Add Cart", Toast.LENGTH_SHORT).show()
        }
        productAdapter.setData(listProduct)
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
    }

    private fun setUpLoadMore() {
        binding.rvListProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && lastVisibleItemPosition == totalItemCount - 1) {
                    binding.pbLoading.visibility = View.VISIBLE
                    isLoading = true
                    loadMoreItems()
                    showLoading()

                }
            }
        })
    }

    private fun showLoading() {
        binding.pbLoading.visibility = View.VISIBLE
        isLoading = true
    }

    // Ẩn ProgressBar sau khi load xong dữ liệu
    private fun hideLoading() {
        binding.pbLoading.visibility = View.GONE
        isLoading = false
    }

    private fun loadMoreItems() {
        Handler(Looper.getMainLooper()).postDelayed({

            hideLoading()
        }, 1000)
    }
}