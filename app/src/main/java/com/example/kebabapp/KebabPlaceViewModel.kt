package com.example.kebabapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONException
import org.json.JSONObject


class KebabPlaceViewModel : ViewModel() {
    private var kebabPlaces = KebabPlaces()

    // Getter for LiveData
    fun getKebabPlaces(): KebabPlaces {
        return kebabPlaces
    }

    // Setter to update JSON data
    fun setKebabPlaces(kebabList: KebabPlaces) = try {
        kebabPlaces = kebabList
    } catch (e: JSONException) {
        e.printStackTrace()
    }
}