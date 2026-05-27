package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class TrackingActivity : AppCompatActivity() {

    private lateinit var firestoreManager: FirestoreManager
    private lateinit var tvStatus: TextView
    private lateinit var tvDescription: TextView
    private lateinit var pbTracking: ProgressBar
    private lateinit var btnBack: AppCompatButton
    private var cleanup: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        firestoreManager = FirestoreManager()
        val orderId = intent.getStringExtra("ORDER_ID") ?: return
        val merchantName = intent.getStringExtra("MERCHANT_NAME") ?: "Store"

        val userName = intent.getStringExtra("USER_NAME") ?: "Customer"
        findViewById<TextView>(R.id.tvUserName).text = userName
        findViewById<View>(R.id.userToggle).setOnClickListener { showSignOutMenu(it) }

        findViewById<TextView>(R.id.tvTrackingMerchant).text = merchantName
        tvStatus = findViewById(R.id.tvTrackingStatus)
        tvDescription = findViewById(R.id.tvTrackingDescription)
        pbTracking = findViewById(R.id.pbTracking)
        btnBack = findViewById(R.id.btnBackToHome)

        btnBack.setOnClickListener { finish() }

        firestoreManager.listenToOrder(orderId, { order ->
            if (order != null) updateUI(order.status)
        }) { poller -> cleanup = poller }
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

    override fun onDestroy() {
        super.onDestroy()
        cleanup?.let { Handler(Looper.getMainLooper()).removeCallbacks(it) }
    }

    private fun updateUI(status: String) {
        when (status) {
            "pending" -> {
                tvStatus.text = "Status: Pending"
                tvDescription.text = "Waiting for merchant to accept..."
                pbTracking.progress = 20
            }
            "accepted" -> {
                tvStatus.text = "Status: Preparing"
                tvDescription.text = "The merchant is preparing your food."
                pbTracking.progress = 40
            }
            "picking_up" -> {
                tvStatus.text = "Status: Picking Up"
                tvDescription.text = "A rider is at the store to pick up your order."
                pbTracking.progress = 60
            }
            "delivering" -> {
                tvStatus.text = "Status: Delivering"
                tvDescription.text = "Your rider is on the way to you!"
                pbTracking.progress = 80
            }
            "completed" -> {
                tvStatus.text = "Status: Delivered"
                tvDescription.text = "Enjoy your meal!"
                pbTracking.progress = 100
                btnBack.visibility = View.VISIBLE
            }
            "cancelled" -> {
                tvStatus.text = "Status: Cancelled"
                tvDescription.text = "This order was cancelled."
                pbTracking.progress = 0
                btnBack.visibility = View.VISIBLE
            }
        }
    }
}
