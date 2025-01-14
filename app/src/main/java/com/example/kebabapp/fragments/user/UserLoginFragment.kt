package com.example.kebabapp.fragments.user

import SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kebabapp.R
import com.example.kebabapp.databinding.FragmentUserLoginBinding
import com.example.kebabapp.utilities.LoginResponse
import com.example.kebabapp.utilities.UserService
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserLoginFragment : Fragment() {
    private lateinit var binding: FragmentUserLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreate(savedInstanceState)
        val userService = RetrofitClient.retrofit.create(UserService::class.java)
        binding = FragmentUserLoginBinding.inflate(layoutInflater)
        val sharedPreferencesManager = context?.let { SharedPreferencesManager(it) }
        binding.btnLogin.setOnClickListener {
            val email = binding.etUseremail.text
            val password = binding.etPassword.text
            if (email != null && password != null) {
                userService.loginUser(email = email.toString(), password = password.toString()).enqueue(
                    object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>,
                        ) {
                            if (response.isSuccessful) {
                                Log.d("Public API", "Data: ${response.body()}")
                                sharedPreferencesManager?.saveAuthToken(response.body()?.token.toString())
                                sharedPreferencesManager?.login()
                                Snackbar.make(binding.root, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
                                RetrofitClient.setAuthToken(response.body()?.token.toString())
                                findNavController().navigate(R.id.action_navigation_user_logging_to_navigation_user)
                            } else {
                                Log.e("Public API", "Error: ${response.body()?.toString()}")
                                Snackbar.make(binding.root, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(
                            call: Call<LoginResponse>,
                            t: Throwable,
                        ) {
                            Log.e("Public API", "Failure: ${t.message}")
                        }
                    },
                )
            }
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_user_logging_to_navigation_user_register)
        }
        return binding.root
    }
}
