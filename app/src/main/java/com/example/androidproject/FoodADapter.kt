package com.anuj.feedforward

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(
    private val donationList: List<Donation>,
    private val onAcceptClick: (Donation) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFoodInitial: TextView = itemView.findViewById(R.id.tvFoodInitial)
        val tvFoodName: TextView = itemView.findViewById(R.id.tvFoodName)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_card, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val donation = donationList[position]

        holder.tvFoodName.text = donation.foodName
        holder.tvQuantity.text = "Qty: ${donation.quantity}"
        holder.tvLocation.text = donation.location

        if (donation.foodName.isNotEmpty()) {
            holder.tvFoodInitial.text = donation.foodName.substring(0, 1).uppercase()
        }

        // --- NEW LOGIC FOR DYNAMIC BUTTON ---
        // This handles the visual switch between Step 2 (Accept) and Step 3 (Collect)
        if (donation.status == "Accepted") {
            holder.btnAccept.text = "Mark as Collected"
            holder.btnAccept.setBackgroundColor(Color.parseColor("#22C55E")) // Success Green
        } else {
            holder.btnAccept.text = "Accept Donation"
            holder.btnAccept.setBackgroundColor(Color.parseColor("#FF6D00")) // Theme Orange
        }

        holder.btnAccept.setOnClickListener {
            onAcceptClick(donation)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FoodDetailActivity::class.java).apply {
                putExtra("donationId", donation.donationId)
                putExtra("foodName", donation.foodName)
                putExtra("quantity", donation.quantity)
                putExtra("location", donation.location)
                putExtra("donorName", donation.donorName)
                putExtra("phoneNumber", donation.phoneNumber)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = donationList.size
}
