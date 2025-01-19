package com.example.kebabapp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kebabapp.utilities.UserService

class UserViewModel : ViewModel() {
    private var favKebabPlaces = KebabPlaces()

    fun getFavKebabPlaces(): KebabPlaces {
        return favKebabPlaces
    }

    suspend fun getFavKebabsFromApi(userService: UserService) {
        try {
            val response = userService.getFavourites()
            if (response.isSuccessful) {
                Log.i("FAVS", response.body()?.data.toString())
                favKebabPlaces = response.body()?.data!!
            } else {
                Log.e("Profile", "Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")
        }
    }

    fun checkIfKebabIsFavourite(kebabId: Int): Boolean {
        for (kebab in favKebabPlaces) {
            if (kebab.id == kebabId) {
                return true
            }
        }
        return false
    }

    fun clearFavKebabPlaces() {
        favKebabPlaces.clear()
    }
}
