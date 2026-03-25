package com.example.feedforward

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NgoProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_profile)

        val etNgoName = findViewById<TextInputEditText>(R.id.etNgoName)
        val btnSave = findViewById<Button>(R.id.btnSaveProfile)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

   
        db.collection("Users").document(userId).get().addOnSuccessListener {
            etNgoName.setText(it.getString("name"))
        }

        btnSave.setOnClickListener {
            val name = etNgoName.text.toString().trim()
            if (name.isEmpty()) return@setOnClickListener

            db.collection("Users").document(userId)
                .update("name", name)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }
}
