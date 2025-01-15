package com.example.kebabapp.utilities

import com.example.kebabapp.KebabPlaceItem
import com.example.kebabapp.KebabPlaces

data class KebabResponse (
    val `data`: KebabPlaces,
    val message: String,
    val status: Boolean
)