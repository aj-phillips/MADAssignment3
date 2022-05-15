package com.example.assignment3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.firebase.client.Firebase
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    private lateinit var emailField : EditText
    private lateinit var passwordField : EditText
    private lateinit var loginButton : Button

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        emailField = findViewById(R.id.emailAddress)
        passwordField = findViewById(R.id.loginPassword)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            startSignIn()
        }
    }

    private fun startSignIn() {
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this.applicationContext, "Email or password are empty!",
                Toast.LENGTH_SHORT).show()
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this.applicationContext, "Invalid credentials or other problem",
                        Toast.LENGTH_SHORT).show()
                }
                createMainActivity()
            }
        }
    }

    private fun createMainActivity() {
        val openMainActivity = Intent(this, MainActivity::class.java)
        startActivity(openMainActivity)
    }
}