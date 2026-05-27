package com.example.ordermoph

data class Merchant(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val type: String = ""
) {
    override fun toString(): String {
        return "$name - $location ($type)"
    }
}
