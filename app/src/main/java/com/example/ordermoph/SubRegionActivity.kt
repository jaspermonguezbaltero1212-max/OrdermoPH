package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SubRegionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_region)

        val province = intent.getStringExtra("SELECTED_REGION") ?: "Zambales"
        val userName = intent.getStringExtra("USER_NAME") ?: "User"

        findViewById<TextView>(R.id.tvUserName).text = userName
        findViewById<TextView>(R.id.tvSubRegionTitle).text = "Towns in $province"

        val ivHeader = findViewById<ImageView>(R.id.ivProvinceHeader)
        when (province) {
            "Zambales" -> ivHeader.setImageResource(R.drawable.bg_green_rounded)
            "Bataan" -> ivHeader.setImageResource(R.drawable.bg_black_rounded)
            "Bulacan" -> ivHeader.setImageResource(R.drawable.bg_black_rounded)
            "Pampanga" -> ivHeader.setImageResource(R.drawable.bg_green_rounded)
        }

        val userToggle = findViewById<View>(R.id.userToggle)
        userToggle.setOnClickListener { showSignOutMenu(it) }

        val places = when (province) {
            "Zambales" -> listOf("Castillejos", "Iba", "Olongapo City", "San Marcelino", "Subic", "Subic Bay Freeport")
            "Bataan" -> listOf("Abucay", "Bagac", "Balanga City", "Dinalupihan", "Hermosa", "Limay", "Mariveles", "Morong", "Orani", "Orion", "Pilar", "Samal")
            "Bulacan" -> listOf("Balagtas", "Baliwag City", "Angat", "Bustos", "Malolos City", "Meycauayan City", "San Jose del Monte", "Marilao", "Pulilan", "San Ildefonso", "San Rafael", "Santa Maria")
            "Pampanga" -> listOf("Candaba", "Floridablanca", "Guagua", "Lubao")
            else -> emptyList()
        }

        val listView = findViewById<ListView>(R.id.lvSubRegions)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, places)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPlace = places[position]
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("MERCHANT_NAME", "$selectedPlace, $province")
            intent.putExtra("LOCATION", selectedPlace)
            intent.putExtra("AREA", province)
            intent.putExtra("USER_ID", intent.getStringExtra("USER_ID") ?: "")
            intent.putExtra("USER_NAME", intent.getStringExtra("USER_NAME") ?: "Customer")
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
