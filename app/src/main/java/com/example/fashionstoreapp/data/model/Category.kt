package com.example.fashionstoreapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var id: Int,
    var categoryName: String,
    var subCategories: Set<Category>? = null
) :
    Parcelable