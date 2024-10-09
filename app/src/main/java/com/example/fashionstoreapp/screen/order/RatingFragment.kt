package com.example.fashionstoreapp.screen.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fashionstoreapp.databinding.FragmentRatingBinding

class RatingFragment : Fragment() {
    private lateinit var binding: FragmentRatingBinding

    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRatingBinding.inflate(inflater, container, false)

        return binding.root
    }
}