package com.example.delado

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        emailEditText = findViewById(R.id.editemail)
        passwordEditText = findViewById(R.id.editpass)

        findViewById<View>(R.id.btnlogin).setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this@Login, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<View>(R.id.registernow).setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task: Task<*> ->
                if (task.isSuccessful) {
                    // Login successful
                    startActivity(Intent(this@Login, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Login, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
