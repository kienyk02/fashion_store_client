package com.example.fashionstoreapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Product(var id: Int, var name: String, var price: Int, var image: String) : Parcelable