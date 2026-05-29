package com.example.ordermoph

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MenuActivity : AppCompatActivity() {

    private val cartItems = mutableListOf<Product>()
    private val productList = mutableListOf<Product>()
    private lateinit var btnViewCart: AppCompatButton
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val tvMerchantName = findViewById<TextView>(R.id.tvMerchantName)
        val lvProducts = findViewById<ListView>(R.id.lvProducts)
        btnViewCart = findViewById(R.id.btnViewCart)

        val merchantName = intent.getStringExtra("MERCHANT_NAME") ?: "Store"
        val location = intent.getStringExtra("LOCATION") ?: ""
        tvMerchantName.text = merchantName

        productList.addAll(getTownMenu(location))

        adapter = ProductAdapter(productList) { product ->
            cartItems.add(product)
            updateCartButton()
            Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
        }
        lvProducts.adapter = adapter

        btnViewCart.setOnClickListener {
            if (cartItems.isNotEmpty()) {
                val intent = Intent(this, OrderActivity::class.java)
                intent.putExtra("MERCHANT_ID", intent.getStringExtra("MERCHANT_ID") ?: "")
                intent.putExtra("MERCHANT_NAME", merchantName)
                intent.putExtra("CART_ITEMS", ArrayList(cartItems))
                intent.putExtra("USER_ID", intent.getStringExtra("USER_ID") ?: "")
                intent.putExtra("USER_NAME", intent.getStringExtra("USER_NAME") ?: "Customer")
                intent.putExtra("AREA", intent.getStringExtra("AREA") ?: "")
                startActivity(intent)
            } else {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            }
        }

        updateCartButton()
    }

    private fun updateCartButton() {
        val total = cartItems.sumOf { it.price }
        btnViewCart.text = "View Cart (${cartItems.size}) - ₱$total"
    }

    private fun getTownMenu(town: String): List<Product> {
        val menus = mapOf(
            "Castillejos" to listOf(
                Product("z1", "Grilled Tanigue", "Fresh Spanish mackerel grilled to perfection ★ BEST SELLER", 220.0, 4.9, null),
                Product("z2", "Sinaing na Tulingan", "Tuna slow-cooked in salt & vinegar ★ BEST SELLER", 185.0, 4.8, null),
                Product("z3", "Kinunot na Pating", "Shark meat in creamy coconut milk ★ BEST SELLER", 195.0, 4.7, null),
                Product("z4", "Adobong Pusit", "Squid simmered in soy-vinegar sauce", 175.0, 4.6, null),
                Product("z5", "Grilled Bangus", "Stuffed milkfish grilled over charcoal", 200.0, 4.7, null),
                Product("z6", "Seafood Kare-Kare", "Mixed seafood in peanut sauce", 250.0, 4.5, null),
                Product("z7", "Laing", "Taro leaves cooked in spiced coconut milk", 140.0, 4.4, null),
                Product("z8", "Crispy Pata", "Deep-fried pork knuckle served crispy", 280.0, 4.8, null),
                Product("z9", "Chicharon Bulaklak", "Crispy fried pork ruffle fat", 160.0, 4.5, null),
                Product("z10", "Zambales Halo-Halo", "Shaved ice with local fruits & leche flan", 110.0, 4.6, null),
            ),
            "Iba" to listOf(
                Product("i1", "Iba's Famous Grilled Pla-Pla", "Fresh pla-pla fish stuffed with tomatoes ★ BEST SELLER", 230.0, 4.9, null),
                Product("i2", "Tinolang Manok sa Tanglad", "Chicken ginger soup with lemongrass ★ BEST SELLER", 175.0, 4.8, null),
                Product("i3", "Paksiw na Isda", "Fish cooked in vinegar & ginger ★ BEST SELLER", 150.0, 4.7, null),
                Product("i4", "Inihaw na Liempo", "Grilled pork belly with special marinade", 185.0, 4.7, null),
                Product("i5", "Ginisang Munggo", "Sauteed mung beans with shrimp", 130.0, 4.4, null),
                Product("i6", "Pritong Tilapia", "Crispy fried tilapia with sawsawan", 165.0, 4.5, null),
                Product("i7", "Bicol Express", "Pork in spicy coconut cream", 190.0, 4.6, null),
                Product("i8", "Ensaladang Talong", "Grilled eggplant salad with tomatoes", 95.0, 4.3, null),
                Product("i9", "Leche Flan", "Creamy caramel custard dessert", 80.0, 4.8, null),
                Product("i10", "Sago't Gulaman", "Refreshing sago and gelatin drink", 55.0, 4.5, null),
            ),
            "Olongapo City" to listOf(
                Product("o1", "Sizzling Sisig", "Kapampangan-style sizzling pork sisig ★ BEST SELLER", 195.0, 4.9, null),
                Product("o2", "Beef Caldereta", "Spicy tomato-based beef stew ★ BEST SELLER", 225.0, 4.8, null),
                Product("o3", "Pancit Canton", "Stir-fried egg noodles with meats ★ BEST SELLER", 150.0, 4.7, null),
                Product("o4", "Chicken Inasal", "Grilled marinated chicken half", 180.0, 4.7, null),
                Product("o5", "Sinigang na Baboy", "Sour tamarind pork soup", 170.0, 4.6, null),
                Product("o6", "Lumpiang Shanghai", "Crispy spring rolls (15 pcs)", 140.0, 4.7, null),
                Product("o7", "Dinuguan", "Savory pork blood stew with puto", 135.0, 4.5, null),
                Product("o8", "Palabok", "Rice noodles with shrimp & pork sauce", 150.0, 4.5, null),
                Product("o9", "Ginataang Langka", "Young jackfruit in coconut milk", 120.0, 4.3, null),
                Product("o10", "Suman sa Lihiya", "Glutinous rice cakes with latik", 70.0, 4.6, null),
            ),
            "San Marcelino" to listOf(
                Product("sm1", "Bagnet", "Crispy deep-fried pork belly Ilocano-style ★ BEST SELLER", 210.0, 4.9, null),
                Product("sm2", "Pinakbet", "Mixed vegetables in shrimp paste ★ BEST SELLER", 145.0, 4.7, null),
                Product("sm3", "Daing na Bangus", "Marinated fried milkfish ★ BEST SELLER", 175.0, 4.8, null),
                Product("sm4", "Igado", "Pork liver and kidney stew", 165.0, 4.5, null),
                Product("sm5", "Pinaputok na Tilapia", "Stuffed fried tilapia", 185.0, 4.6, null),
                Product("sm6", "Ginataang Alimasag", "Crabs cooked in coconut milk", 220.0, 4.7, null),
                Product("sm7", "Pork Steak", "Filipino-style beefsteak with onions", 170.0, 4.5, null),
                Product("sm8", "Tortang Giniling", "Ground beef omelette", 130.0, 4.3, null),
                Product("sm9", "Maja Blanca", "Coconut milk pudding with corn", 75.0, 4.6, null),
                Product("sm10", "Taho", "Fresh silken tofu with syrup & sago", 50.0, 4.7, null),
            ),
            "Subic" to listOf(
                Product("su1", "Subic Seafood Platter", "Assorted grilled seafood platter for 2 ★ BEST SELLER", 450.0, 4.9, null),
                Product("su2", "Grilled Tuna Belly", "Thick tuna belly grilled to order ★ BEST SELLER", 260.0, 4.8, null),
                Product("su3", "Crispy Danggit", "Salted dried fish fried crispy ★ BEST SELLER", 140.0, 4.7, null),
                Product("su4", "Sinigang na Hipon", "Sour shrimp soup with vegetables", 190.0, 4.7, null),
                Product("su5", "Adobong Manok", "Classic chicken adobo", 170.0, 4.6, null),
                Product("su6", "Fried Calamares", "Crispy fried squid rings", 165.0, 4.5, null),
                Product("su7", "Ginataang Kuhol", "Snails in spicy coconut cream", 150.0, 4.4, null),
                Product("su8", "Chopsuey", "Stir-fried mixed vegetables", 135.0, 4.3, null),
                Product("su9", "Bananaque", "Caramelized banana skewers", 45.0, 4.6, null),
                Product("su10", "Mais con Hielo", "Shaved ice with sweet corn & milk", 65.0, 4.5, null),
            ),
            "Subic Bay Freeport" to listOf(
                Product("sf1", "US Ribeye Steak", "Grilled US ribeye with mushroom sauce ★ BEST SELLER", 650.0, 4.9, null),
                Product("sf2", "Cajun Grilled Prawns", "Spicy Cajun seasoned prawns ★ BEST SELLER", 380.0, 4.8, null),
                Product("sf3", "Baby Back Ribs", "Slow-cooked BBQ baby back ribs ★ BEST SELLER", 420.0, 4.9, null),
                Product("sf4", "Fish & Chips", "Beer-battered fish with tartar sauce", 250.0, 4.6, null),
                Product("sf5", "Chicken Alfredo", "Creamy pasta with grilled chicken", 220.0, 4.5, null),
                Product("sf6", "Seafood Paella", "Spanish-style seafood rice", 350.0, 4.7, null),
                Product("sf7", "Clubhouse Sandwich", "Triple-decker clubhouse with fries", 180.0, 4.4, null),
                Product("sf8", "Caesar Salad", "Fresh romaine with caesar dressing", 160.0, 4.3, null),
                Product("sf9", "Cheesecake", "New York-style cheesecake", 120.0, 4.7, null),
                Product("sf10", "Iced Mocha Latte", "Chilled coffee with chocolate", 95.0, 4.6, null),
            ),
            "Abucay" to listOf(
                Product("ab1", "Bataan Fried Chicken", "Garlic-marinated crispy fried chicken ★ BEST SELLER", 190.0, 4.8, null),
                Product("ab2", "Pork Sinigang", "Sour tamarind soup with pork ribs ★ BEST SELLER", 175.0, 4.7, null),
                Product("ab3", "Tuyo Fried Rice", "Garlic fried rice with crispy dried fish ★ BEST SELLER", 140.0, 4.7, null),
                Product("ab4", "Adobong Pusit", "Squid adobo with black ink", 180.0, 4.6, null),
                Product("ab5", "Ginisang Munggo", "Sauteed mung beans with chicharon", 125.0, 4.4, null),
                Product("ab6", "Pritong Tilapia", "Crispy fried tilapia", 160.0, 4.5, null),
                Product("ab7", "Tortang Talong", "Eggplant omelette with ground pork", 120.0, 4.4, null),
                Product("ab8", "Ensaladang Mangga", "Green mango salad with bagoong", 90.0, 4.3, null),
                Product("ab9", "Bibingka", "Rice cake with salted egg & cheese", 65.0, 4.6, null),
                Product("ab10", "Ginumis", "Shaved ice with macapuno & saba", 70.0, 4.5, null),
            ),
            "Bagac" to listOf(
                Product("bg1", "Bagac Fried Isda", "Crispy fried whole fish with suka ★ BEST SELLER", 200.0, 4.8, null),
                Product("bg2", "Inihaw na Pusit", "Grilled stuffed squid ★ BEST SELLER", 195.0, 4.8, null),
                Product("bg3", "Pinaupong Manok", "Salted chicken steamed on rock salt ★ BEST SELLER", 220.0, 4.7, null),
                Product("bg4", "Sinigang na Isda", "Fish in sour tamarind soup", 165.0, 4.6, null),
                Product("bg5", "Pork BBQ", "Grilled pork skewers (5 sticks)", 130.0, 4.6, null),
                Product("bg6", "Laing", "Dried taro leaves in coconut milk", 140.0, 4.4, null),
                Product("bg7", "Ginataang Sitaw", "String beans in coconut cream", 110.0, 4.3, null),
                Product("bg8", "Lumpiang Sariwa", "Fresh vegetable spring rolls", 125.0, 4.5, null),
                Product("bg9", "Puto Bumbong", "Purple rice cake with coconut", 55.0, 4.6, null),
                Product("bg10", "Buko Juice", "Fresh young coconut juice", 50.0, 4.7, null),
            ),
            "Balanga City" to listOf(
                Product("bc1", "Tuyo ni Balanga", "Premium tuyo fried to golden crisp ★ BEST SELLER", 150.0, 4.9, null),
                Product("bc2", "Beef Tapa", "Cured beef breakfast with sinangag ★ BEST SELLER", 185.0, 4.8, null),
                Product("bc3", "Chicken Lomi", "Thick egg noodle soup with chicken ★ BEST SELLER", 145.0, 4.7, null),
                Product("bc4", "Lechon Kawali", "Crispy deep-fried pork belly", 200.0, 4.8, null),
                Product("bc5", "Kare-Kare", "Oxtail peanut stew with bagoong", 260.0, 4.7, null),
                Product("bc6", "Pancit Guisado", "Stir-fried canton noodles", 140.0, 4.5, null),
                Product("bc7", "Arroz Caldo", "Chicken rice porridge with egg", 110.0, 4.5, null),
                Product("bc8", "Tokwa't Baboy", "Fried tofu and pork with soy dip", 120.0, 4.4, null),
                Product("bc9", "Turron de Balanga", "Crispy banana spring rolls", 60.0, 4.6, null),
                Product("bc10", "Sago't Gulaman", "Chilled sago and gelatin drink", 45.0, 4.5, null),
            ),
            "Dinalupihan" to listOf(
                Product("dn1", "Dinalupihan Inihaw", "Assorted grilled meat platter ★ BEST SELLER", 350.0, 4.9, null),
                Product("dn2", "Bulalo", "Beef marrow soup with vegetables ★ BEST SELLER", 240.0, 4.8, null),
                Product("dn3", "Pinakbet", "Mixed vegetable stew with bagoong ★ BEST SELLER", 135.0, 4.7, null),
                Product("dn4", "Crispy Pata", "Deep-fried pork knuckle", 290.0, 4.8, null),
                Product("dn5", "Bistek Tagalog", "Beef steak in soy-calamansi sauce", 185.0, 4.6, null),
                Product("dn6", "Tinolang Manok", "Chicken ginger soup", 165.0, 4.5, null),
                Product("dn7", "Pork Sisig", "Sizzling chopped pork face", 180.0, 4.7, null),
                Product("dn8", "Ginataang Alimasag", "Crab in coconut milk", 240.0, 4.5, null),
                Product("dn9", "Palitaw", "Rice cakes with coconut & sugar", 55.0, 4.4, null),
                Product("dn10", "Melon Juice", "Fresh cantaloupe shake", 60.0, 4.6, null),
            ),
            "Hermosa" to listOf(
                Product("he1", "Hermosa Fried Rice", "Special fried rice with shrimp & ham ★ BEST SELLER", 160.0, 4.8, null),
                Product("he2", "Sweet & Sour Fish", "Crispy fried fish in sweet-sour sauce ★ BEST SELLER", 195.0, 4.7, null),
                Product("he3", "Chicken Adobo sa Gata", "Chicken adobo with coconut milk ★ BEST SELLER", 185.0, 4.7, null),
                Product("he4", "Pork Menudo", "Pork stew with potatoes & carrots", 155.0, 4.5, null),
                Product("he5", "Ginisang Ampalaya", "Sauteed bitter melon with egg", 115.0, 4.3, null),
                Product("he6", "Daing na Bisugo", "Marinated fried threadfin bream", 170.0, 4.6, null),
                Product("he7", "Pininyahang Manok", "Pineapple chicken stew", 165.0, 4.5, null),
                Product("he8", "Lumpiang Shanghai", "Fried spring rolls (10 pcs)", 120.0, 4.6, null),
                Product("he9", "Ginataan", "Mixed root crops in coconut milk", 85.0, 4.4, null),
                Product("he10", "Calamansi Juice", "Fresh calamansi lime juice", 40.0, 4.5, null),
            ),
            "Limay" to listOf(
                Product("lm1", "Limay Crispy Bangus", "Boneless bangus fried extra crispy ★ BEST SELLER", 190.0, 4.8, null),
                Product("lm2", "Pork Bicol Express", "Spicy pork in coconut cream ★ BEST SELLER", 195.0, 4.7, null),
                Product("lm3", "Chicken Curry", "Creamy coconut chicken curry ★ BEST SELLER", 180.0, 4.7, null),
                Product("lm4", "Inihaw na Liempo", "Grilled pork belly", 185.0, 4.6, null),
                Product("lm5", "Sinigang na Baboy", "Pork in sour tamarind soup", 170.0, 4.6, null),
                Product("lm6", "Pancit Palabok", "Rice noodles with shrimp sauce", 145.0, 4.4, null),
                Product("lm7", "Ginataang Langka", "Young jackfruit in coconut milk", 120.0, 4.3, null),
                Product("lm8", "Chicken Pastel", "Chicken in creamy mushroom sauce", 175.0, 4.5, null),
                Product("lm9", "Buko Salad", "Fruit salad with young coconut", 80.0, 4.5, null),
                Product("lm10", "Saba con Hielo", "Boiled saba banana in sweet milk", 55.0, 4.4, null),
            ),
            "Mariveles" to listOf(
                Product("mv1", "Mariveles Seafood Basket", "Mixed fried seafood with chips ★ BEST SELLER", 380.0, 4.9, null),
                Product("mv2", "Grilled Blue Marlin", "Fresh blue marlin steak grilled ★ BEST SELLER", 280.0, 4.8, null),
                Product("mv3", "Shrimp Sinigang", "Sour soup with fresh shrimp ★ BEST SELLER", 195.0, 4.8, null),
                Product("mv4", "Adobong Pusit", "Squid adobo in its own ink", 185.0, 4.6, null),
                Product("mv5", "Fried Tahong", "Battered fried mussels", 160.0, 4.6, null),
                Product("mv6", "Inihaw na Tuna Panga", "Grilled tuna jaw", 240.0, 4.7, null),
                Product("mv7", "Kilawin na Tanigue", "Raw mackerel ceviche", 200.0, 4.5, null),
                Product("mv8", "Ginataang Alimasag", "Crabs in coconut cream", 250.0, 4.6, null),
                Product("mv9", "Halo-Halo", "Shaved ice dessert special", 95.0, 4.7, null),
                Product("mv10", "Niyog-niyogan", "Coconut rice cake snack", 50.0, 4.3, null),
            ),
            "Morong" to listOf(
                Product("mo1", "Morong Fried Chicken", "Herb-marinated crispy fried chicken ★ BEST SELLER", 195.0, 4.8, null),
                Product("mo2", "Paksiw na Isda", "Fish braised in vinegar & spices ★ BEST SELLER", 155.0, 4.7, null),
                Product("mo3", "Beef Mechado", "Beef stew in tomato sauce ★ BEST SELLER", 190.0, 4.7, null),
                Product("mo4", "Inihaw na Manok", "Grilled chicken marinated in annatto", 175.0, 4.6, null),
                Product("mo5", "Pork Adobo", "Classic Filipino pork adobo", 160.0, 4.6, null),
                Product("mo6", "Ginisang Sayote", "Sauteed chayote with shrimp", 110.0, 4.3, null),
                Product("mo7", "Okoy", "Shrimp and vegetable fritters", 100.0, 4.4, null),
                Product("mo8", "Lumpiang Ubod", "Fresh spring rolls with palm heart", 135.0, 4.5, null),
                Product("mo9", "Puto Calasiao", "Small rice cakes", 50.0, 4.4, null),
                Product("mo10", "Buko Pandan", "Coconut jelly drink with pandan", 60.0, 4.5, null),
            ),
            "Orani" to listOf(
                Product("or1", "Orani Fried Isda", "Crispy whole fish with special dip ★ BEST SELLER", 210.0, 4.8, null),
                Product("or2", "Pork Stir-fry", "Spicy pork stir-fry with vegetables ★ BEST SELLER", 175.0, 4.7, null),
                Product("or3", "Chicken Afritada", "Chicken stewed in tomato sauce ★ BEST SELLER", 170.0, 4.7, null),
                Product("or4", "Ginataang Kuhol", "Snails in spicy coconut cream", 145.0, 4.5, null),
                Product("or5", "Pritong Hito", "Crispy fried catfish", 165.0, 4.5, null),
                Product("or6", "Pinakbet", "Mixed vegetables with bagoong", 130.0, 4.4, null),
                Product("or7", "Pork Barbecue", "Grilled pork on sticks (3 sticks)", 100.0, 4.6, null),
                Product("or8", "Pancit Bihon", "Thin rice noodles with vegetables", 140.0, 4.4, null),
                Product("or9", "Kalamay", "Sticky rice cake with coconut", 70.0, 4.5, null),
                Product("or10", "Salabat", "Fresh ginger tea", 35.0, 4.6, null),
            ),
            "Orion" to listOf(
                Product("ot1", "Orion Beef Pares", "Beef brisket with garlic rice & soup ★ BEST SELLER", 175.0, 4.9, null),
                Product("ot2", "Lechon Manok", "Whole roasted chicken ★ BEST SELLER", 280.0, 4.8, null),
                Product("ot3", "Pork Giniling", "Ground pork with vegetables ★ BEST SELLER", 145.0, 4.7, null),
                Product("ot4", "Calderetang Kambing", "Spicy goat stew", 230.0, 4.6, null),
                Product("ot5", "Pritong Dalag", "Crispy fried mudfish", 185.0, 4.5, null),
                Product("ot6", "Gulay na Langka", "Young jackfruit vegetable stew", 110.0, 4.3, null),
                Product("ot7", "Tinolang Isda", "Fish in sour ginger soup", 160.0, 4.5, null),
                Product("ot8", "Pork Tempura", "Deep-fried pork strips", 120.0, 4.4, null),
                Product("ot9", "Maruya", "Fried banana fritters with sugar", 50.0, 4.6, null),
                Product("ot10", "Mango Shake", "Fresh carabao mango shake", 70.0, 4.7, null),
            ),
            "Pilar" to listOf(
                Product("pl1", "Pilar Crispy Tadyang", "Crispy fried beef ribs ★ BEST SELLER", 260.0, 4.9, null),
                Product("pl2", "Chicken Sopas", "Creamy chicken macaroni soup ★ BEST SELLER", 130.0, 4.7, null),
                Product("pl3", "Pork Sinigang", "Sour tamarind pork belly ★ BEST SELLER", 180.0, 4.7, null),
                Product("pl4", "Beef Tapa", "Cured beef strips", 185.0, 4.6, null),
                Product("pl5", "Fried Danggit", "Fried rabbitfish with vinegar", 150.0, 4.5, null),
                Product("pl6", "Chicken Adobo", "Chicken adobo with garlic", 165.0, 4.6, null),
                Product("pl7", "Pancit Malabon", "Thick rice noodles with seafood", 160.0, 4.5, null),
                Product("pl8", "Ginataang Sitaw at Kalabasa", "String beans & squash in coconut", 115.0, 4.3, null),
                Product("pl9", "Sapin-sapin", "Layered rice cake with coconut", 65.0, 4.4, null),
                Product("pl10", "Guyabano Juice", "Fresh soursop fruit juice", 55.0, 4.5, null),
            ),
            "Samal" to listOf(
                Product("sa1", "Samal Fried Pla-Pla", "Whole fried pla-pla with garlic ★ BEST SELLER", 220.0, 4.8, null),
                Product("sa2", "Pork Lechon", "Lechon baka (roasted beef) specialty ★ BEST SELLER", 320.0, 4.8, null),
                Product("sa3", "Chicken Tinola", "Ginger chicken soup with sayote ★ BEST SELLER", 170.0, 4.7, null),
                Product("sa4", "Crispy Danggit", "Crispy dried rabbitfish", 145.0, 4.6, null),
                Product("sa5", "Pininyahang Manok", "Pineapple chicken", 165.0, 4.5, null),
                Product("sa6", "Bicol Express", "Pork in spicy coconut milk", 190.0, 4.6, null),
                Product("sa7", "Dinuguan", "Pork blood stew with puto", 135.0, 4.5, null),
                Product("sa8", "Lumpia Sariwa", "Fresh lumpia with peanut sauce", 130.0, 4.4, null),
                Product("sa9", "Royal Bibingka", "Rice cake with salted egg & cheese", 70.0, 4.6, null),
                Product("sa10", "Hot Tsokolate", "Tablea chocolate drink", 45.0, 4.7, null),
            ),
            "Balagtas" to listOf(
                Product("bl1", "Balagtas Fried Chicken", "Crispy golden fried chicken ★ BEST SELLER", 190.0, 4.8, null),
                Product("bl2", "Kare-Kare", "Oxtail & tripe peanut stew ★ BEST SELLER", 260.0, 4.8, null),
                Product("bl3", "Pork Sisig", "Sizzling pork sisig special ★ BEST SELLER", 185.0, 4.7, null),
                Product("bl4", "Beef Caldereta", "Spicy beef stew", 220.0, 4.6, null),
                Product("bl5", "Sinampalukang Manok", "Chicken simmered in tamarind", 175.0, 4.6, null),
                Product("bl6", "Pancit Canton", "Stir-fried noodle delight", 145.0, 4.5, null),
                Product("bl7", "Lechon Kawali", "Crispy pork belly", 200.0, 4.7, null),
                Product("bl8", "Ginisang Ampalaya", "Sauteed bitter melon with egg", 110.0, 4.3, null),
                Product("bl9", "Pastillas de Leche", "Milk candies wrapped in paper", 80.0, 4.6, null),
                Product("bl10", "Melon Juice", "Sweet melon refreshment", 55.0, 4.5, null),
            ),
            "Baliwag City" to listOf(
                Product("bw1", "Baliwag Lechon Manok", "Famous roasted chicken special ★ BEST SELLER", 290.0, 4.9, null),
                Product("bw2", "Pork Steak", "Filipino pork steak with onions ★ BEST SELLER", 180.0, 4.7, null),
                Product("bw3", "Chicken Curry", "Creamy Filipino chicken curry ★ BEST SELLER", 175.0, 4.7, null),
                Product("bw4", "Crispy Pata", "Deep-fried pork hock", 290.0, 4.8, null),
                Product("bw5", "Pancit Malabon", "Seafood rice noodles", 160.0, 4.5, null),
                Product("bw6", "Inihaw na Liempo", "Grilled pork belly", 185.0, 4.6, null),
                Product("bw7", "Pinakbet", "Sauteed vegetables with bagoong", 130.0, 4.4, null),
                Product("bw8", "Tokwat Baboy", "Tofu and pork appetizer", 115.0, 4.4, null),
                Product("bw9", "Chicharon", "Pork cracklings (Baliwag specialty)", 100.0, 4.7, null),
                Product("bw10", "Sago at Gulaman", "Refreshing cold drink", 45.0, 4.5, null),
            ),
            "Angat" to listOf(
                Product("an1", "Angat Fried Isda", "Crispy fried dalag with suka ★ BEST SELLER", 200.0, 4.8, null),
                Product("an2", "Pork Adobo sa Gata", "Pork adobo in coconut cream ★ BEST SELLER", 195.0, 4.7, null),
                Product("an3", "Chicken Menudo", "Chicken stew with hotdog & raisins ★ BEST SELLER", 165.0, 4.7, null),
                Product("an4", "Suam na Mais", "Corn and malunggay soup", 110.0, 4.5, null),
                Product("an5", "Ginataang Alimasag", "Crabs cooked in coconut milk", 240.0, 4.6, null),
                Product("an6", "Pritong Tilapia", "Fried tilapia with tomatoes", 160.0, 4.5, null),
                Product("an7", "Inihaw na Baboy", "Grilled pork chops", 180.0, 4.5, null),
                Product("an8", "Lumpiang Togue", "Bean sprout spring rolls", 110.0, 4.3, null),
                Product("an9", "Inipit", "Soft layered cake with filling", 60.0, 4.5, null),
                Product("an10", "Kalamansi Juice", "Fresh lime cooler", 40.0, 4.5, null),
            ),
            "Bustos" to listOf(
                Product("bu1", "Bustos Fried Chicken", "Herb-marinated crispy fried chicken ★ BEST SELLER", 195.0, 4.8, null),
                Product("bu2", "Beef Kare-Kare", "Beef tripe & vegetables in peanut sauce ★ BEST SELLER", 250.0, 4.8, null),
                Product("bu3", "Chicharon Bulaklak", "Crispy pork ruffles special ★ BEST SELLER", 160.0, 4.7, null),
                Product("bu4", "Pork Menudo", "Pork stew with hotdog & cheese", 155.0, 4.5, null),
                Product("bu5", "Tinolang Manok", "Chicken ginger stew", 165.0, 4.5, null),
                Product("bu6", "Daing na Bangus", "Marinated fried milkfish", 175.0, 4.6, null),
                Product("bu7", "Longganisa", "Bustos-style garlic sausage (5 pcs)", 130.0, 4.6, null),
                Product("bu8", "Pancit Luglug", "Rice noodles with shrimp sauce", 145.0, 4.4, null),
                Product("bu9", "Puto", "Steamed rice cake", 40.0, 4.4, null),
                Product("bu10", "Suman", "Glutinous rice in banana leaf", 50.0, 4.5, null),
            ),
            "Malolos City" to listOf(
                Product("mc1", "Pastillas de Malolos", "Famous carabao milk candy ★ BEST SELLER", 90.0, 4.9, null),
                Product("mc2", "Inipit ng Malolos", "Layered chiffon cake with filling ★ BEST SELLER", 75.0, 4.8, null),
                Product("mc3", "Ensaymada de Malolos", "Buttery brioche with cheese ★ BEST SELLER", 65.0, 4.7, null),
                Product("mc4", "Pork Sisig", "Sizzling pork sisig", 185.0, 4.7, null),
                Product("mc5", "Chicken Inasal", "Grilled chicken with annatto oil", 180.0, 4.6, null),
                Product("mc6", "Pancit Palabok", "Rice noodles with palabok sauce", 150.0, 4.5, null),
                Product("mc7", "Crispy Pata", "Deep-fried pork leg", 290.0, 4.7, null),
                Product("mc8", "Sinigang na Hipon", "Shrimp in sour soup", 190.0, 4.6, null),
                Product("mc9", "Halo-Halo", "Special halo-halo with pastillas", 100.0, 4.7, null),
                Product("mc10", "Leche Flan", "Creamy caramel custard", 80.0, 4.7, null),
            ),
            "Meycauayan City" to listOf(
                Product("my1", "Meycauayan Fried Tilapia", "Crispy fried tilapia special ★ BEST SELLER", 175.0, 4.8, null),
                Product("my2", "Bistek Tagalog", "Beefsteak in soy-calamansi ★ BEST SELLER", 190.0, 4.7, null),
                Product("my3", "Chicken Barbecue", "Grilled chicken skewers with sauce ★ BEST SELLER", 140.0, 4.7, null),
                Product("my4", "Pork Adobo", "Classic pork adobo", 160.0, 4.6, null),
                Product("my5", "Sinigang na Baboy", "Pork in tamarind soup", 170.0, 4.5, null),
                Product("my6", "Lumpiang Shanghai", "Fried spring rolls (12 pcs)", 130.0, 4.6, null),
                Product("my7", "Ginataang Kuhol", "Snails in coconut cream", 145.0, 4.4, null),
                Product("my8", "Pancit Bihon", "Stir-fried rice noodles", 135.0, 4.4, null),
                Product("my9", "Bibingka", "Rice cake with cheese topping", 70.0, 4.5, null),
                Product("my10", "Mais con Hielo", "Sweet corn in crushed ice", 60.0, 4.5, null),
            ),
            "San Jose del Monte" to listOf(
                Product("sj1", "SJDM Fried Chicken", "Garlic pepper fried chicken ★ BEST SELLER", 195.0, 4.8, null),
                Product("sj2", "Bulalo", "Beef marrow soup special ★ BEST SELLER", 250.0, 4.8, null),
                Product("sj3", "Pork BBQ", "Grilled pork barbecue (5 sticks) ★ BEST SELLER", 130.0, 4.7, null),
                Product("sj4", "Chicken Curry", "Creamy coconut chicken", 175.0, 4.6, null),
                Product("sj5", "Lechon Kawali", "Crispy pork belly with lechon sauce", 200.0, 4.7, null),
                Product("sj6", "Pancit Canton", "Canton noodles with quail eggs", 145.0, 4.5, null),
                Product("sj7", "Tokwat Baboy", "Tofu with pork ears & soy dip", 120.0, 4.4, null),
                Product("sj8", "Ginisang Munggo", "Sauteed mung beans with chicharon", 125.0, 4.4, null),
                Product("sj9", "Puto Calasiao", "Rice cake bites (10 pcs)", 45.0, 4.4, null),
                Product("sj10", "Sago't Gulaman", "Classic refreshing drink", 40.0, 4.5, null),
            ),
            "Marilao" to listOf(
                Product("ma1", "Marilao Fried Isda", "Crispy friend isda with suka at bawang ★ BEST SELLER", 190.0, 4.8, null),
                Product("ma2", "Pork Lechon", "Bustos-style lechon baka ★ BEST SELLER", 320.0, 4.7, null),
                Product("ma3", "Chicken Adobo", "Adobong manok sa toyo ★ BEST SELLER", 165.0, 4.7, null),
                Product("ma4", "Monggo with Chicharon", "Mung bean soup with pork rinds", 120.0, 4.5, null),
                Product("ma5", "Tinolang Manok", "Traditional chicken tinola", 165.0, 4.5, null),
                Product("ma6", "Pritong Bangus", "Fried milkfish with tomato salsa", 180.0, 4.5, null),
                Product("ma7", "Lumpiang Shanghai", "Special lumpiang shanghai", 125.0, 4.6, null),
                Product("ma8", "Ensaladang Talong", "Grilled eggplant & tomato salad", 95.0, 4.3, null),
                Product("ma9", "Kalamay", "Sticky rice sweet treat", 55.0, 4.5, null),
                Product("ma10", "Buko Juice", "Fresh young coconut juice", 50.0, 4.6, null),
            ),
            "Pulilan" to listOf(
                Product("pu1", "Pulilan Roasted Chicken", "Herb-roasted whole chicken ★ BEST SELLER", 290.0, 4.8, null),
                Product("pu2", "Beef Caldereta", "Spicy beef caldereta special ★ BEST SELLER", 225.0, 4.8, null),
                Product("pu3", "Pork Sinigang", "Sinigang na baboy sa sampaloc ★ BEST SELLER", 175.0, 4.7, null),
                Product("pu4", "Chicken Alfredo", "Pasta in creamy alfredo sauce", 195.0, 4.5, null),
                Product("pu5", "Crispy Danggit", "Crispy dried fish with rice", 140.0, 4.5, null),
                Product("pu6", "Pork Steak", "Pork steak with onion rings", 175.0, 4.5, null),
                Product("pu7", "Pancit Bihon", "Special bihon guisado", 135.0, 4.4, null),
                Product("pu8", "Ginataang Langka", "Jackfruit in coconut milk", 120.0, 4.3, null),
                Product("pu9", "Puto Bumbong", "Steamed purple rice with niyog", 55.0, 4.5, null),
                Product("pu10", "Salabat", "Hot ginger tea", 35.0, 4.6, null),
            ),
            "San Ildefonso" to listOf(
                Product("si1", "San Ildefonso Fried Catfish", "Crispy golden fried hito ★ BEST SELLER", 185.0, 4.8, null),
                Product("si2", "Pork Kare-Kare", "Pork kare-kare with bagoong ★ BEST SELLER", 240.0, 4.7, null),
                Product("si3", "Chicken Tinola", "Classic tinolang manok ★ BEST SELLER", 165.0, 4.7, null),
                Product("si4", "Pork Sisig", "Spicy sizzling pork sisig", 180.0, 4.6, null),
                Product("si5", "Beef Tapa", "Garlic beef tapa with sinangag", 185.0, 4.6, null),
                Product("si6", "Ginataang Alimasag", "Crabs in gata", 245.0, 4.5, null),
                Product("si7", "Dinuguan", "Pork dinuguan with puto", 135.0, 4.5, null),
                Product("si8", "Pancit Guisado", "Stir-fried noodles with veggies", 135.0, 4.4, null),
                Product("si9", "Sopas", "Creamy chicken sopas", 110.0, 4.4, null),
                Product("si10", "Turron", "Fried banana spring rolls", 55.0, 4.5, null),
            ),
            "San Rafael" to listOf(
                Product("sr1", "San Rafael Fried Liempo", "Crispy fried pork liempo ★ BEST SELLER", 195.0, 4.8, null),
                Product("sr2", "Beef Mechado", "Tomato-based beef stew ★ BEST SELLER", 200.0, 4.7, null),
                Product("sr3", "Chicken BBQ", "Grilled chicken barbecue ★ BEST SELLER", 145.0, 4.7, null),
                Product("sr4", "Pork Adobo sa Gata", "Adobo with coconut milk", 185.0, 4.6, null),
                Product("sr5", "Sinigang na Isda", "Fish sour soup", 165.0, 4.5, null),
                Product("sr6", "Lumpiang Shanghai", "Fried lumpia (15 pcs)", 140.0, 4.6, null),
                Product("sr7", "Pininyahang Manok", "Pineapple chicken stew", 170.0, 4.5, null),
                Product("sr8", "Pancit Palabok", "Palabok with shrimps & eggs", 150.0, 4.5, null),
                Product("sr9", "Mango Float", "Filipino mango dessert", 85.0, 4.6, null),
                Product("sr10", "Graham Balls", "Bite-sized graham dessert", 45.0, 4.5, null),
            ),
            "Santa Maria" to listOf(
                Product("smr1", "Santa Maria Fried Chicken", "Garlic rosemary fried chicken ★ BEST SELLER", 200.0, 4.8, null),
                Product("smr2", "Pork Bicol Express", "Spicy pork in coconut cream ★ BEST SELLER", 195.0, 4.7, null),
                Product("smr3", "Beef Broccoli", "Beef with broccoli in oyster sauce ★ BEST SELLER", 210.0, 4.7, null),
                Product("smr4", "Chicken Adobo Flakes", "Shredded adobo fried crispy", 165.0, 4.6, null),
                Product("smr5", "Monggo Guisado", "Sauteed mung beans", 125.0, 4.4, null),
                Product("smr6", "Pritong Tilapia", "Fried tilapia with spicy dip", 160.0, 4.5, null),
                Product("smr7", "Ginataang Kuhol", "Snails in coconut cream", 145.0, 4.4, null),
                Product("smr8", "Pancit Canton", "Pancit canton special", 145.0, 4.5, null),
                Product("smr9", "Buko Salad", "Young coconut fruit salad", 80.0, 4.5, null),
                Product("smr10", "Calamansi Juice", "Iced calamansi drink", 40.0, 4.5, null),
            ),
            "Candaba" to listOf(
                Product("ca1", "Candaba Fried Danggit", "Crispy danggit from Candaba wetlands ★ BEST SELLER", 150.0, 4.8, null),
                Product("ca2", "Pork Sisig", "Original Kapampangan sisig ★ BEST SELLER", 195.0, 4.9, null),
                Product("ca3", "Bringhe", "Kapampangan green rice with chicken ★ BEST SELLER", 190.0, 4.8, null),
                Product("ca4", "Tocino", "Pampanga's Best sweet cured pork", 170.0, 4.7, null),
                Product("ca5", "Kilawin", "Fresh fish kilawin in vinegar", 185.0, 4.6, null),
                Product("ca6", "Menudo", "Pork menudo Kapampangan-style", 155.0, 4.5, null),
                Product("ca7", "Lechon Manok", "Roasted chicken special", 280.0, 4.6, null),
                Product("ca8", "Pancit Molo", "Kapampangan wonton soup", 145.0, 4.5, null),
                Product("ca9", "Turrones de Casuy", "Cashew nougat candy", 120.0, 4.8, null),
                Product("ca10", "Puto Seko", "Powdery rice cookies", 60.0, 4.5, null),
            ),
            "Floridablanca" to listOf(
                Product("fl1", "Floridablanca Fried Chicken", "Special garlic fried chicken ★ BEST SELLER", 195.0, 4.8, null),
                Product("fl2", "Beef Caldereta", "Kapampangan spicy beef stew ★ BEST SELLER", 230.0, 4.8, null),
                Product("fl3", "Pork Adobo", "Adobong matanda (dried adobo) ★ BEST SELLER", 170.0, 4.7, null),
                Product("fl4", "Chicken Inasal", "Grilled chicken with sinamak", 185.0, 4.6, null),
                Product("fl5", "Pork BBQ", "Floridablanca-style pork skewers", 135.0, 4.6, null),
                Product("fl6", "Sinigang na Hipon", "Shrimp sour soup", 190.0, 4.5, null),
                Product("fl7", "Dinuguan", "Pampanga dinuguan with puto", 140.0, 4.5, null),
                Product("fl8", "Pancit Bihon", "Pancit bihon guisado", 135.0, 4.4, null),
                Product("fl9", "Sanikulas Cookie", "Flower-shaped shortbread cookies", 70.0, 4.6, null),
                Product("fl10", "Buko Pandan", "Coconut pandan refreshment", 60.0, 4.5, null),
            ),
            "Guagua" to listOf(
                Product("gu1", "Guagua Fried Chicken", "Famous Guagua-style crispy chicken ★ BEST SELLER", 200.0, 4.9, null),
                Product("gu2", "Bringheng Guagua", "Kapampangan safflower rice ★ BEST SELLER", 195.0, 4.8, null),
                Product("gu3", "Pork Kilawin", "Guagua-style pork ceviche ★ BEST SELLER", 190.0, 4.7, null),
                Product("gu4", "Tocino", "Pampanga's Best original tocino", 170.0, 4.7, null),
                Product("gu5", "Kare-Kare", "Oxtail kare-kare with bagoong", 260.0, 4.7, null),
                Product("gu6", "Lechon Kawali", "Crispy pork belly Guagua-style", 200.0, 4.7, null),
                Product("gu7", "Chicken Adobo", "Kapampangan adobo sa puti", 165.0, 4.6, null),
                Product("gu8", "Pancit Molo", "Wonton soup Guagua-style", 150.0, 4.5, null),
                Product("gu9", "Buro", "Fermented rice & shrimp delicacy", 55.0, 4.3, null),
                Product("gu10", "Puto Seko", "Traditional Pampanga rice cookies", 60.0, 4.6, null),
            ),
            "Lubao" to listOf(
                Product("lu1", "Lubao Fried Talangka", "Crispy fried small crabs ★ BEST SELLER", 220.0, 4.9, null),
                Product("lu2", "Pork Sinigang", "Sinigang na baboy sa miso ★ BEST SELLER", 180.0, 4.7, null),
                Product("lu3", "Beef Tapa", "Lubao-style cured beef ★ BEST SELLER", 190.0, 4.7, null),
                Product("lu4", "Sisig", "Original sizzling sisig", 195.0, 4.8, null),
                Product("lu5", "Lechon Manok", "Lubao roasted chicken", 285.0, 4.6, null),
                Product("lu6", "Pork Barbecue", "Lubao-style pork BBQ (5 sticks)", 140.0, 4.6, null),
                Product("lu7", "Inihaw na Bangus", "Grilled milkfish with special dip", 195.0, 4.5, null),
                Product("lu8", "Pancit Palabok", "Palabok special", 145.0, 4.4, null),
                Product("lu9", "Bibingka", "Lubao bibingka with itlog na maalat", 75.0, 4.5, null),
                Product("lu10", "Turrones de Lubao", "Cashew nougat Lubao specialty", 130.0, 4.7, null),
            ),
        )
        return menus[town] ?: listOf(
            Product("g1", "Chicken Adobo", "Tender chicken simmered in soy-vinegar sauce", 180.0, 4.8, null),
            Product("g2", "Pork Sinigang", "Sour tamarind soup with fresh veggies", 165.0, 4.7, null),
            Product("g3", "Beef Caldereta", "Spicy tomato-based beef stew ★ BEST SELLER", 220.0, 4.9, null),
            Product("g4", "Lechon Kawali", "Crispy deep-fried pork belly", 195.0, 4.8, null),
            Product("g5", "Chicken Curry", "Creamy coconut chicken curry", 175.0, 4.6, null),
            Product("g6", "Bangus Sisig", "Sizzling milkfish sisig", 155.0, 4.5, null),
            Product("g7", "Kare-Kare", "Peanut stew with oxtail & veggies", 250.0, 4.9, null),
            Product("g8", "Pancit Palabok", "Rice noodles with shrimp sauce", 145.0, 4.4, null),
        )
    }

    inner class ProductAdapter(
        private val products: List<Product>,
        private val onAdd: (Product) -> Unit
    ) : BaseAdapter() {
        override fun getCount(): Int = products.size
        override fun getItem(position: Int): Any = products[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_product, parent, false)

            val product = products[position]

            view.findViewById<TextView>(R.id.tvProductName).text = product.name
            view.findViewById<TextView>(R.id.tvProductDesc).text = product.description
            view.findViewById<TextView>(R.id.tvProductPrice).text = "₱${product.price}"
            view.findViewById<TextView>(R.id.tvProductRating).text = "⭐ ${product.rating}"

            val tvBestSeller = view.findViewById<TextView>(R.id.tvBestSeller)
            tvBestSeller.visibility = if (position < 3) View.VISIBLE else View.GONE

            view.findViewById<TextView>(R.id.btnAddToCart).setOnClickListener { onAdd(product) }

            return view
        }
    }
}