package com.example.fashionstoreapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
class Product(
    var id: Int,
    var name: String = "",
    var description: String = "",
    var price: Int = 0,
    var sales: Int = 0,
    var colors: List<Color> = listOf(),
    var material: String = "",
    var style: String = "",
    var gender: String = "",
    var weight: Double = 0.0,
    var height: Double = 0.0,
    var width: Double = 0.0,
    var depth: Double = 0.0,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var tags: List<String> = listOf(),
    var discount: Double = 0.0,
    var categories: List<Category> = listOf()
) : Parcelable