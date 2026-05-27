package com.example.ordermoph

data class Order(
    val orderId: String = "",
    val customerId: String = "",
    val merchantId: String = "",
    val riderId: String? = null,
    val merchantName: String = "",
    val customerName: String = "",
    val deliveryAddress: String = "",
    val deliveryArea: String = "", // Zambales, Bataan, Bulacan, Pampanga
    val items: List<Product> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: String = "pending", // pending, accepted, picking_up, delivering, completed
    val paymentMethod: String = "Cash on Delivery",
    val timestamp: Long = System.currentTimeMillis(),
    val source: String = "Android"
)
