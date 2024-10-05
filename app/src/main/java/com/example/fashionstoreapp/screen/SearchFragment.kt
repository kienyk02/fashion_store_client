package com.example.fashionstoreapp.screen

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.databinding.FragmentSearchBinding
import com.example.fashionstoreapp.screen.adapter.CategoryAdapter
import com.example.fashionstoreapp.screen.adapter.ProductAdapter
import com.example.fashionstoreapp.screen.viewmodel.SearchViewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val searchViewModel by lazy {
        ViewModelProvider(requireActivity())[SearchViewModel::class.java]
    }

    private val controller by lazy {
        findNavController()
    }

    private lateinit var seekBar: SeekBar
    private var currentPrice: Int = 0
    private var listProduct = mutableListOf<Product>()
    private var listCategory = mutableListOf<Category>()

    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        initFakeDate()
        setUpProductRecycleView()
        searchViewModel.key.observe(viewLifecycleOwner) {
            searchData(it)
        }
        binding.btnFilter.setOnClickListener {
            createDialogFilter()
        }

        return binding.root
    }

    private fun setUpProductRecycleView() {
        productAdapter = ProductAdapter(listOf())
        binding.rvProductResult.adapter = productAdapter
        binding.rvProductResult.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        productAdapter.onItemClick = {
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
        }
        productAdapter.onAddCartClick = {
            Toast.makeText(requireContext(), "Clicked Add Cart", Toast.LENGTH_SHORT).show()
        }
        productAdapter.setData(listProduct)
    }

    private fun searchData(key: String) {
        binding.txtKey.text = key
        Toast.makeText(requireContext(), key, Toast.LENGTH_SHORT).show()
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

    private fun createDialogFilter() {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_filter)

        val categoryRecyclerView = dialog.findViewById<RecyclerView>(R.id.rvCategoryPopup)
        categoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        val categoryAdapter = CategoryAdapter(listCategory)
        categoryRecyclerView.adapter = categoryAdapter

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