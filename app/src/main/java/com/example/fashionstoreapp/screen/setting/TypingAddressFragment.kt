package com.example.fashionstoreapp.screen.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Address
import com.example.fashionstoreapp.data.model.District
import com.example.fashionstoreapp.data.model.Province
import com.example.fashionstoreapp.data.model.Ward
import com.example.fashionstoreapp.databinding.FragmentTypingAddressBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.viewmodel.AddressViewModel

class TypingAddressFragment : Fragment() {
    private lateinit var binding: FragmentTypingAddressBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    private val addressViewModel: AddressViewModel by lazy {
        ViewModelProvider(
            this
        )[AddressViewModel::class.java]
    }

    private var address: Address? = null
    private var selectedCity: Province? = null
    private var selectedDistrict: District? = null
    private var selectedWard: Ward? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTypingAddressBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        headerBinding.txtTitle.text = getString(R.string.address)

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        addressViewModel.fetchAllProvince()
        setUpSpinner()
        handleSaveAddress()
        handleDataInput()
        binding.btnDelete.setOnClickListener {
            address!!.id?.let { it1 -> addressViewModel.deleteAddressById(it1) }
            controller.popBackStack()
        }

        return binding.root
    }

    private fun handleDataInput() {
        address = arguments?.getParcelable("address")
        if (address != null) {
            binding.btnDelete.visibility = View.VISIBLE
            binding.edtHouse.setText(address!!.address)
            if (address!!.active == 1) binding.switchActiveAddress.isChecked = true
        }
    }

    private fun handleSaveAddress() {
        binding.btnConfirm.setOnClickListener {
            saveAddress()
        }

        addressViewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAddress() {
        if (binding.edtHouse.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT)
                .show()
            binding.edtHouse.requestFocus()
            return
        }
        val addressBody = Address(
            provinceId = selectedCity!!.provinceID,
            provinceName = selectedCity!!.provinceName,
            districtId = selectedDistrict!!.districtID,
            districtName = selectedDistrict!!.districtName,
            wardId = selectedWard!!.wardID,
            wardName = selectedWard!!.wardName,
            address = binding.edtHouse.text.toString(),
            active = if (binding.switchActiveAddress.isChecked) 1 else 0
        )
        if (address != null) addressBody.id = address!!.id
        addressViewModel.saveAddress(addressBody)
        controller.popBackStack()
    }

    private fun setUpSpinner() {
        // Adapter cho City
        addressViewModel.listProvince.observe(viewLifecycleOwner) {
            val cityAdapter = ArrayAdapter<Province>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it
            )
            binding.spinnerCity.adapter = cityAdapter

            if (address != null) {
                for (i in it) {
                    if (i.provinceID == address!!.provinceId) {
                        binding.spinnerCity.setSelection(it.indexOf(i))
                        break
                    }
                }
            }
        }
        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedCity = parent.getItemAtPosition(position) as Province
                addressViewModel.fetchAllDistrictByProvinceID(selectedCity!!.provinceID)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        addressViewModel.listDistrict.observe(viewLifecycleOwner) {
            val districtAdapter = ArrayAdapter<District>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it
            )
            binding.spinnerDistrict.adapter = districtAdapter
            if (address != null) {
                for (i in it) {
                    if (i.districtID == address!!.districtId) {
                        binding.spinnerDistrict.setSelection(it.indexOf(i))
                        break
                    }
                }
            }
        }

        binding.spinnerDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedDistrict = parent.getItemAtPosition(position) as District
                    addressViewModel.fetchAllWardByDistrictID(selectedDistrict!!.districtID)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }


        addressViewModel.listWard.observe(viewLifecycleOwner) {
            val wardAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it
            )
            binding.spinnerWard.adapter = wardAdapter
            if (address != null) {
                for (i in it) {
                    if (i.wardID == address!!.wardId) {
                        binding.spinnerWard.setSelection(it.indexOf(i))
                        break
                    }
                }
            }
        }

        binding.spinnerWard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedWard = parent.getItemAtPosition(position) as Ward
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}