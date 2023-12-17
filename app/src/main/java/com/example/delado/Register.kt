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

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        emailEditText = findViewById(R.id.editemail)
        passwordEditText = findViewById(R.id.editpass)

        findViewById<View>(R.id.btnreg).setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(email, password)
            } else {
                Toast.makeText(this@Register, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<View>(R.id.loginnow).setOnClickListener {
            startActivity(Intent(this@Register, Login::class.java))
            finish()
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task: Task<*> ->
                if (task.isSuccessful) {
                    // Registration successful
                    startActivity(Intent(this@Register, MainActivity::class.java))
                    finish()
                } else {
                    // If registration fails, display a message to the user.
                    Toast.makeText(this@Register, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
