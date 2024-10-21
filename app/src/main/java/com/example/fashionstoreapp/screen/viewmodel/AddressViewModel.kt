package com.example.fashionstoreapp.screen.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fashionstoreapp.data.model.Address
import com.example.fashionstoreapp.data.model.District
import com.example.fashionstoreapp.data.model.Province
import com.example.fashionstoreapp.data.model.Ward
import com.example.fashionstoreapp.data.repository.AddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class AddressViewModel : ViewModel() {
    private val addressRepository: AddressRepository = AddressRepository()

    private val _listAddress = MutableLiveData<List<Address>>()
    var listAddress: LiveData<List<Address>> = _listAddress

    var message = MutableLiveData<String>()

    var listProvince = MutableLiveData<List<Province>>()
    var listDistrict = MutableLiveData<List<District>>()
    var listWard = MutableLiveData<List<Ward>>()

    fun fetchAllAddress() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = addressRepository.getAllAddress()
            if (response.isSuccessful) {
                _listAddress.postValue(response.body())
            } else {
                _listAddress.postValue(emptyList())
            }
        } catch (e: Exception) {
            Log.d("errorAddress", e.toString())
        }
    }

    fun saveAddress(address: Address) = viewModelScope.launch {
        try {
            val response = addressRepository.saveAddress(address)
            if (response.isSuccessful) {
                message.postValue("Thành công!")
            } else {
                message.postValue(response.errorBody().toString())
            }
            fetchAllAddress()
        } catch (e: Exception) {
            Log.d("errorAddress", e.toString())
        }
    }

    fun deleteAddressById(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            addressRepository.deleteAddressById(id)
            fetchAllAddress()
        } catch (e: Exception) {
            Log.d("errorAddress", e.toString())
        }
    }

    fun fetchAllProvince() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = addressRepository.getAllProvince()
            val provinceList = parseProvinceListFromJson(response.toString())
            listProvince.postValue(provinceList)
        } catch (e: Exception) {
            Log.d("errorAddress", e.toString())
        }
    }

    fun fetchAllDistrictByProvinceID(provinceID: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = addressRepository.getAllDistrictByProvince(provinceID)
            val districtList = parseDistrictListFromJson(response.toString())
            listDistrict.postValue(districtList)
        } catch (e: Exception) {
            Log.d("errorAddress", e.toString())
        }
    }

    fun fetchAllWardByDistrictID(districtID: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = addressRepository.getAllWardByDistrict(districtID)
            val wardList = parseWardListFromJson(response.toString())
            listWard.postValue(wardList)
        } catch (e: Exception) {
            Log.d("errorAddress", e.toString())
        }
    }

    private fun parseProvinceListFromJson(jsonString: String): List<Province> {
        val provinceList = mutableListOf<Province>()
        val jsonObject = JSONObject(jsonString)
        val dataArray: JSONArray = jsonObject.getJSONArray("data")

        for (i in 0 until dataArray.length()) {
            val provinceID = dataArray.getJSONObject(i).getInt("ProvinceID")
            val provinceName = dataArray.getJSONObject(i).getString("ProvinceName")
            provinceList.add(Province(provinceID, provinceName))
        }
        return provinceList
    }

    private fun parseDistrictListFromJson(jsonString: String): List<District> {
        val districtList = mutableListOf<District>()
        val jsonObject = JSONObject(jsonString)
        val dataArray: JSONArray = jsonObject.getJSONArray("data")

        for (i in 0 until dataArray.length()) {
            val districtID = dataArray.getJSONObject(i).getInt("DistrictID")
            val districtName = dataArray.getJSONObject(i).getString("DistrictName")
            districtList.add(District(districtID, districtName))
        }
        return districtList
    }

    private fun parseWardListFromJson(jsonString: String): List<Ward> {
        val wardList = mutableListOf<Ward>()
        val jsonObject = JSONObject(jsonString)
        val dataArray: JSONArray = jsonObject.getJSONArray("data")

        for (i in 0 until dataArray.length()) {
            val wardCode = dataArray.getJSONObject(i).getString("WardCode")
            val wardName = dataArray.getJSONObject(i).getString("WardName")
            wardList.add(Ward(wardCode, wardName))
        }
        return wardList
    }
}