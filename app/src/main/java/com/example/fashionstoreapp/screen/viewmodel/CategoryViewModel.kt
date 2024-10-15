package com.example.fashionstoreapp.screen.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.data.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val categoryRepository = CategoryRepository()

    private val _categories = MutableLiveData<List<Category>>()

    val categories: LiveData<List<Category>> = _categories

    fun fetchCategoriesList() = viewModelScope.launch(Dispatchers.IO) {
        val response = categoryRepository.getCategoriesList()
        if (response.isSuccessful) {
            _categories.postValue(response.body())
        } else {
            _categories.postValue(emptyList())
        }
    }


}