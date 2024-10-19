package com.example.fashionstoreapp.screen.product

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionstoreapp.Contants.LIMIT
import com.example.fashionstoreapp.Contants.PAGE
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.databinding.FragmentSearchBinding
import com.example.fashionstoreapp.screen.adapter.CategoryAdapter
import com.example.fashionstoreapp.screen.adapter.CategoryFilterAdapter
import com.example.fashionstoreapp.screen.adapter.ProductAdapter
import com.example.fashionstoreapp.screen.viewmodel.CategoryViewModel
import com.example.fashionstoreapp.screen.viewmodel.ProductsViewModel
import com.example.fashionstoreapp.screen.viewmodel.SearchViewModel
import com.google.android.material.slider.RangeSlider

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val searchViewModel by lazy {
        ViewModelProvider(requireActivity())[SearchViewModel::class.java]
    }

    private val productsViewModel: ProductsViewModel by lazy {
        ViewModelProvider(this)[ProductsViewModel::class.java]
    }

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(
            this,
        )[CategoryViewModel::class.java]
    }

    private val controller by lazy {
        findNavController()
    }

    private lateinit var rangeSlider: RangeSlider
    private var fromPrice: Int = 0
    private var toPrice: Int = 2000000
    private var isLoading = false
    private var currPage = 1
    private var isAsc = false
    private var isDesc = false

    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryFilterAdapter
    private val allCategoryId = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        categoryViewModel.fetchCategoriesList()
        categoryAdapter = CategoryFilterAdapter(listOf())
        setUpProductRecycleView()
        setUpLoadMore()
        handleSortPrice()
        searchViewModel.key.observe(viewLifecycleOwner) {
            //reset list product
            productAdapter.setData(listOf())
            //search
            productsViewModel.searchProducts(it, PAGE, LIMIT)
            binding.txtKey.text = it
            currPage = 1
        }
        binding.btnFilter.setOnClickListener {
            createDialogFilter()
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setUpProductRecycleView() {
        productAdapter = ProductAdapter(listOf())
        binding.rvProductResult.adapter = productAdapter
        binding.rvProductResult.layoutManager =
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
        productsViewModel.searchProducts.observe(viewLifecycleOwner) {
            productAdapter.addData(it)
            binding.txtNumResult.text = "${it.size} Sản Phẩm"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createDialogFilter() {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_filter)

        val categoryRecyclerView = dialog.findViewById<RecyclerView>(R.id.rvCategoryPopup)
        categoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        categoryRecyclerView.adapter = categoryAdapter

        categoryViewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.setData(it)
            allCategoryId.clear()
            for (i in it) {
                allCategoryId.add(i.id)
            }
        }

        rangeSlider = dialog.findViewById(R.id.skPrice)

        val startPrice = dialog.findViewById<TextView>(R.id.tvStartPrice)
        val endPrice = dialog.findViewById<TextView>(R.id.tvEndPrice)
        startPrice.text = formatPrice(fromPrice)
        endPrice.text = formatPrice(toPrice)

        initializeSeekbar(startPrice, endPrice)

        dialog.findViewById<Button>(R.id.btnApply).setOnClickListener {
            fetchProductSearchWithFilter()
            productAdapter.setData(listOf())
            dialog.dismiss()
        }

        dialog.findViewById<TextView>(R.id.tvReset).setOnClickListener {
            categoryAdapter.listCategoryId = mutableListOf()
            categoryAdapter.notifyDataSetChanged()
            fromPrice = 0
            toPrice = 2000000
            startPrice.text = formatPrice(fromPrice)
            endPrice.text = formatPrice(toPrice)
            rangeSlider.values = listOf(fromPrice / 20000f, toPrice / 20000f)
        }

        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutParams = dialog.window?.attributes
        layoutParams?.gravity = Gravity.BOTTOM
        dialog.window?.attributes = layoutParams

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.show()
    }

    private fun fetchProductSearchWithFilter() {
        var sort = ""
        if (isAsc) {
            sort = "ASC"
        } else if (isDesc) {
            sort = "DESC"
        }
        if (categoryAdapter.listCategoryId.isEmpty()) {
            productsViewModel.fetchProductSearchWithFilter(
                allCategoryId,
                binding.txtKey.text.toString(),
                fromPrice,
                toPrice,
                sort,
                currPage,
                LIMIT
            )
        } else {
            productsViewModel.fetchProductSearchWithFilter(
                categoryAdapter.listCategoryId,
                binding.txtKey.text.toString(),
                fromPrice,
                toPrice,
                sort,
                currPage,
                LIMIT
            )
        }
    }

    private fun initializeSeekbar(startPrice: TextView, endPrice: TextView) {
        rangeSlider.values = listOf(fromPrice / 20000f, toPrice / 20000f)
        rangeSlider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            fromPrice = calculatePrice(values[0].toInt())
            toPrice = calculatePrice(values[1].toInt())
            startPrice.text = formatPrice(fromPrice)
            endPrice.text = formatPrice(toPrice)
        }
    }

    private fun handleSortPrice() {
        binding.btnPriceASC.setOnClickListener {
            productAdapter.sortProductsByPriceAscending()
            isDesc = false
            isAsc = !isAsc
            changeColorButtonSort()
        }
        binding.btnPriceDESC.setOnClickListener {
            productAdapter.sortProductsByPriceDescending()
            isAsc = false
            isDesc = !isDesc
            changeColorButtonSort()
        }
    }

    private fun changeColorButtonSort() {
        val selectColor = ContextCompat.getColor(
            requireContext(),
            R.color.txt_color2
        )
        val unSelectColor = ContextCompat.getColor(
            requireContext(),
            R.color.gray
        )
        if (isAsc) {
            binding.btnPriceASC.setColorFilter(selectColor)
        } else {
            binding.btnPriceASC.setColorFilter(unSelectColor)
        }
        if (isDesc) {
            binding.btnPriceDESC.setColorFilter(
                selectColor
            )
        } else {
            binding.btnPriceDESC.setColorFilter(unSelectColor)
        }
    }

    private fun setUpLoadMore() {
        binding.rvProductResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && lastVisibleItemPosition == totalItemCount - 1 && totalItemCount == LIMIT) {
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
            fetchProductSearchWithFilter()
            hideLoading()
        }, 1000)
    }

    private fun calculatePrice(progress: Int): Int {
        return progress * 20000
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }
}