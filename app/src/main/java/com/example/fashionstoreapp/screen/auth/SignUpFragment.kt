package com.example.fashionstoreapp.screen.auth

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.payload.SignupRequest
import com.example.fashionstoreapp.databinding.FragmentSignUpBinding
import com.example.fashionstoreapp.screen.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    private val controller by lazy {
        findNavController()
    }

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(
            this,
            AuthViewModel.AuthViewModelFactory(requireActivity().application)
        )[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        showAndHidePassword()

        val itemSpinner = arrayOf("Nam", "Nữ")
        val spinnerAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            itemSpinner
        )
        binding.spinnerGender.adapter = spinnerAdapter

        binding.btnSignIn.setOnClickListener {
            controller.navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            val name = binding.txtNameInput.text.toString().trim()
            val gender = binding.spinnerGender.selectedItem.toString().trim()
            val email = binding.txtEmailInput.text.toString().trim()
            val password = binding.txtPasswordInput.text.toString().trim()

            val request = SignupRequest(name, gender, email, password)
            binding.progressBar.visibility = View.VISIBLE
            viewModel.signUp(request)
        }

        viewModel.signUpResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            if (it == null) {
                Toast.makeText(requireContext(), "Email đã được sử dụng!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                controller.navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        })

        return binding.root
    }

    private fun showAndHidePassword() {
        binding.btnShowPassword.setOnClickListener {
            binding.btnShowPassword.visibility = View.INVISIBLE
            binding.btnHidePassword.visibility = View.VISIBLE
            binding.txtPasswordInput.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            binding.txtPasswordInput.setSelection(binding.txtPasswordInput.length())
        }

        binding.btnHidePassword.setOnClickListener {
            binding.btnShowPassword.visibility = View.VISIBLE
            binding.btnHidePassword.visibility = View.INVISIBLE
            binding.txtPasswordInput.transformationMethod =
                PasswordTransformationMethod.getInstance()
            binding.txtPasswordInput.setSelection(binding.txtPasswordInput.length())
        }
    }
}