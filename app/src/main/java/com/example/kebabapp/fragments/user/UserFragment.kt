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
import com.example.kebabapp.utilities.ProfileResponse
import com.example.kebabapp.utilities.UserService
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
        val token = sharedPreferencesManager?.getAuthToken()
        binding.tvUserLoggedName.text = token.toString()
        Log.i("TOKEN",token.toString())
        val isLogged = sharedPreferencesManager?.checkStatus()
        Log.i("TOKEN",isLogged.toString())
        if(!isLogged!!)
        {
            Log.i("TOKEN", "User is not logged")
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                findNavController().navigate(R.id.action_navigation_user_to_navigation_user_logging)
            }
            return binding.root
        }
        binding.buttonLogout.setOnClickListener {
            sharedPreferencesManager?.clearName()
            sharedPreferencesManager?.clearAuthToken()
            sharedPreferencesManager?.logout()
            Log.i("TOKEN",sharedPreferencesManager?.getAuthToken().toString()+" RT\n"+RetrofitClient.getAuthToken().toString())
            findNavController().navigate(R.id.action_navigation_user_to_navigation_user_logging)
                userService.logoutUser().enqueue(object : Callback<LogoutResponse>{
                    override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                        if (response.isSuccessful) {
                            RetrofitClient.setAuthToken("")
                            Log.i("LOGOUT","SUCCESS LOGOUT")
                        }
                        else
                        {
                            Log.i("LOGOUT","FAKAP")
                        }
                    }
                    override fun onFailure(p0: Call<LogoutResponse>, p1: Throwable) {
                        Log.e("LOGOUT","Error")
                    }
                })
        }
        return binding.root
    }
    fun getUserName(userService : UserService): String{
        var name = String()
        userService.getProfileInfo().enqueue(object : Callback<ProfileResponse>{
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    name = response.body()?.data?.name.toString()
                    Log.e("Profile", "Name: $name")
                } else {
                    Log.e("Profile", "Error: ${response.code()} - ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e("Profile", "Failure: ${t.message}")
            }
        })
        return name
    }
}
