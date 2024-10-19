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
import com.example.fashionstoreapp.screen.adapter.ProductAdapter
import com.example.fashionstoreapp.screen.viewmodel.CategoryViewModel
import com.example.fashionstoreapp.screen.viewmodel.ProductsViewModel
import com.example.fashionstoreapp.screen.viewmodel.SearchViewModel

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

    private lateinit var seekBar: SeekBar
    private var currentPrice: Int = 0
    private var isLoading = false
    private var currPage = 1

    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        categoryViewModel.fetchCategoriesList()
        setUpProductRecycleView()
        setUpLoadMore()
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

    private fun createDialogFilter() {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_filter)

        val categoryRecyclerView = dialog.findViewById<RecyclerView>(R.id.rvCategoryPopup)
        categoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        val categoryAdapter = CategoryAdapter(listOf())
        categoryRecyclerView.adapter = categoryAdapter

        categoryViewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.setData(it)
        }

        seekBar = dialog.findViewById(R.id.skPrice)

        val startPrice = dialog.findViewById<TextView>(R.id.tvStartPrice)
        val endPrice = dialog.findViewById<TextView>(R.id.tvEndPrice)

        initializeSeekbar(startPrice, endPrice)

        dialog.findViewById<Button>(R.id.btnApply).setOnClickListener {
            dialog.dismiss()
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
            productsViewModel.searchProducts(binding.txtKey.text.toString(), currPage, LIMIT)
            hideLoading()
        }, 1000)
    }

    private fun initializeSeekbar(startPrice: TextView, endPrice: TextView) {

        seekBar.max = 100
        currentPrice = 2000000

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                currentPrice = calculatePrice(progress)

                startPrice.text = formatPrice(currentPrice)
                endPrice.text = formatPrice(2000000)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private fun calculatePrice(progress: Int): Int {
        return progress * 20000
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }
}