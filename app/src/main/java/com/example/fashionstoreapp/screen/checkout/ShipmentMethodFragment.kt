package com.example.fashionstoreapp.screen.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.ShipmentMethod
import com.example.fashionstoreapp.databinding.FragmentShipmentMethodBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.ShipmentMethodAdapter
import com.example.fashionstoreapp.screen.viewmodel.ShareCheckoutViewModel

class ShipmentMethodFragment : Fragment() {
    private lateinit var binding: FragmentShipmentMethodBinding
    private lateinit var headerBinding: HeaderLayoutBinding
    lateinit var shipmentMethodTmp: ShipmentMethod

    private val controller by lazy {
        findNavController()
    }

    private val shareCheckoutViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ShareCheckoutViewModel.ShareCheckoutViewModelFactory(requireActivity().application)
        )[ShareCheckoutViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShipmentMethodBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        headerBinding.txtTitle.text = getString(R.string.shipment_method)

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        val shipmentMethodAdapter = ShipmentMethodAdapter(mutableListOf(), this)
        binding.rvShipmentMethod.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvShipmentMethod.adapter = shipmentMethodAdapter

        shareCheckoutViewModel.shipmentMethods.observe(viewLifecycleOwner, Observer {
            shipmentMethodAdapter.setData(it)
        })

        shipmentMethodTmp = shareCheckoutViewModel.shipmentMethodSelected.value!!
        binding.btnConfirm.setOnClickListener {
            shareCheckoutViewModel.updateShipmentMethod(shipmentMethodTmp)
            controller.popBackStack()
        }

        return binding.root
    }

    fun updateShipmentMethod(shipmentMethod: ShipmentMethod) {
        shipmentMethodTmp = shipmentMethod
    }
}