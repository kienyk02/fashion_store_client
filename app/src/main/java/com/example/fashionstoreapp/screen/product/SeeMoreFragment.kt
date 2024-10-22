package com.example.fashionstoreapp.screen.product

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionstoreapp.Contants.LIMIT
import com.example.fashionstoreapp.Contants.PAGE
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Cart
import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.databinding.FragmentSeeMoreBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.CategoryAdapter
import com.example.fashionstoreapp.screen.adapter.ProductAdapter
import com.example.fashionstoreapp.screen.viewmodel.CartViewModel
import com.example.fashionstoreapp.screen.viewmodel.CategoryViewModel
import com.example.fashionstoreapp.screen.viewmodel.ProductsViewModel

class SeeMoreFragment : Fragment() {
    private lateinit var binding: FragmentSeeMoreBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    private val productsViewModel: ProductsViewModel by lazy {
        ViewModelProvider(this)[ProductsViewModel::class.java]
    }

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(
            this,
        )[CategoryViewModel::class.java]
    }

    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(
            this,
            CartViewModel.CartViewModelFactory(requireActivity().application)
        )[CartViewModel::class.java]
    }

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private var isLoading = false
    private var currPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSeeMoreBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        headerBinding.txtTitle.text = getString(R.string.app_name)

        productsViewModel.fetchAllProducts(PAGE, LIMIT)
        categoryViewModel.fetchCategoriesList()
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
            if (it.id == 0) {
                productsViewModel.fetchAllProducts(PAGE, LIMIT)
            } else {
                productsViewModel.getProductsByCategory(it.id, PAGE, LIMIT)
            }
        }
        categoryViewModel.categories.observe(viewLifecycleOwner) {
            val list = it.toMutableList()
            list.add(0, Category(0, "Tất cả", HashSet()))
            categoryAdapter.setData(list)
        }
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
            addCart(it)
        }
        productsViewModel.seeMoreProducts.observe(viewLifecycleOwner) {
            productAdapter.addData(it)
        }
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
            currPage++
            val categoryId = categoryAdapter.listCategory[categoryAdapter.selectedPosition].id
            if (categoryId == 0) {
                productsViewModel.fetchAllProducts(currPage, LIMIT)
            } else {
                productsViewModel.getProductsByCategory(categoryId, currPage, LIMIT)
            }
            hideLoading()
        }, 1000)
    }

    private fun addCart(product: Product) {
        val cart: Cart = Cart(
            product = Product(id = product.id),
            price = (product.price - product.price * product.discount / 100).toInt(),
            color = product.colors[0],
            size = product.colors[0].sizes[0],
            quantity = 1
        )

        cartViewModel.addCart(cart).observe(viewLifecycleOwner, Observer {
            if (it is Cart) {
                Toast.makeText(
                    requireActivity(),
                    "Sản phẩm đã được thêm vào giỏ hàng",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireActivity(),
                    it.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}