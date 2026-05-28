package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LocationActivity : AppCompatActivity() {

    private lateinit var firestoreManager: FirestoreManager
    private val merchantList = mutableListOf<Merchant>()
    private lateinit var adapter: ArrayAdapter<Merchant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        firestoreManager = FirestoreManager()
        val selectedPlace = intent.getStringExtra("SELECTED_PLACE") ?: ""
        val selectedRegion = intent.getStringExtra("AREA") ?: "Zambales"
        val userName = intent.getStringExtra("USER_NAME") ?: "User"

        findViewById<TextView>(R.id.tvRegionTitle).text = "Stores in $selectedPlace"
        findViewById<TextView>(R.id.tvUserName).text = userName

        val ivHeader = findViewById<ImageView>(R.id.ivLocationHeader)
        when (selectedRegion) {
            "Zambales" -> ivHeader.setImageResource(R.drawable.bg_green_rounded)
            "Bataan" -> ivHeader.setImageResource(R.drawable.bg_black_rounded)
            "Bulacan" -> ivHeader.setImageResource(R.drawable.bg_black_rounded)
            "Pampanga" -> ivHeader.setImageResource(R.drawable.bg_green_rounded)
        }

        val userToggle = findViewById<View>(R.id.userToggle)
        userToggle.setOnClickListener { showSignOutMenu(it) }

        val lvMerchants = findViewById<ListView>(R.id.lvMerchants)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, merchantList)
        lvMerchants.adapter = adapter

        firestoreManager.getMerchantsByRegion(selectedPlace) { merchants ->
            if (merchants.isNotEmpty()) {
                merchantList.clear()
                merchantList.addAll(merchants)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "No stores in $selectedPlace yet", Toast.LENGTH_SHORT).show()
            }
        }

        lvMerchants.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val merchant = merchantList[position]
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("MERCHANT_ID", merchant.id)
            intent.putExtra("MERCHANT_NAME", merchant.name)
            intent.putExtra("LOCATION", intent.getStringExtra("SELECTED_PLACE") ?: "")
            intent.putExtra("USER_ID", intent.getStringExtra("USER_ID") ?: "")
            startActivity(intent)
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
}
