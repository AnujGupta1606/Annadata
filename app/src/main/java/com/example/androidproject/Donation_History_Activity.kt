package com.anuj.feedforward

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DonationHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_history)

        val rvHistory = findViewById<RecyclerView>(R.id.rvDonationHistory)
        rvHistory.layoutManager = LinearLayoutManager(this)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("Donations")
            .whereEqualTo("donorId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val list = mutableListOf<Donation>()
                for (doc in documents) {
                    val donation = doc.toObject(Donation::class.java)
                    list.add(donation)
                }

                // Updated Adapter initialization to handle clicks
                rvHistory.adapter = DonationAdapter(list) { donationId ->
                    showTrackingDialog(donationId)
                }
            }
    }

    // 🚀 THE NEW TRACKING LOGIC
    private fun showTrackingDialog(donationId: String) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_status_tracking, null)
        dialog.setContentView(view)

        val dotAccepted = view.findViewById<View>(R.id.dotAccepted)
        val dotCollected = view.findViewById<View>(R.id.dotCollected)
        val tvAcceptedText = view.findViewById<TextView>(R.id.tvAcceptedText)

        val db = FirebaseFirestore.getInstance()

        // Listen in real-time to THIS specific donation
        db.collection("Donations").document(donationId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null || !snapshot.exists()) return@addSnapshotListener

                val status = snapshot.getString("status") ?: "Pending"
                val ngoName = snapshot.getString("ngoName") ?: "an NGO"

                when (status) {
                    "Accepted" -> {
                        dotAccepted.setBackgroundResource(R.drawable.step_circle_green)
                        tvAcceptedText.text = "Accepted by $ngoName"
                        tvAcceptedText.setTextColor(resources.getColor(android.R.color.black))
                    }
                    "Completed" -> {
                        dotAccepted.setBackgroundResource(R.drawable.step_circle_green)
                        dotCollected.setBackgroundResource(R.drawable.step_circle_green)
                        tvAcceptedText.text = "Accepted by $ngoName"
                    }
                }
            }

        dialog.show()
    }
}
