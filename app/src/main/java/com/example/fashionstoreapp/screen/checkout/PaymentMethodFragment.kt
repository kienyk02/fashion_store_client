package com.example.fashionstoreapp.screen.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.PaymentMethod
import com.example.fashionstoreapp.databinding.FragmentPaymentMethodBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import com.example.fashionstoreapp.screen.adapter.PaymentMethodAdapter

class PaymentMethodFragment : Fragment() {
    private lateinit var binding: FragmentPaymentMethodBinding
    private lateinit var headerBinding: HeaderLayoutBinding
    lateinit var paymentMethodTmp: PaymentMethod
    private val controller by lazy {
        findNavController()
    }
//    private val shareCheckoutViewModel by lazy {
//        ViewModelProvider(
//            requireActivity(),
//            ShareCheckoutViewModel.ShareCheckoutViewModelFactory(requireActivity().application)
//        )[ShareCheckoutViewModel::class.java]
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentMethodBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        headerBinding.txtTitle.text = getString(R.string.payment_method)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        val paymentMethodAdapter = PaymentMethodAdapter(mutableListOf(), this)
        binding.rvPaymentMethod.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvPaymentMethod.adapter = paymentMethodAdapter

        val list = mutableListOf<PaymentMethod>()
        list.add(PaymentMethod(1, "Thanh toán trực tiếp"))
        list.add(PaymentMethod(2, "Thanh toán chuyển khoản"))
        paymentMethodAdapter.setData(list)
        paymentMethodTmp = list[0]
//        shareCheckoutViewModel.paymentMethods.observe(viewLifecycleOwner, Observer {
//            paymentMethodAdapter.setData(it)
//        })
//
//        paymentMethodTmp= shareCheckoutViewModel.paymentMethodSelected.value!!
//        binding.btnConfirm.setOnClickListener {
//            shareCheckoutViewModel.updatePaymentMethod(paymentMethodTmp)
//            controller.popBackStack()
//        }

    }

    fun updatePaymentMethod(paymentMethod: PaymentMethod) {
        paymentMethodTmp = paymentMethod
    }
}