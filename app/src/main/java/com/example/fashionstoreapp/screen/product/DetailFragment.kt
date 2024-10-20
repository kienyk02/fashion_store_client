package com.example.fashionstoreapp.screen.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Cart
import com.example.fashionstoreapp.data.model.Color
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.databinding.FragmentDetailBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.ColorSelectAdapter
import com.example.fashionstoreapp.screen.adapter.ImageProductAdapter
import com.example.fashionstoreapp.screen.adapter.SizeAdapter
import com.example.fashionstoreapp.screen.viewmodel.CartViewModel

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(
            this,
            CartViewModel.CartViewModelFactory(requireActivity().application)
        )[CartViewModel::class.java]
    }

    private lateinit var product: Product
    private lateinit var imageAdapter: ImageProductAdapter
    private lateinit var colorAdapter: ColorSelectAdapter
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
        setUpColorRecyclerView()
        setUpSizeRecyclerView()
        handleAddCart()

        return binding.root
    }

    private fun setUpData() {
        product = arguments?.getParcelable("product")!!
        binding.txtTitleProduct.text = product.name
        binding.txtPriceP.text =
            formatPrice((product.price - product.price * product.discount / 100).toInt())
        binding.animeDescriptionTextView.text = product.description
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
        imageAdapter.setData(product.colors[0].images)
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


    private fun setUpColorRecyclerView() {
        colorAdapter = ColorSelectAdapter(listOf())
        binding.rvColor.adapter = colorAdapter
        binding.rvColor.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        colorAdapter.onItemClick = {
            binding.txtColor.text = it.name
            imageAdapter.setData(it.images)
            sizeAdapter.setData(it.sizes)
            binding.rvImageProduct.scrollToPosition(0)
        }
        binding.txtColor.text = product.colors[0].name
        colorAdapter.setData(product.colors)
    }

    private fun setUpSizeRecyclerView() {
        sizeAdapter = SizeAdapter(listOf())
        binding.rvSize.adapter = sizeAdapter
        binding.rvSize.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        sizeAdapter.onItemClick = {
        }
        sizeAdapter.setData(product.colors[0].sizes)
    }


    @SuppressLint("SetTextI18n")
    private fun handleAddCart() {
        binding.btnAddCart.setOnClickListener {
            val cart: Cart = Cart(
                product = Product(id = product.id),
                price = (product.price - product.discount / 100).toInt(),
                color = product.colors[colorAdapter.selectedPosition],
                size = product.colors[colorAdapter.selectedPosition].sizes[sizeAdapter.selectedPosition],
                quantity = binding.tvQuantity.text.toString().toInt()
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

        binding.btnAdd.setOnClickListener {
            var value = binding.tvQuantity.text.toString().toInt()
            if (value < 99) {
                binding.tvQuantity.setText((value + 1).toString())
                binding.txtPriceP.text =
                    formatPrice((product.price - product.price * product.discount / 100).toInt() * (value + 1))
            }
        }
        binding.btnMinus.setOnClickListener {
            var value = binding.tvQuantity.text.toString().toInt()
            if (value > 1) {
                binding.tvQuantity.setText(
                    (value - 1).toString()
                )
                binding.txtPriceP.text =
                    formatPrice((product.price - product.price * product.discount / 100).toInt() * (value - 1))
            }
        }
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + "đ"
    }
}