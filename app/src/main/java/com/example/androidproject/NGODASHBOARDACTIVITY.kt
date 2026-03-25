package com.example.feedforward

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NgoDashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rvDonations: RecyclerView
    private lateinit var adapter: FoodAdapter
    private val donationList = mutableListOf<Donation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_dashboard)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        rvDonations = findViewById(R.id.rvDonations)
        rvDonations.layoutManager = LinearLayoutManager(this)

        adapter = FoodAdapter(donationList) { selectedDonation ->
            handleDonationAction(selectedDonation)
        }
        rvDonations.adapter = adapter

        val btnProfile = findViewById<ImageButton>(R.id.btnProfile)
        btnProfile.setOnClickListener {
            startActivity(Intent(this, NgoProfileActivity::class.java))
        }

        val btnLogout = findViewById<ImageButton>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        findViewById<FloatingActionButton>(R.id.chatBtn).setOnClickListener {
            startActivity(Intent(this, ChatBotActivity::class.java))
        }

        fetchRealtimeDonations()
    }

    private fun handleDonationAction(donation: Donation) {
        val currentNgoId = auth.currentUser?.uid ?: return

        if (donation.status == "Accepted") {
            db.collection("Donations").document(donation.donationId)
                .update("status", "Completed")
                .addOnSuccessListener {
                    Toast.makeText(this, "Food marked as Collected! ✅", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            return
        }

        // Standard Accept Logic
        db.collection("Users").document(currentNgoId).get()
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    val ngoNameFromProfile = userDoc.getString("name") ?: "An NGO"

                    val updates = hashMapOf<String, Any>(
                        "status" to "Accepted",
                        "ngoId" to currentNgoId,
                        "ngoName" to ngoNameFromProfile
                    )

                    db.collection("Donations").document(donation.donationId)
                        .update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Donation Accepted!", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Set up your NGO name in Profile first!", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun fetchRealtimeDonations() {
        val currentNgoId = auth.currentUser?.uid ?: return

      
        db.collection("Donations")
            .whereIn("status", listOf("pending", "Accepted"))
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("FirestoreError", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    donationList.clear()
                    for (doc in snapshots) {
                        val item = doc.toObject(Donation::class.java)

                      
                        if (item.status == "pending" || item.ngoId == currentNgoId) {
                            donationList.add(item)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }
}
