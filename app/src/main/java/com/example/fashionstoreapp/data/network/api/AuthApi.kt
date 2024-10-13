package com.example.fashionstoreapp.data.network.api

import com.example.fashionstoreapp.data.payload.MessageResponse
import com.example.fashionstoreapp.data.payload.SigninRequest
import com.example.fashionstoreapp.data.payload.SignupRequest
import com.example.fashionstoreapp.data.payload.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/auth/signup")
    suspend fun signUp(@Body request: SignupRequest): Response<MessageResponse>

    @POST("api/v1/auth/signin")
    suspend fun signIn(@Body loginRequest: SigninRequest): Response<TokenResponse>

}