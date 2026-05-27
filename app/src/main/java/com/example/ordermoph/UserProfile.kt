package com.example.ordermoph

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val role: String = "customer", // customer, merchant, rider
    val email: String = "",
    val address: String = "",
    val serviceArea: String = "" // Zambales, Bataan, Bulacan, Pampanga (for riders)
)
