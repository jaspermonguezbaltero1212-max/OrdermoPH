package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MerchantDashboardActivity : AppCompatActivity() {

    private lateinit var firestoreManager: FirestoreManager
    private val ordersList = mutableListOf<Order>()
    private lateinit var adapter: MerchantOrderAdapter
    private var userId: String = ""
    private var serviceArea: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_dashboard)

        val userName = intent.getStringExtra("USER_NAME") ?: "Merchant"
        userId = intent.getStringExtra("USER_ID") ?: ""
        serviceArea = intent.getStringExtra("SERVICE_AREA") ?: ""
        findViewById<TextView>(R.id.tvUserName).text = userName

        val ivBg = findViewById<ImageView>(R.id.ivMerchantBg)
        when (serviceArea) {
            "Zambales" -> ivBg.setImageResource(R.drawable.bg_green_rounded)
            "Bataan" -> ivBg.setImageResource(R.drawable.bg_black_rounded)
            "Bulacan" -> ivBg.setImageResource(R.drawable.bg_black_rounded)
            "Pampanga" -> ivBg.setImageResource(R.drawable.bg_green_rounded)
        }

        val userToggle = findViewById<View>(R.id.userToggle)
        userToggle.setOnClickListener { showSignOutMenu(it) }

        val tvArea = findViewById<TextView>(R.id.tvMerchantArea)
        tvArea.text = "Service Area: $serviceArea"

        firestoreManager = FirestoreManager()
        val lvOrders = findViewById<ListView>(R.id.lvMerchantOrders)

        val btnAddFood = findViewById<AppCompatButton>(R.id.btnAddFood)
        btnAddFood.setOnClickListener { showAddFoodDialog() }

        adapter = MerchantOrderAdapter(ordersList)
        lvOrders.adapter = adapter

        firestoreManager.listenToOrders("merchant", userId) { updatedOrders ->
            ordersList.clear()
            ordersList.addAll(updatedOrders.filter {
                it.status == "pending" && it.deliveryArea == serviceArea
            })
            adapter.notifyDataSetChanged()
        }
    }

    private fun showAddFoodDialog() {
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_food, null)
        builder.setView(view)
            .setTitle("Add Food Item")
            .setPositiveButton("Add") { _, _ ->
                val name = view.findViewById<EditText>(R.id.etFoodName).text.toString().trim()
                val desc = view.findViewById<EditText>(R.id.etFoodDesc).text.toString().trim()
                val priceStr = view.findViewById<EditText>(R.id.etFoodPrice).text.toString().trim()
                if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val price = priceStr.toDoubleOrNull() ?: 0.0
                val product = Product(name = name, description = desc, price = price, rating = 0.0)
                firestoreManager.addProduct(userId, product) { success ->
                    if (success) Toast.makeText(this, "$name added to menu!", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSignOutMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menu.add("Sign Out")
        popup.setOnMenuItemClickListener {
            val intent = Intent(this, AccountSelectionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            true
        }
        popup.show()
    }

    inner class MerchantOrderAdapter(private val orders: List<Order>) : BaseAdapter() {
        override fun getCount(): Int = orders.size
        override fun getItem(position: Int): Any = orders[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_merchant_order, parent, false)

            val order = orders[position]
            val tvOrderId = view.findViewById<TextView>(R.id.tvMerchantOrderItemId)
            val tvSummary = view.findViewById<TextView>(R.id.tvMerchantOrderSummary)
            val tvTotal = view.findViewById<TextView>(R.id.tvMerchantOrderTotal)
            val tvCustomer = view.findViewById<TextView>(R.id.tvCustomerName)
            val btnAccept = view.findViewById<AppCompatButton>(R.id.btnMerchantAccept)

            tvOrderId.text = "Order #${order.orderId.takeLast(5)}"
            tvSummary.text = order.items.groupBy { it.id }.map { "${it.value.size}x ${it.value[0].name}" }.joinToString(", ")
            tvTotal.text = "Total: ₱${order.totalAmount}"
            tvCustomer.text = "Customer: ${order.customerName}"

            val btnDecline = view.findViewById<AppCompatButton>(R.id.btnMerchantDecline)
            btnAccept.text = "Approve"
            btnAccept.setOnClickListener {
                firestoreManager.acceptOrder(order.orderId, userId) { success ->
                    if (success) Toast.makeText(parent?.context, "Sending to Rider From $serviceArea", Toast.LENGTH_SHORT).show()
                }
            }
            btnDecline.setOnClickListener {
                firestoreManager.declineOrder(order.orderId) { success ->
                    if (success) Toast.makeText(parent?.context, "Order declined.", Toast.LENGTH_SHORT).show()
                }
            }

            return view
        }
    }
}
