
package com.example.kebabapp.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kebabapp.R
import com.example.kebabapp.databinding.FragmentUserRegisterBinding
import com.example.kebabapp.utilities.LoginResponse
import com.example.kebabapp.utilities.UserService
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRegisterFragment : Fragment() {
    private lateinit var binding: FragmentUserRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentUserRegisterBinding.inflate(layoutInflater)
        val userService = RetrofitClient.retrofit.create(UserService::class.java)
        val name = binding.etUsername.text
        val email = binding.etUseremail.text
        val password = binding.etPassword.text
        binding.btnRegister.setOnClickListener {
            if (email != null && name != null && password != null) {
                userService.registerUser(email = email.toString(), password = password.toString(), name = name.toString()).enqueue(
                    object :
                        Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>,
                        ) {
                            if (response.isSuccessful) {
                                Snackbar.make(binding.root, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_navigation_user_register_to_navigation_user_logging)
                            } else {
                                Snackbar.make(binding.root, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(
                            p0: Call<LoginResponse>,
                            p1: Throwable,
                        ) {
                            Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                        }
                    },
                )
            }
        }
        return binding.root
    }
}
