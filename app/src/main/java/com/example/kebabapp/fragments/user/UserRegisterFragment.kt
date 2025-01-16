package com.example.kebabapp.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kebabapp.R
import com.example.kebabapp.databinding.FragmentUserRegisterBinding

class UserRegisterFragment : Fragment() {
    private lateinit var binding: FragmentUserRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentUserRegisterBinding.inflate(layoutInflater)
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_user_register_to_navigation_user_logging)
        }
        return binding.root
    }
}
