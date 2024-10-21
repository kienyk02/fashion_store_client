package com.example.fashionstoreapp.data.model

data class User(
    val id: Int,
    val name: String? = null,
    val phoneNumber: String? = null,
    val gender: String? = null,
    val avatarImage: String? = null
)