package com.example.fashionstoreapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
class Product(
    var id: Int,
    var name: String = "",
    var price: Int,
    var images: List<String>,
    var description: String = "",
    var stockQuantity: Int,
    var isAvailable: Boolean,
    var size: List<String>,
    var color: List<String>,
    var material: String = "",
    var style: String = "",
    var gender: String = "",
    var weight: Double,
    var height: Double,
    var width: Double,
    var depth: Double,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var tags: List<String>,
    var discount: Double,
    var categories: List<Category> = listOf()
) : Parcelable