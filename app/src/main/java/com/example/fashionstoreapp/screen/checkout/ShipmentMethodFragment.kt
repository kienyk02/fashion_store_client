package com.example.fashionstoreapp.screen.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.ShipmentMethod
import com.example.fashionstoreapp.databinding.FragmentShipmentMethodBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.ShipmentMethodAdapter

class ShipmentMethodFragment : Fragment() {
    private lateinit var binding: FragmentShipmentMethodBinding
    private lateinit var headerBinding: HeaderLayoutBinding
    lateinit var shipmentMethodTmp: ShipmentMethod

    private val controller by lazy {
        findNavController()
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

        val list = mutableListOf<ShipmentMethod>()
        list.add(ShipmentMethod(1, "Vận chuyển nhanh", 1500))
        list.add(ShipmentMethod(2, "Vận chuyển tiết kiệm", 2000))
        list.add(ShipmentMethod(3, "Vận chuyển thường", 2500))
        shipmentMethodAdapter.setData(list)
        shipmentMethodTmp = list[0]

//          shareCheckoutViewModel.shipmentMethods.observe(viewLifecycleOwner, Observer {
//               shipmentMethodAdapter.setData(it)
//          })
//
//          shipmentMethodTmp= shareCheckoutViewModel.shipmentMethodSelected.value!!
//          binding.btnConfirm.setOnClickListener {
//               shareCheckoutViewModel.updateShipmentMethod(shipmentMethodTmp)
//               controller.popBackStack()
//          }

        return binding.root
    }

    fun updateShipmentMethod(shipmentMethod: ShipmentMethod) {
        shipmentMethodTmp = shipmentMethod
    }
}