package com.example.ordermoph

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MerchantApplyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_apply)

        val btnSubmit = findViewById<AppCompatButton>(R.id.btnSubmitMerchantApply)
        btnSubmit.setOnClickListener {
            // Mock submission
            Toast.makeText(this, "Application Submitted! We will contact you soon.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
