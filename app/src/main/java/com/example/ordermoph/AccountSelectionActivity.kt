package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class
AccountSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_selection)

        val btnCustomer = findViewById<AppCompatButton>(R.id.btnRoleCustomer)
        val btnMerchant = findViewById<AppCompatButton>(R.id.btnRoleMerchant)
        val btnRider = findViewById<AppCompatButton>(R.id.btnRoleRider)
        val tvMerchantApply = findViewById<TextView>(R.id.tvMerchantApply)
        val tvRiderApply = findViewById<TextView>(R.id.tvRiderApply)

        btnCustomer.setOnClickListener {
            startLoginActivity("customer")
        }

        btnMerchant.setOnClickListener {
            startLoginActivity("merchant")
        }

        btnRider.setOnClickListener {
            startLoginActivity("rider")
        }

        tvMerchantApply.setOnClickListener {
            // Navigate to Merchant Application Screen (would need a new activity)
            startActivity(Intent(this, MerchantApplyActivity::class.java))
        }

        tvRiderApply.setOnClickListener {
            // Navigate to Rider Application Screen (would need a new activity)
            startActivity(Intent(this, RiderApplyActivity::class.java))
        }
    }

    private fun startLoginActivity(role: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("TARGET_ROLE", role)
        startActivity(intent)
    }
}
