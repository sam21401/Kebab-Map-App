package com.example.kebabapp

import SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.kebabapp.databinding.KebabDetailPageBinding
import com.example.kebabapp.utilities.KebabResponse
import com.example.kebabapp.utilities.KebabService
import com.example.kebabapp.utilities.UserService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class KebabDetailPage : Fragment() {
    private lateinit var binding: KebabDetailPageBinding
    private lateinit var kebabDetail: KebabDetailPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = KebabDetailPageBinding.inflate(layoutInflater)
        val kebabService = RetrofitClient.retrofit.create(KebabService::class.java)
        val userService = RetrofitClient.retrofit.create(UserService::class.java)
        kebabDetail = ViewModelProvider(requireActivity()).get(KebabDetailPageViewModel::class.java)
        val userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        val sharedPreferencesManager = context?.let { SharedPreferencesManager(it) }
        val isLogged = sharedPreferencesManager?.checkStatus()
        val kebabId = kebabDetail.getKebabId().toString()
        viewLifecycleOwner.lifecycleScope.launch {
            val kebabBasicItem = getBasicKebabInfo(kebabService, kebabId)
            val kebabDetailItem = getDetailKebabInfo(kebabService, kebabId)
            val kebabOpeningHours = kebabDetailItem?.opening_hours
            binding.name.text = kebabBasicItem?.name.toString()
            binding.address.text = kebabBasicItem?.address.toString()
            binding.meatTypes.text = getStringFromTable(kebabDetailItem?.meat_types)
            binding.status.text = kebabDetailItem?.status.toString()
            binding.sauces.text = getStringFromTable(kebabDetailItem?.sauces)
            binding.craftIndicator.text = getYesNo(kebabDetailItem?.is_craft.toString())
            binding.chainMemberIndicator.text = getYesNo(kebabDetailItem?.is_chain_member.toString())
            binding.stallIndicator.text = getYesNo(kebabDetailItem?.is_in_stall.toString())
            binding.orderingOptions.text = getStringFromTable(kebabDetailItem?.ordering_options)
            binding.openingHours.text = getOpeningHours(kebabOpeningHours)
        }
        if( isLogged == true) {
            viewLifecycleOwner.lifecycleScope.launch {
                userViewModel.getFavKebabsFromApi(userService)
            }
            checkIfFavourite(kebabId, userViewModel)
            binding.favHeart.setOnClickListener{
                viewLifecycleOwner.lifecycleScope.launch {
                    if(userViewModel.checkIfKebabIsFavourite(kebabId.toInt()) == false)
                    {
                        addToFavourites(userService, kebabId)
                        binding.favHeart.setImageResource(R.drawable.ic_favorite)
                        userViewModel.getFavKebabsFromApi(userService)
                    }
                    else
                    {
                        removeFromFavourites(userService, kebabId)
                        binding.favHeart.setImageResource(R.drawable.ic_favorite_empty)
                        userViewModel.getFavKebabsFromApi(userService)
                    }
                }
            }
        }
        else
        {
            binding.favHeart.visibility = View.GONE
        }
        return binding.root
    }

    private fun checkIfFavourite(kebabId: String,userViewModel: UserViewModel) {
        val check =  userViewModel.checkIfKebabIsFavourite(kebabId.toInt())
        if (check == true)
        {
            binding.favHeart.setImageResource(R.drawable.ic_favorite)
        }
        else
        {
            binding.favHeart.setImageResource(R.drawable.ic_favorite_empty)
        }
    }

    private suspend fun removeFromFavourites(
        userService: UserService,
        kebabId: String
    ) {
        try {
            val response = userService.removeFromFavourites(kebabId)
            if (response.isSuccessful) {
                Snackbar.make(binding.root, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
            } else {
                Log.e("Profile", "Error: ${response.code()} - ${response.message()} ")
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")

        }
    }

    private suspend fun addToFavourites(
        userService: UserService,
        kebabId: String
    ) {
        try {
            val response = userService.addToFavourites(kebabId)
            if (response.isSuccessful) {
                Snackbar.make(binding.root, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
            } else {
                Log.e("Profile", "Error: ${response.code()} - ${response.message()} ")
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")

        }
    }

    private suspend fun getBasicKebabInfo(
        kebabService: KebabService,
        kebabId: String,
    ): KebabPlaceItem? {
        return try {
            val response = kebabService.getBasicKebabById(kebabId)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                Log.e("Profile", "Error: ${response.code()} - ${response.message()} ")
                null
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")
            null
        }
    }

    private suspend fun getDetailKebabInfo(
        kebabService: KebabService,
        kebabId: String,
    ): KebabPlaceDetailItem? {
        return try {
            val response = kebabService.getDetailsKebabById(kebabId)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                Log.e("Profile", "Error: ${response.code()} - ${response.message()} ")
                null
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")
            null
        }
    }

    private fun getOpeningHours(openingHours: OpeningHours?): String {
        return "Monday: " + openingHours?.monday.toString() + "\n" +
            "Tuesday: " + openingHours?.tuesday.toString() + "\n" +
            "Wednesday: " + openingHours?.wednesday.toString() + "\n" +
            "Thursday: " + openingHours?.thursday.toString() + "\n" +
            "Friday: " + openingHours?.friday.toString() + "\n" +
            "Saturday" + openingHours?.saturday.toString() + "\n" +
            "Sunday" + openingHours?.sunday.toString()
    }

    private fun getStringFromTable(list: List<String>?): String {
        var string = ""
        if (list != null) {
            for (name in list) {
                string = string + "‣ " + name + "\n"
            }
        }
        Log.i("STRING", string)
        return string
    }

    private fun getYesNo(string: String?): String {
        if (string == "1") {
            return "Yes"
        } else {
            return "No"
        }
    }
}
