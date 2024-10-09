package com.example.fashionstoreapp.screen.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.databinding.FragmentBankBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding

class BankFragment : Fragment() {
    private lateinit var binding: FragmentBankBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBankBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        headerBinding.txtTitle.text = getString(R.string.bank)

        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        return binding.root
    }
}