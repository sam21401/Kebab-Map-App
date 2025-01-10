package com.example.kebabapp.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kebabapp.R
import com.example.kebabapp.databinding.FragmentUserLoginBinding

class UserLoginFragment : Fragment() {
    private lateinit var binding: FragmentUserLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentUserLoginBinding.inflate(layoutInflater)
        binding.btnLogin.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_user_logging_to_navigation_user)
        }
        binding.tvRegister.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_user_logging_to_navigation_user_register)
        }

        return binding.root
    }
}
