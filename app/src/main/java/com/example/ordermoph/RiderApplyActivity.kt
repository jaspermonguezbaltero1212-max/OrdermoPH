package com.example.ordermoph

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class RiderApplyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rider_apply)

        val btnSubmit = findViewById<AppCompatButton>(R.id.btnSubmitRiderApply)
        btnSubmit.setOnClickListener {
            // Mock submission
            Toast.makeText(this, "Application Submitted! Thank you for joining our fleet.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
