package com.example.fashionstoreapp.screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {
    private val _key = MutableLiveData<String>()
    val key: LiveData<String> = _key

    fun updateKey(newKey: String) {
        _key.value = newKey
    }
}