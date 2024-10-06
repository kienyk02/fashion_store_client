package com.example.fashionstoreapp.screen

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.databinding.FragmentDetailBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.ImageProductAdapter
import com.example.fashionstoreapp.screen.adapter.SizeAdapter

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    private lateinit var product: Product
    private lateinit var imageAdapter: ImageProductAdapter
    private lateinit var sizeAdapter: SizeAdapter
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        headerBinding.txtTitle.text = getString(R.string.detail)

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        setUpData()
        handleSaveFavorite()
        setUpImageProductRecyclerView()
        handleSeeMoreContent()
        setUpSizeRecyclerView()

        return binding.root
    }

    private fun setUpData() {
        product = arguments?.getParcelable("product")!!
        binding.txtTitleProduct.text = product.name
//        binding.animeDescriptionTextView.text = product.description
        binding.txtPriceP.text = formatPrice(product.price)
    }

    private fun setUpImageProductRecyclerView() {
        imageAdapter = ImageProductAdapter(listOf())
        binding.rvImageProduct.adapter = imageAdapter
        binding.rvImageProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.rvImageProduct)
        imageAdapter.onItemClick = {
        }
        imageAdapter.setData(listOf("", "", ""))
    }

    private fun handleSaveFavorite() {
        headerBinding.btnSaveFavorite.visibility = View.VISIBLE
        headerBinding.btnSaveFavorite.setOnClickListener {
            if (headerBinding.btnSaveFavorite.drawable.constantState == ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.save_favorite
                )?.constantState
            ) {
                headerBinding.btnSaveFavorite.setImageResource(R.drawable.not_save_favorite)
            } else {
                headerBinding.btnSaveFavorite.setImageResource(R.drawable.save_favorite)
            }
        }
    }

    private fun handleSeeMoreContent() {
        val seeMoreListener =
            View.OnClickListener {
                if (isExpanded) {
                    // Collapse the description
                    binding.animeDescriptionTextView.maxLines = 3
                    binding.animeDescriptionTextView.ellipsize = TextUtils.TruncateAt.END
                    binding.seeMore.text = getString(R.string.see_more)
                    binding.imgDown.setImageResource(R.drawable.angle_down)
                } else {
                    // Expand the description
                    binding.animeDescriptionTextView.maxLines = Int.MAX_VALUE
                    binding.animeDescriptionTextView.ellipsize = null
                    binding.seeMore.text = getString(R.string.see_less)
                    binding.imgDown.setImageResource(R.drawable.angle_up)
                }
                isExpanded = !isExpanded
            }
        binding.seeMore.setOnClickListener(seeMoreListener)
    }

    private fun setUpSizeRecyclerView() {
        sizeAdapter = SizeAdapter(listOf())
        binding.rvSize.adapter = sizeAdapter
        binding.rvSize.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        sizeAdapter.onItemClick = {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        sizeAdapter.setData(listOf("S", "M", "L", "XL", "XXL"))
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + "đ"
    }
}