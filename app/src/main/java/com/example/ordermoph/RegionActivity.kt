package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class RegionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_region)

        val userName = intent.getStringExtra("USER_NAME") ?: "User"
        findViewById<TextView>(R.id.tvUserName).text = userName

        val userToggle = findViewById<View>(R.id.userToggle)
        userToggle.setOnClickListener { showSignOutMenu(it) }

        val btnZambales = findViewById<View>(R.id.btnRegionZambales)
        val btnBataan = findViewById<View>(R.id.btnRegionBataan)
        val btnBulacan = findViewById<View>(R.id.btnRegionBulacan)
        val btnPampanga = findViewById<View>(R.id.btnRegionPampanga)

        btnZambales.setOnClickListener { navigateToSubRegion("Zambales") }
        btnBataan.setOnClickListener { navigateToSubRegion("Bataan") }
        btnBulacan.setOnClickListener { navigateToSubRegion("Bulacan") }
        btnPampanga.setOnClickListener { navigateToSubRegion("Pampanga") }
    }

    private fun navigateToSubRegion(region: String) {
        val intent = Intent(this, SubRegionActivity::class.java)
        intent.putExtra("SELECTED_REGION", region)
        intent.putExtra("USER_NAME", intent.getStringExtra("USER_NAME") ?: "User")
        intent.putExtra("USER_ID", intent.getStringExtra("USER_ID") ?: "")
        startActivity(intent)
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
}
