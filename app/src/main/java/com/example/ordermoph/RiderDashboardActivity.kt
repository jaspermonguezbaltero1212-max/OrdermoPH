package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class RiderDashboardActivity : AppCompatActivity() {

    private lateinit var firestoreManager: FirestoreManager
    private lateinit var lvOrders: ListView
    private val ordersList = mutableListOf<Order>()
    private lateinit var adapter: RiderOrderAdapter
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rider_dashboard)

        val userName = intent.getStringExtra("USER_NAME") ?: "Rider"
        userId = intent.getStringExtra("USER_ID") ?: ""
        val serviceArea = intent.getStringExtra("SERVICE_AREA") ?: ""
        findViewById<TextView>(R.id.tvUserName).text = userName

        val userToggle = findViewById<View>(R.id.userToggle)
        userToggle.setOnClickListener { showSignOutMenu(it) }

        val tvArea = findViewById<TextView>(R.id.tvRiderArea)
        tvArea.text = "Delivery Area: $serviceArea"

        firestoreManager = FirestoreManager()
        lvOrders = findViewById(R.id.lvRiderOrders)

        adapter = RiderOrderAdapter(ordersList) { order ->
            handleOrderAction(order)
        }
        lvOrders.adapter = adapter

        firestoreManager.listenToOrders("rider", userId) { updatedOrders ->
            ordersList.clear()
            ordersList.addAll(updatedOrders.filter {
                it.deliveryArea == serviceArea && (it.status == "accepted" || it.riderId == userId)
            })
            adapter.notifyDataSetChanged()
        }
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

    private fun handleOrderAction(order: Order) {
        when (order.status) {
            "accepted" -> {
                firestoreManager.assignRider(order.orderId, userId) { success ->
                    if (success) Toast.makeText(this, "Order picked up! Start delivery.", Toast.LENGTH_SHORT).show()
                }
            }
            "picking_up" -> {
                startDelivery(order.orderId)
            }
            "delivering" -> {
                completeDelivery(order.orderId)
            }
        }
    }

    private fun startDelivery(orderId: String) {
        firestoreManager.startDelivery(orderId) { success ->
            if (success) Toast.makeText(this, "Delivering order...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun completeDelivery(orderId: String) {
        firestoreManager.completeDelivery(orderId) { success ->
            if (success) Toast.makeText(this, "Order delivered!", Toast.LENGTH_SHORT).show()
        }
    }

    inner class RiderOrderAdapter(
        private val orders: List<Order>,
        private val onAction: (Order) -> Unit
    ) : BaseAdapter() {

        override fun getCount(): Int = orders.size
        override fun getItem(position: Int): Any = orders[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_rider_order, parent, false)

            val order = orders[position]
            val tvMerchant = view.findViewById<TextView>(R.id.tvOrderItemMerchant)
            val tvStatus = view.findViewById<TextView>(R.id.tvOrderItemStatus)
            val tvTotal = view.findViewById<TextView>(R.id.tvOrderItemTotal)
            val tvAddress = view.findViewById<TextView>(R.id.tvDeliveryAddress)
            val btnAction = view.findViewById<AppCompatButton>(R.id.btnOrderAction)

            tvMerchant.text = order.merchantName
            tvStatus.text = "Status: ${order.status.uppercase()}"
            tvTotal.text = "Total: ₱${order.totalAmount}"
            tvAddress.text = "Deliver to: ${order.customerName} @ ${order.deliveryAddress}"

            btnAction.text = when (order.status) {
                "accepted" -> "Pick Up Order"
                "picking_up" -> "Start Delivery"
                "delivering" -> "Complete Delivery"
                else -> "In Progress"
            }

            btnAction.setOnClickListener { onAction(order) }

            return view
        }
    }
}
