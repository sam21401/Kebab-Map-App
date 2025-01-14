package com.example.kebabapp.utilities

data class LoginResponse(
    val token: String,
    val message: String,
    val status: Boolean,
)
