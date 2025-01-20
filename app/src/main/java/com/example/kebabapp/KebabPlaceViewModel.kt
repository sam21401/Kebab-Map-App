package com.example.kebabapp

import androidx.lifecycle.ViewModel
import org.json.JSONException

class KebabPlaceViewModel : ViewModel() {
    private var kebabPlaces = KebabPlaces()
    private var kebabFilteredPlaces = KebabPlaces()

    fun getKebabPlaces(): KebabPlaces {
        return kebabPlaces
    }

    fun setKebabPlaces(kebabList: KebabPlaces) =
        try {
            kebabPlaces = kebabList
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    fun setFilteredKebabPlaces(kebabList: KebabPlaces) =
        try {
            kebabFilteredPlaces = kebabList
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    fun getFilteredKebabPlaces(): KebabPlaces {
        return kebabFilteredPlaces
    }

    fun clearFilteredKebabPlaces() {
        kebabFilteredPlaces.clear()
    }

    fun getKebabsAmount(): Int {
        if (kebabFilteredPlaces.isNullOrEmpty()) {
            return kebabPlaces.size
        } else {
            return kebabFilteredPlaces.size
        }
    }
}
