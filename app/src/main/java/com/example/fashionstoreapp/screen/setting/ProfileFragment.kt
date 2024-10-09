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

        return binding.root
    }


}