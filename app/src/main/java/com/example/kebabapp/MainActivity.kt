package com.example.kebabapp

import RetrofitClient
import SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.kebabapp.databinding.ActivityMainBinding
import com.example.kebabapp.utilities.KebabService
import com.example.kebabapp.utilities.UserService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var kebabPlaces: KebabPlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val userService = RetrofitClient.retrofit.create(UserService::class.java)
        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        navView.setupWithNavController(navController)
        val sharedPreferencesManager = SharedPreferencesManager(this)
        val authToken = sharedPreferencesManager.getAuthToken()
        if (authToken != null) {
            RetrofitClient.setAuthToken(authToken)
        }
        val kebabService = RetrofitClient.retrofit.create(KebabService::class.java)
        kebabPlaces = ViewModelProvider(this)[KebabPlaceViewModel::class.java]
        lifecycleScope.launch {
            val data = getAllKebab(kebabService)
            if (data != null) {
                kebabPlaces.setKebabPlaces(data)
                if (sharedPreferencesManager.checkStatus() == true) {
                    userViewModel.getFavKebabsFromApi(userService)
                }
            }
        }
    }

    private suspend fun getAllKebab(kebabService: KebabService): KebabPlaces? {
        return try {
            val response = kebabService.getAllKebabs()
            if (response.isSuccessful) {
                response.body()?.data
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
