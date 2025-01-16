package com.example.kebabapp.utilities

import com.example.kebabapp.KebabPlaceDetailItem

data class KebabDetailResponse(
    val `data`: KebabPlaceDetailItem,
    val message: String,
    val status: Boolean,
)
