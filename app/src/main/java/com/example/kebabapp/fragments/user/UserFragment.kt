package com.example.kebabapp.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kebabapp.R
import com.example.kebabapp.databinding.FragmentUserLoginBinding
import com.example.kebabapp.databinding.FragmentUserPanelBinding

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserPanelBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentUserPanelBinding.inflate(layoutInflater)
        binding.buttonLogout.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_user_to_navigation_user_logging)
        }
        return binding.root
    }
}
