package com.example.kebabapp.fragments

import SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Snackbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kebabapp.R
import com.example.kebabapp.databinding.FragmentSuggestionBinding
import com.example.kebabapp.utilities.LoginResponse
import com.example.kebabapp.utilities.UserService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuggestionFormFragment: Fragment() {
    private lateinit var binding: FragmentSuggestionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSuggestionBinding.inflate(layoutInflater)
        val userService = RetrofitClient.retrofit.create(UserService::class.java)
        val sharedPreferencesManager = context?.let { SharedPreferencesManager(it) }
        val isLogged = sharedPreferencesManager?.checkStatus()
        val suggestion = binding.suggestionInput.text
        binding.submitButton.setOnClickListener {
        if(suggestion != null && isLogged == true) {

                viewLifecycleOwner.lifecycleScope.launch {
                    sendSuggestion(suggestion.toString(),userService)
                }
        }
        else if (isLogged == false)
        {
            Snackbar.make(binding.root, "Please log in to send suggestions", Snackbar.LENGTH_SHORT).show()
        }
        else
            Snackbar.make(binding.root, "Suggestion is empty ", Snackbar.LENGTH_SHORT).show()
        }
        binding.clearButton.setOnClickListener {
            binding.suggestionInput.text.clear()
        }
        return binding.root
    }

    private suspend fun sendSuggestion(input: String,userService: UserService)
    {
        try {
            val response = userService.sendSuggestion(input)
            if (response.isSuccessful) {
                Snackbar.make(binding.root, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")
        }
    }
}