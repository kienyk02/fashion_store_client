package com.example.fashionstoreapp.screen

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val controller by lazy{
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        showAndHidePassword()

        binding.btnLogin.setOnClickListener {
            controller.navigate(R.id.action_loginFragment_to_navigationFragment)
        }

        binding.btnSignUp.setOnClickListener {
            controller.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        return binding.root
    }

    private fun showAndHidePassword(){
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