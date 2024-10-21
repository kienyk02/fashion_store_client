package com.example.fashionstoreapp.data.model

data class Province(val provinceID: Int, val provinceName: String) {
    override fun toString(): String {
        return provinceName
    }
}

data class District(val districtID: Int, val districtName: String) {
    override fun toString(): String {
        return districtName
    }
}

data class Ward(val wardID: String, val wardName: String) {
    override fun toString(): String {
        return wardName
    }
}