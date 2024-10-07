package com.example.fashionstoreapp.screen

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.databinding.FragmentCheckoutBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class CheckoutFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var geocoder: Geocoder

    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        headerBinding.txtTitle.text = getString(R.string.payment)

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.btnEditName.setOnClickListener {
            openDialogEditName()
        }

        binding.btnEditPhone.setOnClickListener {
            openDialogEditPhone()
        }


        geocoder = Geocoder(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.btnMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        map.let {
            googleMap = it
            var addr = "Yên Khê, Yên Thường, Gia Lâm, Hà Nội"
//            shareCheckoutViewModel.address.observe(viewLifecycleOwner, Observer { addr ->
            binding.txtAddress.text = addr
            var address: Address? = null
            var LatLng = LatLng(21.0278, 105.8342)
            try {
                address = if (addr != "Chưa có địa chỉ") {
                    geocoder.getFromLocationName(addr, 1)!!.get(0)
                } else {
                    geocoder.getFromLocationName("Hà Nội, Việt Nam", 1)!!.get(0)
                }
                LatLng = LatLng(address.latitude, address.longitude)
            } catch (e: IOException) {
                Log.d("geocoderError", "error")
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 15f))
            googleMap.clear()
            marker = googleMap.addMarker(MarkerOptions().position(LatLng))!!
//            })

//            googleMap.setOnMapClickListener { latLng ->
//                controller.navigate(R.id.action_checkoutFragment_to_GGMapFragment)
//            }
        }
    }

    private fun openDialogEditName() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_edit_name)

        val window = dialog.window
        if (window == null) {
            return
        }
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))// Để loại bỏ viền màu trắng xung quanh dialog

        val btnSave: Button = dialog.findViewById(R.id.btnSave)
        val edtName: EditText = dialog.findViewById(R.id.edtName)
        edtName.setText(binding.txtName.text.toString())
        btnSave.setOnClickListener {
            val name = edtName.text.toString()
            if (name != "") {
//                shareCheckoutViewModel.updateName(name)
                dialog.dismiss()
            } else {
                Toast.makeText(requireActivity(), "Vui lòng nhập họ tên", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dialog.show()
    }

    private fun openDialogEditPhone() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_edit_phonenumber)

        val window = dialog.window
        if (window == null) {
            return
        }
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))// Để loại bỏ viền màu trắng xung quanh dialog

        val btnSave: Button = dialog.findViewById(R.id.btnSave)
        val edtPhone: EditText = dialog.findViewById(R.id.edtPhone)
        edtPhone.setText(binding.txtPhone.text.toString())
        btnSave.setOnClickListener {
            val phone = edtPhone.text.toString()
            if (phone.length == 10) {
//                shareCheckoutViewModel.updatePhone(phone)
                dialog.dismiss()
            } else {
                Toast.makeText(requireActivity(), "Please enter all 10 numbers", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dialog.show()
    }
}