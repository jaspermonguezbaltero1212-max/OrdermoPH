package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import java.util.UUID

class RegistrationActivity : AppCompatActivity() {

    private lateinit var firestoreManager: FirestoreManager
    private var selectedRole: String = "customer"
    private var selectedArea: String = "Zambales"
    private var existingUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        firestoreManager = FirestoreManager()
        selectedRole = intent.getStringExtra("TARGET_ROLE") ?: "customer"
        existingUid = intent.getStringExtra("USER_ID")

        val rbCustomer = findViewById<RadioButton>(R.id.rbCustomer)
        val rbMerchant = findViewById<RadioButton>(R.id.rbMerchant)
        val rbRider = findViewById<RadioButton>(R.id.rbRider)

        when (selectedRole) {
            "merchant" -> rbMerchant.isChecked = true
            "rider" -> rbRider.isChecked = true
            else -> rbCustomer.isChecked = true
        }

        val tvSelectedRole = findViewById<TextView>(R.id.tvSelectedRole)
        val layoutServiceArea = findViewById<View>(R.id.layoutServiceArea)
        tvSelectedRole.text = "You are registering as ${selectedRole.uppercase()}"
        layoutServiceArea.visibility = if (selectedRole == "rider" || selectedRole == "merchant") View.VISIBLE else View.GONE

        val rgRole = findViewById<RadioGroup>(R.id.rgRole)
        rgRole.setOnCheckedChangeListener { _, checkedId ->
            selectedRole = when (checkedId) {
                R.id.rbMerchant -> "merchant"
                R.id.rbRider -> "rider"
                else -> "customer"
            }
            tvSelectedRole.text = "You are registering as ${selectedRole.uppercase()}"
            layoutServiceArea.visibility = if (selectedRole == "rider" || selectedRole == "merchant") View.VISIBLE else View.GONE
        }

        val rgServiceArea = findViewById<RadioGroup>(R.id.rgServiceArea)
        rgServiceArea.setOnCheckedChangeListener { _, checkedId ->
            selectedArea = when (checkedId) {
                R.id.rbAreaZambales -> "Zambales"
                R.id.rbAreaBataan -> "Bataan"
                R.id.rbAreaBulacan -> "Bulacan"
                R.id.rbAreaPampanga -> "Pampanga"
                else -> "Zambales"
            }
        }

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val btnRegister = findViewById<AppCompatButton>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val name = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnRegister.isEnabled = false
            btnRegister.text = "Confirming..."

            val uid = existingUid ?: UUID.randomUUID().toString()
            val profile = UserProfile(
                uid = uid,
                name = name,
                email = email,
                phone = phone,
                address = address,
                role = selectedRole,
                serviceArea = if (selectedRole == "rider" || selectedRole == "merchant") selectedArea else ""
            )

            firestoreManager.saveUser(profile) { success ->
                if (success) {
                    redirectToDashboard(uid, name)
                } else {
                    btnRegister.isEnabled = true
                    btnRegister.text = "Confirm & Continue"
                    Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun redirectToDashboard(uid: String, name: String) {
        val intent = when (selectedRole) {
            "merchant" -> Intent(this, MerchantDashboardActivity::class.java)
            "rider" -> Intent(this, RiderDashboardActivity::class.java)
            else -> Intent(this, RegionActivity::class.java)
        }
        intent.putExtra("USER_NAME", name)
        intent.putExtra("USER_ID", uid)
        if (selectedRole == "rider" || selectedRole == "merchant") intent.putExtra("SERVICE_AREA", selectedArea)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
