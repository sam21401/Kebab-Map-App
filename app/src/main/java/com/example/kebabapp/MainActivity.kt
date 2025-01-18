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
import com.example.kebabapp.utilities.readJSONFromAssets
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var kebabPlaces: KebabPlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        navView.setupWithNavController(navController)
        val sharedPreferencesManager = SharedPreferencesManager(this)
        val authToken = sharedPreferencesManager.getAuthToken()
        if (authToken != null) {
            RetrofitClient.setAuthToken(authToken)
        }
        val kebabService = RetrofitClient.retrofit.create(KebabService::class.java)
        val jsonString = readJSONFromAssets(baseContext, "sampledata.json")
        //val data = Gson().fromJson(jsonString, KebabPlaces::class.java)
        kebabPlaces = ViewModelProvider(this)[KebabPlaceViewModel::class.java]
        lifecycleScope.launch {
            val data = getAllKebab(kebabService)
            if (data != null) {
                kebabPlaces.setKebabPlaces(data)
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
