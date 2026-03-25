package com.example.feedforward

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hbb20.CountryCodePicker

class DonateFoodActivity : AppCompatActivity() {

    private lateinit var etFoodName: TextInputEditText
    private lateinit var etQuantity: TextInputEditText
    private lateinit var etLocation: TextInputEditText
    private lateinit var etDonorName: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var btnSubmit: Button
    private lateinit var ccp: CountryCodePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate_food)


        etFoodName = findViewById(R.id.etFoodName)
        etQuantity = findViewById(R.id.etQuantity)
        etLocation = findViewById(R.id.etLocation)
        etDonorName = findViewById(R.id.etDonorName)
        etPhone = findViewById(R.id.etPhone)
        btnSubmit = findViewById(R.id.btnSubmitDonation)
        ccp = findViewById(R.id.ccp)

        ccp.registerCarrierNumberEditText(etPhone)

        btnSubmit.setOnClickListener {
            saveDonationToFirestore()
        }
    }

    private fun saveDonationToFirestore() {
        val fName = etFoodName.text.toString().trim()
        val qty = etQuantity.text.toString().trim()
        val loc = etLocation.text.toString().trim()
        val dName = etDonorName.text.toString().trim()
        val phoneInput = etPhone.text.toString().trim()

     
        if (fName.isEmpty() || qty.isEmpty() || loc.isEmpty() || dName.isEmpty() || phoneInput.isEmpty()) {
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            return
        }


        if (!ccp.isValidFullNumber) {
            etPhone.error = "Invalid number for ${ccp.selectedCountryName}"
            etPhone.requestFocus()
            return
        }

        
        val finalPhoneNumber = ccp.fullNumberWithPlus

        btnSubmit.text = "Posting..."
        btnSubmit.isEnabled = false

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Donations").document() 

        val donation = Donation(
            donationId = docRef.id,
            foodName = fName,
            quantity = qty,
            location = loc,
            donorName = dName,
            phoneNumber = finalPhoneNumber,
            status = "pending",
            donorId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown",
            timestamp = System.currentTimeMillis()
        )

        docRef.set(donation)
            .addOnSuccessListener {
                Toast.makeText(this, "Donation Posted Successfully!", Toast.LENGTH_LONG).show()
            
                finish()
            }
            .addOnFailureListener {
                btnSubmit.isEnabled = true
                btnSubmit.text = "Post Donation"
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
