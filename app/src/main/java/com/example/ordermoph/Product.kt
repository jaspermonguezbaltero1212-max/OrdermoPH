package com.example.ordermoph

import java.io.Serializable

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val imageUrl: String? = null
) : Serializable {
    override fun toString(): String {
        return "$name - ₱$price"
    }
}
