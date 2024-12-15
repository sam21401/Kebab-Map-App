package com.example.kebabapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
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
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.navigation_list,
                    R.id.navigation_map,
                    R.id.navigation_user,
                ),
            )
        getSupportActionBar()?.hide()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val jsonString = readJSONFromAssets(baseContext, "sampledata.json")
        val data = Gson().fromJson(jsonString, KebabPlaces::class.java)
        kebabPlaces = ViewModelProvider(this)[KebabPlaceViewModel::class.java]
        kebabPlaces.setKebabPlaces(data)
    }
}
