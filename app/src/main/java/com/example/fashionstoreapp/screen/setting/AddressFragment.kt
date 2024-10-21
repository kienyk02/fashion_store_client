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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.databinding.FragmentAddressBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.AddressAdapter
import com.example.fashionstoreapp.screen.viewmodel.AddressViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class AddressFragment : Fragment() {
    private lateinit var binding: FragmentAddressBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    private val addressViewModel: AddressViewModel by lazy {
        ViewModelProvider(
            this
        )[AddressViewModel::class.java]
    }

    private lateinit var addressAdapter: AddressAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader

        headerBinding.txtTitle.text = getString(R.string.address)

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.btnAddAddress.setOnClickListener {
            controller.navigate(R.id.action_addressFragment_to_typingAddressFragment)
        }

        addressViewModel.fetchAllAddress()

        setUpAddressAdapter()

        return binding.root
    }

    private fun setUpAddressAdapter() {
        addressAdapter = AddressAdapter(listOf())
        binding.rvAddress.adapter = addressAdapter
        binding.rvAddress.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        addressAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putParcelable("address", it)
            }
            controller.navigate(R.id.action_addressFragment_to_typingAddressFragment, bundle)
        }
        addressViewModel.listAddress.observe(viewLifecycleOwner, Observer {
            addressAdapter.setData(it)
        })
    }

}