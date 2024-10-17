package com.example.fashionstoreapp.screen.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fashionstoreapp.data.model.User
import com.example.fashionstoreapp.data.payload.MessageResponse
import com.example.fashionstoreapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.IllegalArgumentException

class UserViewModel(application: Application) : ViewModel() {

    private val userRepository: UserRepository = UserRepository(application)

    private val _user = MutableLiveData<User>()
    var user: LiveData<User> = _user

    private val _responseUpdateUser = MutableLiveData<MessageResponse>()
    var responseUpdateUser: LiveData<MessageResponse> = _responseUpdateUser
    private val _responseUpdateAddress = MutableLiveData<MessageResponse>()
    var responseUpdateAddress: LiveData<MessageResponse> = _responseUpdateAddress

    fun getUserInfo() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = userRepository.getUserInfo()
            if (response.isSuccessful) {
                _user.postValue(response.body())
            } else {
                _user.postValue(User(0))
            }
        } catch (e: Exception) {
            Log.d("userError", e.toString())
        }
    }

    fun updateUserInfo(
        name: RequestBody,
        gender: RequestBody,
        phoneNumber: RequestBody,
        avatarImage: MultipartBody.Part
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = userRepository.updateUserInfo(name, gender, phoneNumber, avatarImage)
            if (response.isSuccessful) {
                _responseUpdateUser.postValue(response.body())
            } else {
                _responseUpdateUser.postValue(MessageResponse("Update Fail!"))
            }
        } catch (e: Exception) {
            Log.d("userError", e.toString())
        }
    }


    fun updateAddress(address: RequestBody) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val reponse = userRepository.updateAddress(address)
            if (reponse.isSuccessful) {
                _responseUpdateAddress.postValue(reponse.body())
            } else {
                _responseUpdateAddress.postValue(MessageResponse("Update Fail!"))
            }
        } catch (e: Exception) {
            Log.d("userError", e.toString())
        }
    }


    class UserViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(application) as T
            }
            throw IllegalArgumentException("sss")
        }
    }

}