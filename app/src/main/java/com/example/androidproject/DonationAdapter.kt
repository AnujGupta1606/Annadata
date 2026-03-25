package com.example.feedforward

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DonationAdapter(
    private val donationList: List<Donation>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    class DonationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodName: TextView = view.findViewById(R.id.tvItemFoodName)
        val quantity: TextView = view.findViewById(R.id.tvItemQuantity)
        val status: TextView = view.findViewById(R.id.tvItemStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_donation, parent, false)
        return DonationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val donation = donationList[position]

 
        holder.foodName.text = donation.foodName
        holder.quantity.text = "Qty: ${donation.quantity}"
        holder.status.text = "Status: ${donation.status}"

        holder.itemView.setOnClickListener {
            
            onItemClick(donation.donationId)
        }
    }

    override fun getItemCount() = donationList.size
}
