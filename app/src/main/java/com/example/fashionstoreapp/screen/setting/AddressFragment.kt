package com.example.fashionstoreapp.screen.setting

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.databinding.FragmentAddressBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class AddressFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentAddressBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var latLng: LatLng
    private lateinit var geocoder: Geocoder
    private var prevAddress: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader

        headerBinding.txtTitle.text = getString(R.string.address)
        val editTextTimeoutHelper = EditTextTimeoutHelper(binding.txtAddress, 2000)

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
            editTextTimeoutHelper.removeListener()
        }

        binding.btnEdit.setOnClickListener {
            binding.btnEdit.visibility = View.INVISIBLE
            binding.btnSave.visibility = View.VISIBLE
            binding.txtAddress.isEnabled = true
        }

        binding.btnSave.setOnClickListener {
            binding.btnEdit.visibility = View.VISIBLE
            binding.btnSave.visibility = View.INVISIBLE
            binding.txtAddress.isEnabled = false
            updateAddress()
        }

        return binding.root
    }


    private fun updateAddress() {
//        userViewModel.responseUpdateAddress.observe(viewLifecycleOwner, Observer {
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//        })

        val address = binding.txtAddress.text.toString().trim()
        prevAddress = address
        val reqBodyAddress: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), address)
//        userViewModel.updateAddress(reqBodyAddress)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        geocoder = Geocoder(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.btnMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        map.let {
            googleMap = it

//            userViewModel.user.observe(viewLifecycleOwner, Observer {
//                when(it){
//                    is Resource.Success -> {
//                        binding.txtAddress.setText(it.value.address!!.address)
//                        var address: Address? = null
//                        if(it.value.address.address != "Chưa có địa chỉ"){
//                            address = geocoder.getFromLocationName(it.value.address.address, 1)!!.get(0)
//                        } else {
//                            address = geocoder.getFromLocationName("Hà Nội, Việt Nam", 1)!!.get(0)
//                        }
//                        latLng = LatLng(address.latitude, address.longitude)
//                        prevAddress = it.value.address.address

//                        val zoomLevel =
//                            15f // Độ zoom ban đầu, giá trị từ 1 (zoom nhỏ nhất) đến 20 (zoom lớn nhất)
//
//                        // Di chuyển camera đến vị trí ban đầu và set độ zoom
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
//
//                        // Đặt marker tại vị trí ban đầu
//                        marker = googleMap.addMarker(MarkerOptions().position(latLng))!!
//                    }
//                    is Resource.Failure -> {
//                        Toast.makeText(requireContext(), "Không tìm thấy thông tin địa chỉ!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
//            userViewModel.getUserInfo()

            googleMap.setOnMapClickListener { newLatLng ->
                val address: Address =
                    geocoder.getFromLocation(newLatLng.latitude, newLatLng.longitude, 1)!!.get(0)
                latLng = newLatLng
                moveLocation(latLng)
                binding.txtAddress.setText(address.getAddressLine(0))
            }
        }
    }

    fun moveLocation(latLng: LatLng) {
        marker.remove()
        marker = googleMap.addMarker(
            MarkerOptions().position(latLng).title("" + latLng.latitude + " KG " + latLng.longitude)
        )!!
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    inner class EditTextTimeoutHelper(
        private val editText: EditText,
        private val timeoutInMillis: Int
    ) {
        private var handler: Handler = Handler()
        private var runnable: Runnable? = null

        private val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }

            override fun afterTextChanged(editable: Editable?) {
                runnable?.let { handler.removeCallbacks(it) }
                runnable = Runnable {
                    val location: String = binding.txtAddress.text.toString()
                    var addrList: ArrayList<Address> = ArrayList()
                    if (location != "") {
                        try {
                            addrList =
                                geocoder.getFromLocationName(location, 1) as ArrayList<Address>
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    if (addrList.size > 0) {
                        val address: Address = addrList.get(0)
                        latLng = LatLng(address.latitude, address.longitude)
                        moveLocation(latLng)
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Can't Find Your Address!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                handler.postDelayed(runnable!!, timeoutInMillis.toLong())
            }
        }

        init {
            editText.addTextChangedListener(textWatcher)
        }

        fun removeListener() {
            runnable?.let { handler.removeCallbacks(it) }
            editText.removeTextChangedListener(textWatcher)
        }
    }
}