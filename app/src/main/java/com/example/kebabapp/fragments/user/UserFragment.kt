package com.example.kebabapp.fragments.user

import RetrofitClient
import SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kebabapp.R
import com.example.kebabapp.databinding.FragmentUserPanelBinding
import com.example.kebabapp.utilities.LogoutResponse
import com.example.kebabapp.utilities.UserService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserPanelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentUserPanelBinding.inflate(layoutInflater)
        val userService = RetrofitClient.retrofit.create(UserService::class.java)
        val sharedPreferencesManager = context?.let { SharedPreferencesManager(it) }
        val isLogged = sharedPreferencesManager?.checkStatus()
        if (!isLogged!!) {
            Log.i("TOKEN", "User is not logged")
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                findNavController().navigate(R.id.action_navigation_user_to_navigation_user_logging)
            }
            return binding.root
        }
        viewLifecycleOwner.lifecycleScope.launch {
            val userName = getUserName(userService)
            if (!userName.isNullOrEmpty()) {
                binding.tvUserLoggedName.text = userName // Assuming you have a TextView for the username
            } else {
                binding.tvUserLoggedName.text = "Failed to load username"
            }
        }
        binding.buttonLogout.setOnClickListener {
            sharedPreferencesManager?.clearName()
            sharedPreferencesManager?.clearAuthToken()
            sharedPreferencesManager?.logout()
            findNavController().navigate(R.id.action_navigation_user_to_navigation_user_logging)
            userService.logoutUser().enqueue(
                object : Callback<LogoutResponse> {
                    override fun onResponse(
                        call: Call<LogoutResponse>,
                        response: Response<LogoutResponse>,
                    ) {
                        if (response.isSuccessful) {
                            RetrofitClient.setAuthToken("")
                            Log.i("LOGOUT", "SUCCESS LOGOUT")
                        } else {
                            Log.i("LOGOUT", "Something went wrong ")
                        }
                    }

                    override fun onFailure(
                        p0: Call<LogoutResponse>,
                        p1: Throwable,
                    ) {
                        Log.e("LOGOUT", "Error")
                    }
                },
            )
        }
        return binding.root
    }

    private suspend fun getUserName(userService: UserService): String? {
        return try {
            val response = userService.getProfileInfo() // Assuming this is a suspend function
            if (response.isSuccessful) {
                response.body()?.data?.name
            } else {
                Log.e("Profile", "Error: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")
            null
        }
    }
}
