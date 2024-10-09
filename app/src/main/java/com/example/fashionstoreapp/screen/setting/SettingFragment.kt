package com.example.fashionstoreapp.screen.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.databinding.FragmentSettingBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader

        headerBinding.txtTitle.text = getString(R.string.setting)
        headerBinding.btnBack.visibility = View.GONE
        binding.apply {
            btnProfile.setOnClickListener {
                controller.navigate(R.id.action_navigationFragment_to_profileFragment)
            }

            btnAddress.setOnClickListener {
                controller.navigate(R.id.action_navigationFragment_to_addressFragment)
            }

            btnPayment.setOnClickListener {
                controller.navigate(R.id.action_navigationFragment_to_bankFragment)
            }

        }

        return binding.root
    }
}