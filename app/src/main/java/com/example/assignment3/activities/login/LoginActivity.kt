package com.example.assignment3.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.assignment3.R
import com.example.assignment3.activities.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailField : EditText
    private lateinit var passwordField : EditText
    private lateinit var loginButton : Button
    private lateinit var registerButton : Button

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        emailField = findViewById(R.id.emailAddress)
        passwordField = findViewById(R.id.loginPassword)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        loginButton.setOnClickListener {
            startSignIn()
        }

        registerButton.setOnClickListener {
            Toast.makeText(this.applicationContext, "Not implemented", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth.currentUser
        if(currentUser != null){
            createMainActivity()
            finish()
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
                    return@addOnCompleteListener
                }
                createMainActivity()
                finish()
            }
        }
    }

    private fun createMainActivity() {
        val openMainActivity = Intent(this, MainActivity::class.java)
        startActivity(openMainActivity)
    }
}