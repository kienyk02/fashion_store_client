package com.example.fashionstoreapp.screen.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fashionstoreapp.data.payload.MessageResponse
import com.example.fashionstoreapp.data.payload.SigninRequest
import com.example.fashionstoreapp.data.payload.SignupRequest
import com.example.fashionstoreapp.data.payload.TokenResponse
import com.example.fashionstoreapp.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AuthViewModel(application: Application) : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository(application)

    private val _signInResponse = MutableLiveData<TokenResponse?>()
    val signInResponse: LiveData<TokenResponse?> = _signInResponse
    private val _signUpResponse = MutableLiveData<MessageResponse?>()
    val signUpResponse: LiveData<MessageResponse?> = _signUpResponse

    fun signIn(request: SigninRequest) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = authRepository.signIn(request)
            if (response.isSuccessful) {
                _signInResponse.postValue(response.body())
            } else {
                _signInResponse.postValue(null)
            }
        } catch (e: Exception) {
            Log.d("authError", e.toString())
        }
    }

    fun signUp(request: SignupRequest) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = authRepository.signUp(request)
            if (response.isSuccessful) {
                _signUpResponse.postValue(response.body())
            } else {
                _signUpResponse.postValue(null)
            }
        } catch (e: Exception) {
            Log.d("authError", e.toString())
        }
    }


    class AuthViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(application) as T
            }
            throw IllegalArgumentException("sss")
        }
    }

}