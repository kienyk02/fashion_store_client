package com.example.fashionstoreapp.screen.setting

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.databinding.FragmentProfileBinding
import com.example.fashionstoreapp.databinding.HeaderLayoutBinding
import java.io.File

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val controller by lazy {
        findNavController()
    }

    private var uri: Uri? = null
    private var avatarImage: File? = null
    private var prevName: String? = null
    private var prevGender: Int? = null
    private var prevPhoneNum: String? = null
    private var prevAvatar: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader

        headerBinding.txtTitle.text = getString(R.string.profile)
        headerBinding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.apply {
            btnEdit.setOnClickListener {
                btnEdit.visibility = View.INVISIBLE
                txtChangeAvt.visibility = View.VISIBLE
                btnAvt.isClickable = true
                txtEditName.isEnabled = true
                spinnerGender.isEnabled = true
                spinnerGender.isClickable = true
                btnSelectGender.visibility = View.VISIBLE
                txtEditPhone.isEnabled = true
                btnSave.visibility = View.VISIBLE
            }

            btnSave.setOnClickListener {
                btnEdit.visibility = View.VISIBLE
                txtChangeAvt.visibility = View.INVISIBLE
                btnAvt.isClickable = false
                txtEditName.isEnabled = false
                spinnerGender.isEnabled = false
                spinnerGender.isClickable = false
                btnSelectGender.visibility = View.INVISIBLE
                txtEditPhone.isEnabled = false
                btnSave.visibility = View.INVISIBLE
//                updateUserInfo()
            }
        }


        return binding.root
    }


}