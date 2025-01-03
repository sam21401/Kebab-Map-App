package com.example.kebabapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.kebabapp.databinding.ActivityMainBinding
import com.example.kebabapp.utilities.readJSONFromAssets
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

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

        val jsonString = readJSONFromAssets(baseContext, "sampledata.json")
        val data = Gson().fromJson(jsonString, KebabPlaces::class.java)
        kebabPlaces = ViewModelProvider(this)[KebabPlaceViewModel::class.java]
        kebabPlaces.setKebabPlaces(data)
    }
}
