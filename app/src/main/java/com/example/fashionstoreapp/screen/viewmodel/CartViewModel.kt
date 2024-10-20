package com.example.fashionstoreapp.screen.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.fashionstoreapp.data.model.Cart
import com.example.fashionstoreapp.data.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CartViewModel(application: Application) : ViewModel() {
    private val cartRepository: CartRepository = CartRepository(application)
    private val _listCart = MutableLiveData<List<Cart>>()
    var listCart: LiveData<List<Cart>> = _listCart

    init {
        _listCart.value = emptyList()
    }

    fun fetchAllCart() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = cartRepository.getAllCart()
                if (response.isSuccessful) {
                    _listCart.postValue(response.body())
                } else {
                    _listCart.postValue(emptyList())
                }
            } catch (e: Exception) {
                Log.d("errorCart", e.toString())
            }
        }
    }

    fun addCart(cart: Cart) = liveData(Dispatchers.IO) {
        try {
            val response = cartRepository.addCart(cart)
            if (response.isSuccessful) {
                emit(response.body())
            } else {
                emit(response.errorBody().toString())
            }
        } catch (e: Exception) {
            emit(e.toString())
        }
    }

    fun updateCartQuantity(id: Int, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cartRepository.updateCartQuantity(id, quantity)
                fetchAllCart()
            } catch (e: Exception) {
                Log.d("errorCart", e.toString())
            }
        }
    }

    fun deleteCartById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cartRepository.deleteCartById(id)
                fetchAllCart()
            } catch (e: Exception) {
                Log.d("errorCart", e.toString())
            }
        }
    }

    fun deleteCartByUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cartRepository.deleteCartByUser()
                fetchAllCart()
            } catch (e: Exception) {
                Log.d("errorCart", e.toString())
            }
        }
    }

    class CartViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(application) as T
            }
            throw IllegalArgumentException("sss")
        }
    }

}