package com.example.kebabapp.utilities

import com.example.kebabapp.KebabPlaceItem

data class KebabBasicResponse(
    val `data`: KebabPlaceItem,
    val message: String,
    val status: Boolean,
)
