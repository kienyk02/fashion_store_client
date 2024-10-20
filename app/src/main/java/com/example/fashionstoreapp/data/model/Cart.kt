package com.example.fashionstoreapp.data.model

data class Cart(
    var id: Int? = null,
    var product: Product,
    var price: Int = 0,
    var color: Color,
    var size: Size,
    var quantity: Int
)