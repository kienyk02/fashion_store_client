package com.example.fashionstoreapp.data.payload

data class SignupRequest(
    val name: String,
    val gender: String,
    val email: String,
    val password: String
)
