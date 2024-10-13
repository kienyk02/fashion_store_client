package com.example.fashionstoreapp.data.repository

import android.app.Application
import com.example.fashionstoreapp.data.localdatebase.AppDatabase
import com.example.fashionstoreapp.data.network.ApiConfig
import com.example.fashionstoreapp.data.network.api.UserApi
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository(app: Application) {

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(app)
    }

    private val userApi: UserApi = ApiConfig.retrofit.create(UserApi::class.java)

    suspend fun getUserInfo() = userApi.getUserInfo()

    suspend fun updateUserInfo(
        name: RequestBody,
        gender: RequestBody,
        phoneNumber: RequestBody,
        avatarImage: MultipartBody.Part
    ) = userApi.updateUserInfo(name, gender, phoneNumber, avatarImage)

    suspend fun updateAddress(address: RequestBody) = userApi.updateAddress(address)

}