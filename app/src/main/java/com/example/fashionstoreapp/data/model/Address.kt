package com.example.fashionstoreapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Address(
    var id: Int? = null,
    var provinceId: Int = 0,
    var provinceName: String = "",
    var districtId: Int = 0,
    var districtName: String = "",
    var wardId: String = "",
    var wardName: String = "",
    var address: String = ""
) : Parcelable
