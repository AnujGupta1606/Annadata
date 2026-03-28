package com.anuj.feedforward

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties // Prevents crashes if Firestore has extra fields not defined here
data class Donation(
    val donationId: String = "",
    val foodName: String = "",
    val quantity: String = "",
    val location: String = "",
    val donorName: String = "",
    val phoneNumber: String = "",
    val status: String = "pending", 
    val donorId: String = "",
    val timestamp: Long = 0,

 
    val ngoName: String = "",
    val ngoId: String = ""
)
