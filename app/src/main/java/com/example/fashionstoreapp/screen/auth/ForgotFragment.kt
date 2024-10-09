package com.example.fashionstoreapp.screen.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.databinding.FragmentForgotBinding

class ForgotFragment : Fragment() {
    private lateinit var binding: FragmentForgotBinding

    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.txtEmailInput.requestFocus()

        binding.btnContinue.setOnClickListener {
            controller.navigate(R.id.action_forgotFragment_to_verificationFragment)
        }

        return binding.root
    }
}