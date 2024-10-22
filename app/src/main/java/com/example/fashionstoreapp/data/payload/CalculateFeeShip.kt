package com.example.fashionstoreapp.data.payload

class CalculateFeeShip(
    var service_type_id: Int = 0,
    var from_district_id: Int = 0,
    var from_ward_code: String = "",
    var to_district_id: Int = 0,
    var to_ward_code: String = "",
    var weight: Int = 0,
    var length: Int = 0,
    var width: Int = 0,
    var height: Int = 0,
    var items: List<Item> = listOf()

)