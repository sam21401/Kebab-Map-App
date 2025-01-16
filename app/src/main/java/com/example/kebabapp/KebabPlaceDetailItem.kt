package com.example.kebabapp

data class KebabPlaceDetailItem(
    val created_at: Any,
    val id: Int,
    val is_chain_member: Int,
    val is_craft: Int,
    val is_in_stall: Int,
    val kebab_id: Int,
    val meat_types: List<String>,
    val opening_hours: OpeningHours,
    val ordering_options: List<String>,
    val sauces: List<String>,
    val status: String,
    val updated_at: Any,
)
