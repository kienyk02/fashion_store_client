package com.example.fashionstoreapp.data.repository

import com.example.fashionstoreapp.data.model.Address
import com.example.fashionstoreapp.data.network.ApiConfig
import com.example.fashionstoreapp.data.network.api.AddressApi
import com.example.fashionstoreapp.data.network.api.AddressSubApi

class AddressRepository {
    private val addressApi: AddressApi = ApiConfig.retrofit.create(AddressApi::class.java)
    private val addressSubApi: AddressSubApi =
        ApiConfig.retrofitAddress.create(AddressSubApi::class.java)

    suspend fun getAllAddress() = addressApi.getAllAddress()

    suspend fun saveAddress(address: Address) = addressApi.saveAddress(address)

    suspend fun deleteAddressById(id: Int) = addressApi.deleteAddressById(id)

    suspend fun getAllProvince() = addressSubApi.getAllProvince()

    suspend fun getAllDistrictByProvince(provinceId: Int) =
        addressSubApi.getAllDistrictByProvince(provinceId)

    suspend fun getAllWardByDistrict(districtId: Int) =
        addressSubApi.getAllWardByDistrict(districtId)

}