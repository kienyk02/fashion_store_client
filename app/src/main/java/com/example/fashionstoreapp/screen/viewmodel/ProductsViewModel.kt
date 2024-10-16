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

    // Save data in these
    val suggestProducts: LiveData<List<Product>> = _suggestProducts
    val allProducts: LiveData<List<Product>> = _allProducts
    val searchProducts: LiveData<List<Product>> = _searchProducts
    val homeProducts: LiveData<List<Product>> = _homeProducts


    fun fetchAllProducts() = viewModelScope.launch(Dispatchers.IO) {
        val response = productRepository.getProducts()
        if (response.isSuccessful) {
            _allProducts.postValue(response.body())
            _homeProducts.postValue(response.body())
        } else {
            _allProducts.postValue(emptyList())
            _homeProducts.postValue(emptyList())
        }
    }

    fun searchProducts(keyword: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = productRepository.searchProducts(keyword)
        if (response.isSuccessful) {
            _searchProducts.postValue(response.body())
        } else {
            _searchProducts.postValue(emptyList())
        }
    }

    fun getProductsByCategory(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        val response = productRepository.getProductsByCategory(id)
        if (response.isSuccessful) {
            _homeProducts.postValue(response.body())
        } else {
            _homeProducts.postValue(emptyList())
        }
    }

}