package com.anuj.feedforward

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var loginBtn: Button
    private lateinit var signupText: TextView
    private lateinit var forgotPasswordText: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI Elements
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginBtn = findViewById(R.id.loginBtn)
        signupText = findViewById(R.id.signupText)
        forgotPasswordText = findViewById(R.id.forgotPasswordText)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        forgotPasswordText.setOnClickListener {
            val emailStr = email.text.toString().trim()
            if (emailStr.isEmpty()) {
                email.error = "Enter your email first"
                email.requestFocus()
            } else {
                sendPasswordReset(emailStr)
            }
        }

        loginBtn.setOnClickListener {
            val e = email.text.toString().trim()
            val p = password.text.toString().trim()

            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            } else {
                performLogin(e, p)
            }
        }

        signupText.setOnClickListener {
            startActivity(Intent(this, RoleSelectionActivity::class.java))
        }
    }

    private fun sendPasswordReset(emailStr: String) {
        auth.sendPasswordResetEmail(emailStr)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Reset link sent to $emailStr", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun performLogin(e: String, p: String) {
        loginBtn.text = "Logging in..."
        loginBtn.isEnabled = false

        auth.signInWithEmailAndPassword(e, p)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userEmail = user?.email

                    // 🚀 GOOGLE REVIEWER BYPASS LOGIC START
                    // Reviewer accounts ko verification check aur role check se pehle hi redirect kar denge
                    if (userEmail == "ngo_test@gmail.com") {
                        val intent = Intent(this, NgoDashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        return@addOnCompleteListener
                    }
                    else if (userEmail == "donor_test@gmail.com") {
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        return@addOnCompleteListener
                    }
                    // 🚀 GOOGLE REVIEWER BYPASS LOGIC END

                    // --- AAPKA NORMAL VERIFICATION CHECK ---
                    if (user != null && user.isEmailVerified) {
                        checkUserRoleAndRedirect()
                    } else {
                        auth.signOut()
                        resetUI()
                        Toast.makeText(this, "Please verify your email address first. Check your inbox!", Toast.LENGTH_LONG).show()
                    }

                } else {
                    resetUI()
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkUserRoleAndRedirect() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("Users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val role = document.getString("role")

                    val nextActivity = if (role.equals("NGO", ignoreCase = true)) {
                        NgoDashboardActivity::class.java
                    } else {
                        HomeActivity::class.java
                    }

                    val intent = Intent(this, nextActivity)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Profile not found in database.", Toast.LENGTH_LONG).show()
                    auth.signOut()
                    resetUI()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Database Error: ${it.message}", Toast.LENGTH_SHORT).show()
                resetUI()
            }
    }

    private fun resetUI() {
        loginBtn.isEnabled = true
        loginBtn.text = "Login"
    }
}
