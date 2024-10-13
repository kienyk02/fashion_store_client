package com.example.fashionstoreapp.data.repository

import android.app.Application
import com.example.fashionstoreapp.data.localdatebase.AppDatabase
import com.example.fashionstoreapp.data.network.ApiConfig
import com.example.fashionstoreapp.data.network.api.AuthApi
import com.example.fashionstoreapp.data.payload.SigninRequest
import com.example.fashionstoreapp.data.payload.SignupRequest


class AuthRepository(app: Application) {

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(app)
    }

    private val authApi: AuthApi = ApiConfig.retrofitWithoutJwt.create(AuthApi::class.java)

    suspend fun signIn(request: SigninRequest) = authApi.signIn(request)


    suspend fun signUp(request: SignupRequest) = authApi.signUp(request)

}