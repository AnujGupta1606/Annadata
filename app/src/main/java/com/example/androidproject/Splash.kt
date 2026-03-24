package com.example.feedforward

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatus()
        }, 2000)
    }

    private fun checkUserStatus() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
           
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
          
            fetchUserRoleAndRedirect(currentUser.uid)
        }
    }

    private fun fetchUserRoleAndRedirect(uid: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("Users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val role = document.getString("role")

                    val intent = if (role.equals("NGO", ignoreCase = true)) {
                        Intent(this, NgoDashboardActivity::class.java)
                    } else {
                        Intent(this, HomeActivity::class.java)
                    }

                    startActivity(intent)
                    finish()
                } else {
              
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            .addOnFailureListener {
        
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }
}
