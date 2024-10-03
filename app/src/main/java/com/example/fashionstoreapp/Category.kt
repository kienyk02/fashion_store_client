package com.example.fashionstoreapp

data class Category(var id: Int, var name: String, var parentCategory: Category? = null)