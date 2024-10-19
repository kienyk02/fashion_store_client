package com.example.fashionstoreapp.screen.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel() : ViewModel() {

    private val productRepository = ProductRepository();

    // Home
    private val _allProducts = MutableLiveData<List<Product>>()
    private val _suggestProducts = MutableLiveData<List<Product>>()

    // Display
    private val _searchProducts = MutableLiveData<List<Product>>()
    private val _homeProducts = MutableLiveData<List<Product>>()
    private val _homeProductSort = MutableLiveData<List<Product>>()
    private val _seeMoreProducts = MutableLiveData<List<Product>>()

    // Save data in these
    val suggestProducts: LiveData<List<Product>> = _suggestProducts
    val allProducts: LiveData<List<Product>> = _allProducts
    val searchProducts: LiveData<List<Product>> = _searchProducts
    val homeProducts: LiveData<List<Product>> = _homeProducts
    val homeProductSort: LiveData<List<Product>> = _homeProductSort
    val seeMoreProducts: LiveData<List<Product>> = _seeMoreProducts


    fun fetchAllProducts(page: Int, limit: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = productRepository.getProducts(page, limit)
            if (response.isSuccessful) {
                _allProducts.postValue(response.body())
                _homeProducts.postValue(response.body())
                _seeMoreProducts.postValue(response.body())
            } else {
                _allProducts.postValue(emptyList())
                _homeProducts.postValue(emptyList())
                _seeMoreProducts.postValue(emptyList())
            }
        } catch (e: Exception) {
            Log.d("productError", e.toString())
        }
    }

    fun searchProducts(keyword: String, page: Int, limit: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = productRepository.searchProducts(keyword, page, limit)
                if (response.isSuccessful) {
                    _searchProducts.postValue(response.body())
                } else {
                    _searchProducts.postValue(emptyList())
                }
            } catch (e: Exception) {
                Log.d("productError", e.toString())
            }
        }

    fun getProductsByCategory(id: Int, page: Int, limit: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = productRepository.getProductsByCategory(id, page, limit)
                if (response.isSuccessful) {
                    _homeProducts.postValue(response.body())
                    _seeMoreProducts.postValue(response.body())
                } else {
                    _homeProducts.postValue(emptyList())
                    _seeMoreProducts.postValue(emptyList())
                }
            } catch (e: Exception) {
                Log.d("productError", e.toString())
            }
        }

    fun fetchProductsBestSeller() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = productRepository.getProductsBestSeller()
            if (response.isSuccessful) {
                _homeProductSort.postValue(response.body())
            } else {
                _homeProductSort.postValue(emptyList())
            }
        } catch (e: Exception) {
            Log.d("productError", e.toString())
        }
    }

    fun fetchProductsBestDiscount() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = productRepository.getProductsBestDiscount()
            if (response.isSuccessful) {
                _homeProductSort.postValue(response.body())
            } else {
                _homeProductSort.postValue(emptyList())
            }
        } catch (e: Exception) {
            Log.d("productError", e.toString())
        }
    }

    fun fetchProductSearchWithFilter(
        categoryIds: List<Int>,
        keyword: String,
        fromPrice: Int,
        toPrice: Int,
        sort: String,
        page: Int,
        limit: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = productRepository.getProductSearchWithFilter(
                categoryIds,
                keyword,
                fromPrice,
                toPrice,
                sort,
                page,
                limit
            )
            if (response.isSuccessful) {
                _searchProducts.postValue(response.body())
            } else {
                _searchProducts.postValue(emptyList())
            }
        } catch (e: Exception) {
            Log.d("productError", e.toString())
        }
    }

}