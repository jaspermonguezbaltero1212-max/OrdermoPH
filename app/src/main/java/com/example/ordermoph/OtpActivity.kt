package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class OtpActivity : AppCompatActivity() {

    private lateinit var firestoreManager: FirestoreManager
    private var targetRole: String = "customer"
    private var phoneNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        firestoreManager = FirestoreManager()
        phoneNumber = intent.getStringExtra("PHONE_NUMBER") ?: ""
        targetRole = intent.getStringExtra("TARGET_ROLE") ?: "customer"

        val displayPhone = if (phoneNumber.length >= 10) {
            "+63 *** *** ${phoneNumber.takeLast(4)}"
        } else {
            "+63 XXX XXX XXXX"
        }

        findViewById<TextView>(R.id.tvOtpPhone).text = displayPhone

        val etOtp = arrayOf<EditText>(
            findViewById(R.id.etOtp1), findViewById(R.id.etOtp2),
            findViewById(R.id.etOtp3), findViewById(R.id.etOtp4),
            findViewById(R.id.etOtp5), findViewById(R.id.etOtp6)
        )

        for (i in etOtp.indices) {
            etOtp[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < 5) {
                        etOtp[i + 1].requestFocus()
                    }
                }
            })

            etOtp[i].setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && etOtp[i].text.isEmpty() && i > 0) {
                    etOtp[i - 1].requestFocus()
                    etOtp[i - 1].setText("")
                }
                false
            })
        }

        findViewById<AppCompatButton>(R.id.btnSubmitOtp).setOnClickListener {
            val code = etOtp.map { it.text.toString() }.joinToString("")
            if (code.length == 6) {
                proceed()
            } else {
                Toast.makeText(this, "Enter all 6 digits", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.tvResendOtp).setOnClickListener {
            Toast.makeText(this, "Code resent!", Toast.LENGTH_SHORT).show()
        }

        etOtp[0].requestFocus()
    }

    private fun proceed() {
        findViewById<AppCompatButton>(R.id.btnSubmitOtp).isEnabled = false
        findViewById<AppCompatButton>(R.id.btnSubmitOtp).text = "Verifying..."

        val uid = phoneNumber.replace("+", "")

        firestoreManager.getUser(uid) { profile ->
            if (profile != null) {
                redirectToDashboard(uid, profile.name, profile.role)
            } else {
                goToRegistration(uid)
            }
        }
    }

    private fun redirectToDashboard(uid: String, name: String, role: String) {
        val intent = when (role) {
            "merchant" -> Intent(this, MerchantDashboardActivity::class.java)
            "rider" -> Intent(this, RiderDashboardActivity::class.java)
            else -> Intent(this, RegionActivity::class.java)
        }
        intent.putExtra("USER_NAME", name)
        intent.putExtra("USER_ID", uid)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun goToRegistration(uid: String) {
        val intent = Intent(this, RegistrationActivity::class.java)
        intent.putExtra("TARGET_ROLE", targetRole)
        intent.putExtra("USER_ID", uid)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
