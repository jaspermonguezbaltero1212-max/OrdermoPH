package com.example.ordermoph

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class FirestoreManager {

    private val db: FirebaseFirestore = Firebase.firestore
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private val localUsers = mutableMapOf<String, UserProfile>()
        private val localMerchants = mutableListOf(
            Merchant("m1", "Jollibee Castillejos", "Castillejos", "Fast Food"),
            Merchant("m2", "Mang Inasal Iba", "Iba", "Fast Food"),
            Merchant("m3", "Kusina ni Maria", "Angat", "Local Eatery"),
            Merchant("m4", "Tapsi ni Juan", "Malolos City", "Local Eatery"),
            Merchant("m5", "Mariveles Seafood", "Mariveles", "Seafood"),
            Merchant("m6", "Balanga Eatery", "Balanga City", "Local Eatery"),
            Merchant("m7", "Iba's Best", "Iba", "Local Eatery"),
            Merchant("m8", "Subic Bay Grille", "Subic Bay Freeport", "Restaurant"),
            Merchant("m9", "Alvin's Eatery", "Guagua", "Local Eatery"),
            Merchant("m10", "Mila's Lechon", "Bustos", "Lechon"),
            Merchant("m11", "Olongapo Diner", "Olongapo City", "Local Eatery"),
            Merchant("m12", "San Marcelino Bakery", "San Marcelino", "Bakery"),
            Merchant("m13", "Dinalupihan Ihawan", "Dinalupihan", "Grill"),
            Merchant("m14", "Floridablanca Restaurant", "Floridablanca", "Local Eatery"),
            Merchant("m15", "Lubao Eatery", "Lubao", "Local Eatery"),
        )
        private val localProducts = mutableMapOf<String, MutableList<Product>>()
        private val localOrders = mutableListOf<Order>()
        private var firebaseReady = false
        private var firebaseTested = false
    }

    private val firebaseAvailable: Boolean get() = firebaseTested && firebaseReady

    init {
        if (!firebaseTested) {
            firebaseTested = true
            testFirebaseConnection()
        }
    }

    private fun testFirebaseConnection() {
        db.collection("_test").document("_ping").set(mapOf("t" to System.currentTimeMillis()))
            .addOnSuccessListener {
                firebaseReady = true
                Log.d("Firestore", "Firebase connected")
                db.collection("_test").document("_ping").delete()
            }
            .addOnFailureListener {
                firebaseReady = false
                Log.d("Firestore", "Firebase unavailable, using local mode")
            }
    }

    private fun <T> fallbackIfNeeded(firebase: (Boolean) -> Unit, local: () -> T): T? {
        if (firebaseAvailable) {
            firebase(true)
        } else {
            firebase(false)
        }
        return null
    }

    fun placeOrder(order: Order, onComplete: (Boolean, String?) -> Unit) {
        if (firebaseAvailable) {
            val orderRef = db.collection("orders").document()
            val finalOrder = order.copy(orderId = orderRef.id, source = "Android")
            orderRef.set(finalOrder)
                .addOnSuccessListener { onComplete(true, orderRef.id) }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "placeOrder failed: ${e.message}")
                    handler.postDelayed({ onComplete(false, null) }, 200)
                }
        } else {
            val orderId = UUID.randomUUID().toString()
            localOrders.add(order.copy(orderId = orderId))
            handler.postDelayed({ onComplete(true, orderId) }, 200)
        }
    }

    fun declineOrder(orderId: String, onComplete: (Boolean) -> Unit) {
        if (firebaseAvailable) {
            db.collection("orders").document(orderId)
                .update("status", "cancelled")
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } else {
            val idx = localOrders.indexOfFirst { it.orderId == orderId }
            if (idx >= 0) localOrders[idx] = localOrders[idx].copy(status = "cancelled")
            handler.postDelayed({ onComplete(idx >= 0) }, 200)
        }
    }

    fun acceptOrder(orderId: String, merchantId: String, onComplete: (Boolean) -> Unit) {
        if (firebaseAvailable) {
            db.collection("orders").document(orderId)
                .update("status", "accepted", "merchantId", merchantId)
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } else {
            val idx = localOrders.indexOfFirst { it.orderId == orderId }
            if (idx >= 0) localOrders[idx] = localOrders[idx].copy(status = "accepted", merchantId = merchantId)
            handler.postDelayed({ onComplete(idx >= 0) }, 200)
        }
    }

    fun startDelivery(orderId: String, onComplete: (Boolean) -> Unit) {
        if (firebaseAvailable) {
            db.collection("orders").document(orderId)
                .update("status", "delivering")
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } else {
            val idx = localOrders.indexOfFirst { it.orderId == orderId }
            if (idx >= 0) localOrders[idx] = localOrders[idx].copy(status = "delivering")
            handler.postDelayed({ onComplete(idx >= 0) }, 200)
        }
    }

    fun completeDelivery(orderId: String, onComplete: (Boolean) -> Unit) {
        if (firebaseAvailable) {
            db.collection("orders").document(orderId)
                .update("status", "completed")
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } else {
            val idx = localOrders.indexOfFirst { it.orderId == orderId }
            if (idx >= 0) localOrders[idx] = localOrders[idx].copy(status = "completed")
            handler.postDelayed({ onComplete(idx >= 0) }, 200)
        }
    }

    fun assignRider(orderId: String, riderId: String, onComplete: (Boolean) -> Unit) {
        if (firebaseAvailable) {
            db.collection("orders").document(orderId)
                .update("status", "picking_up", "riderId", riderId)
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } else {
            val idx = localOrders.indexOfFirst { it.orderId == orderId }
            if (idx >= 0) localOrders[idx] = localOrders[idx].copy(status = "picking_up", riderId = riderId)
            handler.postDelayed({ onComplete(idx >= 0) }, 200)
        }
    }

    fun getMerchants(onResult: (List<Merchant>) -> Unit) {
        if (firebaseAvailable) {
            db.collection("merchants").get()
                .addOnSuccessListener { snap -> onResult(snap.documents.mapNotNull { it.toObject(Merchant::class.java)?.copy(id = it.id) }) }
                .addOnFailureListener { onResult(emptyList()) }
        } else {
            handler.postDelayed({ onResult(localMerchants.toList()) }, 200)
        }
    }

    fun getProducts(merchantId: String, onResult: (List<Product>) -> Unit) {
        if (firebaseAvailable) {
            db.collection("merchants").document(merchantId).collection("products").get()
                .addOnSuccessListener { snap -> onResult(snap.documents.mapNotNull { it.toObject(Product::class.java)?.copy(id = it.id) }) }
                .addOnFailureListener { onResult(emptyList()) }
        } else {
            handler.postDelayed({ onResult(localProducts[merchantId]?.toList() ?: emptyList()) }, 200)
        }
    }

    fun getMerchantsByRegion(region: String, onResult: (List<Merchant>) -> Unit) {
        if (firebaseAvailable) {
            db.collection("merchants").whereEqualTo("location", region).get()
                .addOnSuccessListener { snap -> onResult(snap.documents.mapNotNull { it.toObject(Merchant::class.java)?.copy(id = it.id) }) }
                .addOnFailureListener { onResult(emptyList()) }
        } else {
            handler.postDelayed({ onResult(localMerchants.filter { it.location == region }) }, 200)
        }
    }

    fun addProduct(merchantId: String, product: Product, onComplete: (Boolean) -> Unit) {
        if (firebaseAvailable) {
            val ref = db.collection("merchants").document(merchantId).collection("products").document()
            ref.set(product.copy(id = ref.id))
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } else {
            val list = localProducts.getOrPut(merchantId) { mutableListOf() }
            list.add(product.copy(id = UUID.randomUUID().toString()))
            handler.postDelayed({ onComplete(true) }, 200)
        }
    }

    fun listenToOrders(role: String, userId: String, onUpdate: (List<Order>) -> Unit) {
        if (firebaseAvailable) {
            val query = when (role) {
                "customer" -> db.collection("orders").whereEqualTo("customerId", userId)
                else -> db.collection("orders")
            }
            query.addSnapshotListener { snap, _ -> onUpdate(snap?.toObjects(Order::class.java) ?: emptyList()) }
        } else {
            val result = when (role) {
                "customer" -> localOrders.filter { it.customerId == userId }
                else -> localOrders.toList()
            }
            onUpdate(result)
        }
    }

    fun saveUser(profile: UserProfile, onComplete: (Boolean) -> Unit) {
        if (firebaseAvailable) {
            db.collection("users").document(profile.uid).set(profile)
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } else {
            localUsers[profile.uid] = profile
            handler.postDelayed({ onComplete(true) }, 200)
        }
    }

    fun getUser(uid: String, onComplete: (UserProfile?) -> Unit) {
        if (uid.isBlank()) { handler.post { onComplete(null) }; return }
        if (firebaseAvailable) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc -> onComplete(doc.toObject(UserProfile::class.java)) }
                .addOnFailureListener { onComplete(null) }
        } else {
            handler.postDelayed({ onComplete(localUsers[uid]) }, 200)
        }
    }

    fun getOrderById(orderId: String): Order? {
        return localOrders.find { it.orderId == orderId }
    }

    fun listenToOrder(orderId: String, onUpdate: (Order?) -> Unit, onCleanup: ((Runnable) -> Unit)? = null) {
        if (firebaseAvailable) {
            db.collection("orders").document(orderId)
                .addSnapshotListener { snap, _ ->
                    onUpdate(snap?.toObject(Order::class.java)?.copy(orderId = snap.id))
                }
        } else {
            val poller = object : Runnable {
                override fun run() {
                    onUpdate(localOrders.find { it.orderId == orderId })
                    handler.postDelayed(this, 2000)
                }
            }
            handler.post(poller)
            onCleanup?.invoke(poller)
        }
    }
}
