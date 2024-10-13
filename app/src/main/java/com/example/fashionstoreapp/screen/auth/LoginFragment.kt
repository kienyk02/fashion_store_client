package com.example.fashionstoreapp.screen.auth

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.network.JwtManager
import com.example.fashionstoreapp.data.payload.SigninRequest
import com.example.fashionstoreapp.databinding.FragmentLoginBinding
import com.example.fashionstoreapp.screen.viewmodel.AuthViewModel
import com.example.fashionstoreapp.screen.viewmodel.UserViewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val controller by lazy {
        findNavController()
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProvider(
            this,
            AuthViewModel.AuthViewModelFactory(requireActivity().application)
        )[AuthViewModel::class.java]
    }
    private val viewModelUser: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(requireActivity().application)
        )[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        JwtManager.CURRENT_USER = JwtManager.getUserId(requireActivity().application)
        JwtManager.CURRENT_JWT = JwtManager.getJwtToken(requireActivity().application)

        viewModelUser.user.observe(viewLifecycleOwner, Observer {
            if (it.id != 0) {
                controller.navigate(R.id.action_loginFragment_to_navigationFragment)
            }
        })
        viewModelUser.getUserInfo()

        viewModelAuth.signInResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            if (it == null) {
                Toast.makeText(
                    requireContext(),
                    "Email hoặc mật khẩu không chính xác!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                JwtManager.saveJwtToken(requireActivity().application, it.token)
                JwtManager.CURRENT_JWT = JwtManager.getJwtToken(requireActivity().application)
                JwtManager.saveUserId(requireActivity().application, it.userId.toString())
                JwtManager.CURRENT_USER = JwtManager.getUserId(requireActivity().application)
                controller.navigate(R.id.action_loginFragment_to_navigationFragment)
            }
        })

        showAndHidePassword()

        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmailInput.text.toString().trim()
            val password = binding.txtPasswordInput.text.toString().trim()
            //add validation sign in information
            val request = SigninRequest(email, password)
            binding.progressBar.visibility = View.VISIBLE
            viewModelAuth.signIn(request)
        }

        binding.btnSignUp.setOnClickListener {
            controller.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.btnForgot.setOnClickListener {
            controller.navigate(R.id.action_loginFragment_to_forgotFragment)
        }

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