package com.example.fashionstoreapp.data.model;

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Size(
    var id: Int,
    var name: String = "",
    var quantity: Int
) : Parcelable
