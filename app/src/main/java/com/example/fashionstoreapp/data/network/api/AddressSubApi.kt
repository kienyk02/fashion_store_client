package com.example.fashionstoreapp.data.network.api

import com.google.android.gms.common.api.Response
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AddressSubApi {

    @GET("master-data/province")
    suspend fun getAllProvince(): JsonObject

    @GET("master-data/district")
    suspend fun getAllDistrictByProvince(@Query("province_id") provinceId: Int): JsonObject

    @GET("master-data/ward")
    suspend fun getAllWardByDistrict(@Query("district_id") districtId: Int): JsonObject
}