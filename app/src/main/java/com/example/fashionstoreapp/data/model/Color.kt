package com.example.fashionstoreapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Color(
    var id: Int,
    var name: String = "",
    var images: List<String>,
    var sizes: List<Size>
) : Parcelable