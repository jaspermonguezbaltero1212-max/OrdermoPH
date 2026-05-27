package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class OrderActivity : AppCompatActivity() {

    private lateinit var firestoreManager: FirestoreManager
    private var customerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        firestoreManager = FirestoreManager()
        customerId = intent.getStringExtra("USER_ID") ?: "anonymous"

        val tvMerchant = findViewById<TextView>(R.id.tvCheckoutMerchant)
        val tvDetails = findViewById<TextView>(R.id.tvOrderDetails)
        val tvTotal = findViewById<TextView>(R.id.tvCheckoutTotal)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val rgPayment = findViewById<RadioGroup>(R.id.rgPayment)
        val btnPlaceOrder = findViewById<AppCompatButton>(R.id.btnPlaceOrder)

        val merchantId = intent.getStringExtra("MERCHANT_ID") ?: ""
        val merchantName = intent.getStringExtra("MERCHANT_NAME") ?: "Store"
        val deliveryArea = intent.getStringExtra("AREA") ?: ""
        val cartItems = intent.getSerializableExtra("CART_ITEMS") as? ArrayList<Product> ?: arrayListOf()

        tvMerchant.text = merchantName

        val summaryText = cartItems.groupBy { it.id }.map { (_, items) ->
            "${items.size}x ${items[0].name} - ₱${items.sumOf { it.price }}"
        }.joinToString("\n")

        tvDetails.text = summaryText
        val total = cartItems.sumOf { it.price }
        tvTotal.text = "₱$total"

        btnPlaceOrder.setOnClickListener {
            val address = etAddress.text.toString()
            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter delivery address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val paymentId = rgPayment.checkedRadioButtonId
            val paymentMethod = if (paymentId == R.id.rbCod) "Cash on Delivery" else "GCash / Online Payment"

            firestoreManager.getUser(customerId) { profile ->
                val customerName = profile?.name ?: "Customer"
                val order = Order(
                    customerId = customerId,
                    merchantId = merchantId,
                    merchantName = merchantName,
                    customerName = customerName,
                    deliveryAddress = address,
                    deliveryArea = deliveryArea,
                    items = cartItems,
                    totalAmount = total,
                    status = "pending",
                    paymentMethod = paymentMethod,
                    source = "Android"
                )

                firestoreManager.placeOrder(order) { success, orderId ->
                    if (success && orderId != null) {
                        Toast.makeText(this@OrderActivity, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@OrderActivity, TrackingActivity::class.java)
                        intent.putExtra("ORDER_ID", orderId)
                        intent.putExtra("MERCHANT_NAME", merchantName)
                        intent.putExtra("USER_NAME", intent.getStringExtra("USER_NAME") ?: "Customer")
                        intent.putExtra("USER_ID", customerId)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@OrderActivity, "Error placing order", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
