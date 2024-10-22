package com.example.fashionstoreapp.screen.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.fashionstoreapp.data.model.Address
import com.example.fashionstoreapp.data.model.ShipmentMethod
import com.example.fashionstoreapp.data.model.User
import com.example.fashionstoreapp.data.network.ApiConfig
import com.example.fashionstoreapp.data.network.api.CalculateFeeShipApi
import com.example.fashionstoreapp.data.network.api.ShipmentMethodApi
import com.example.fashionstoreapp.data.payload.CalculateFeeShip
import com.example.fashionstoreapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ShareCheckoutViewModel(application: Application) : ViewModel() {
    private val userRepository: UserRepository = UserRepository(application)
    private val shipmentMethodApi: ShipmentMethodApi =
        ApiConfig.retrofit.create(ShipmentMethodApi::class.java)
    private val calculateFeeShipApi: CalculateFeeShipApi =
        ApiConfig.retrofitAddress.create(CalculateFeeShipApi::class.java)

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address> = _address

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> = _phone

    val shipmentMethods = MutableLiveData<List<ShipmentMethod>>()
    private val _shipmentMethodSelected = MutableLiveData<ShipmentMethod>()
    val shipmentMethodSelected: LiveData<ShipmentMethod> = _shipmentMethodSelected

    fun getUserInfo() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = userRepository.getUserInfo()
            if (response.isSuccessful) {
                val user = response.body() as User
                _name.postValue(user.name!!)
                _phone.postValue(user.phoneNumber!!)
            }
        } catch (e: Exception) {
            Log.d("userError", e.toString())
        }
    }

    fun calculateFeeShip(calculateFeeShip: CalculateFeeShip) = liveData(Dispatchers.IO) {
        try {
            val response = calculateFeeShipApi.calculateFeeShip(calculateFeeShip)
            val fee = response.getAsJsonObject("data").get("total").asInt
            emit(fee)
        } catch (e: Exception) {
            emit(0)
        }
    }

    fun fetchShipmentMethods() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val shipMethods = shipmentMethodApi.getShipmentMethods()
            shipmentMethods.postValue(shipMethods)
            _shipmentMethodSelected.postValue(shipMethods[0])
        } catch (e: Exception) {
            Log.d("errorShipment", e.toString())
        }
    }

    fun updateShipmentMethod(shipmentMethod: ShipmentMethod) {
        _shipmentMethodSelected.postValue(shipmentMethod)
    }

    fun updateAddressSelected(addr: Address) {
        _address.postValue(addr)
    }

    fun updateName(name: String) {
        _name.postValue(name)
    }

    fun updatePhone(phone: String) {
        _phone.postValue(phone)
    }

    class ShareCheckoutViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShareCheckoutViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShareCheckoutViewModel(application) as T
            }
            throw IllegalArgumentException("sss")
        }
    }
}