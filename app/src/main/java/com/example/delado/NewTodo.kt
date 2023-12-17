package com.example.delado

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap

class NewTodo : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var namakegiatan: EditText
    private lateinit var desc: EditText
    private lateinit var deadline: EditText
    private lateinit var buttonadd: Button
    private lateinit var buttoncancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_todo)

        namakegiatan = findViewById(R.id.editaddnamakegiatan)
        desc = findViewById(R.id.editadddesc)
        deadline = findViewById(R.id.editadddeadline)

        buttonadd = findViewById(R.id.btnadd)
        buttoncancel = findViewById(R.id.btncancel)
        auth = FirebaseAuth.getInstance()

        buttonadd.setOnClickListener {
            insertData()
            clearAll()
        }

        buttoncancel.setOnClickListener {
            val intent = Intent(this@NewTodo, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun insertData() {
        // Check if the user is authenticated
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Get the user's ID
            val userId: String = currentUser.uid

            // Create a map with the user's ID and other activity details
            val map: MutableMap<String, Any> = HashMap()
            map["userId"] = userId
            map["namakegiatan"] = namakegiatan.text.toString()
            map["desc"] = desc.text.toString()
            map["deadline"] = deadline.text.toString()

            // Save the data in the database under a specific user's node
            FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("todo").push().setValue(map)
                .addOnSuccessListener {
                    Toast.makeText(this@NewTodo, "New Activity Added", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    Toast.makeText(this@NewTodo, "Failed", Toast.LENGTH_SHORT).show()
                    // added
                    Log.e("NewTodo", "Error adding new activity", e)
                }
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(this@NewTodo, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearAll() {
        namakegiatan.text = null
        desc.text = null
        deadline.text = null
    }
}
