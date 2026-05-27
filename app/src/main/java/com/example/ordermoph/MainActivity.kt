package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var firestoreManager: FirestoreManager
    private var targetRole: String = "customer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestoreManager = FirestoreManager()
        targetRole = intent.getStringExtra("TARGET_ROLE") ?: "customer"

        findViewById<View>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, AccountSelectionActivity::class.java))
            finish()
        }

        val etPhone = findViewById<EditText>(R.id.etPhoneNumber)
        val btnContinue = findViewById<AppCompatButton>(R.id.btnContinuePhone)

        btnContinue.setOnClickListener {
            val phone = etPhone.text.toString().trim()
            if (phone.length < 10) {
                Toast.makeText(this, "Enter a valid PH mobile number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnContinue.isEnabled = false
            btnContinue.text = "Checking..."

            val uid = "63$phone"

            firestoreManager.getUser(uid) { profile ->
                if (profile != null) {
                    redirectToDashboard(uid, profile.name, profile.role, profile.serviceArea)
                } else {
                    val intent = Intent(this, OtpActivity::class.java)
                    intent.putExtra("VERIFICATION_ID", UUID.randomUUID().toString())
                    intent.putExtra("PHONE_NUMBER", "+63$phone")
                    intent.putExtra("TARGET_ROLE", targetRole)
                    startActivity(intent)
                }
            }
        }
    }

    private fun redirectToDashboard(uid: String, name: String, role: String, serviceArea: String = "") {
        val intent = when (role) {
            "merchant" -> Intent(this, MerchantDashboardActivity::class.java)
            "rider" -> Intent(this, RiderDashboardActivity::class.java)
            else -> Intent(this, RegionActivity::class.java)
        }
        intent.putExtra("USER_NAME", name)
        intent.putExtra("USER_ID", uid)
        if (role == "rider" || role == "merchant") intent.putExtra("SERVICE_AREA", serviceArea)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
