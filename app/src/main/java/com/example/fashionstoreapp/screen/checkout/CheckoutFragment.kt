package com.example.fashionstoreapp.screen.checkout

import android.annotation.SuppressLint
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.Cart
import com.example.fashionstoreapp.data.model.ShipmentMethod
import com.example.fashionstoreapp.data.payload.CalculateFeeShip
import com.example.fashionstoreapp.data.payload.Item
import com.example.fashionstoreapp.databinding.FragmentCheckoutBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.viewmodel.AddressViewModel
import com.example.fashionstoreapp.screen.viewmodel.CartViewModel
import com.example.fashionstoreapp.screen.viewmodel.ShareCheckoutViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(
            this,
            CartViewModel.CartViewModelFactory(requireActivity().application)
        )[CartViewModel::class.java]
    }
    private val addressViewModel: AddressViewModel by lazy {
        ViewModelProvider(
            this
        )[AddressViewModel::class.java]
    }
    private val shareCheckoutViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ShareCheckoutViewModel.ShareCheckoutViewModelFactory(requireActivity().application)
        )[ShareCheckoutViewModel::class.java]
    }

    private var listShipmentMethod = mutableListOf<ShipmentMethod>()
    private var totalPrice: Int = 0;
    private var shipPrice: Int = 0;
    private var isFirstTimeCreated = true
    private var weight = 0
    private var height = 0
    private var width = 0
    private var length = 0
    private var districtId = 0
    private var wardCode = ""

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

        binding.layoutShipmentMethod.setOnClickListener {
            controller.navigate(R.id.action_checkoutFragment_to_shipmentMethodFragment)
        }

        binding.layoutPaymentMethod.setOnClickListener {
            controller.navigate(R.id.action_checkoutFragment_to_paymentMethodFragment)
        }

        binding.btnPayment.setOnClickListener {
            openDialogNotification()
        }

        binding.layoutAddress.setOnClickListener {
            controller.navigate(R.id.action_checkoutFragment_to_addressFragment)
        }

        if (isFirstTimeCreated) {
            shareCheckoutViewModel.getUserInfo()
            shareCheckoutViewModel.fetchShipmentMethods()
            cartViewModel.fetchAllCart()
            isFirstTimeCreated = false
        }

        handleCart()
        handleNameAndPhone()
        handleShipmentMethod()

        return binding.root
    }

    private fun handleShipmentMethod() {
        shareCheckoutViewModel.shipmentMethods.observe(viewLifecycleOwner, Observer {
            listShipmentMethod.clear()
            listShipmentMethod.addAll(it)
            handleAddress()
        })

        shareCheckoutViewModel.shipmentMethodSelected.observe(viewLifecycleOwner, Observer {
            binding.shipmentMethod.text = it.name
            binding.txtShiping.text = formatPrice(it.price)
            shipPrice = it.price
            binding.txtTotal.text = formatPrice(totalPrice + shipPrice)
        })

    }

    @SuppressLint("SetTextI18n")
    private fun handleAddress() {
        addressViewModel.fetchAllAddress()
        addressViewModel.listAddress.observe(viewLifecycleOwner, Observer {
            var check = false
            for (address in it) {
                if (address.active == 1) {
                    shareCheckoutViewModel.updateAddressSelected(address)
                    check = true
                    break
                }
            }
            if (!check) {
                shareCheckoutViewModel.updateAddressSelected(it[0])
            }
        })

        shareCheckoutViewModel.address.observe(viewLifecycleOwner, Observer {
            binding.txtAddress.text = it.address
            binding.txtWard.text = "${it.wardName}, ${it.districtName}, ${it.provinceName}"
            districtId = it.districtId
            wardCode = it.wardId
            getFeeShip()
        })
    }

    private fun getFeeShip() {
        for (shipmentMethod in listShipmentMethod) {
            val calculateFeeShip = CalculateFeeShip(
                service_type_id = shipmentMethod.type,
                from_district_id = 1703,
                from_ward_code = "1A1221",
                to_district_id = districtId,
                to_ward_code = wardCode,
                weight = weight,
                length = length,
                width = width,
                height = height,
                items = listOf(Item())
            )
            shareCheckoutViewModel.calculateFeeShip(calculateFeeShip)
                .observe(viewLifecycleOwner, Observer { value ->
                    shipmentMethod.price = value
                    if (shareCheckoutViewModel.shipmentMethodSelected.value!!.id == shipmentMethod.id) {
                        shareCheckoutViewModel.updateShipmentMethod(shipmentMethod)
                    }
                })
        }
    }

    private fun handleCart() {
        cartViewModel.listCart.observe(viewLifecycleOwner, Observer {
            totalPrice = 0
            var quantityProduct = 0
            for (cart in it) {
                totalPrice += (cart.product.price - cart.product.price * cart.product.discount / 100).toInt() * cart.quantity
                quantityProduct += cart.quantity

                weight = 0
                height = 0
                width = 0
                length = 0
                weight += cart.product.weight.toInt() * cart.quantity
                height += cart.product.height.toInt() * cart.quantity
                width = cart.product.width.toInt()
                length = cart.product.depth.toInt()
            }
            binding.txtSubtotal.text = formatPrice(totalPrice)
            binding.txtTotal.text = formatPrice(totalPrice + shipPrice)
        })
    }

    private fun handleNameAndPhone() {
        shareCheckoutViewModel.name.observe(viewLifecycleOwner, Observer {
            binding.txtName.text = it
        })
        shareCheckoutViewModel.phone.observe(viewLifecycleOwner, Observer {
            binding.txtPhone.text = it
        })
    }


    fun openDialogNotification() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_noti_checkout)
        dialog.setCanceledOnTouchOutside(false)

        val window = dialog.window
        if (window == null) {
            return
        }
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))// Để loại bỏ viền màu trắng xung quanh dialog

        var btnBack: Button = dialog.findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            controller.navigate(R.id.action_checkoutFragment_to_orderHistoryFragment)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openDialogEditName() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_edit_name)

        val window = dialog.window
        if (window == null) {
            return
        }
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnSave: Button = dialog.findViewById(R.id.btnSave)
        val edtName: EditText = dialog.findViewById(R.id.edtName)
        edtName.setText(binding.txtName.text.toString())
        btnSave.setOnClickListener {
            val name = edtName.text.toString()
            if (name != "") {
                shareCheckoutViewModel.updateName(name)
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
                shareCheckoutViewModel.updatePhone(phone)
                dialog.dismiss()
            } else {
                Toast.makeText(requireActivity(), "Please enter all 10 numbers", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dialog.show()
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }
}