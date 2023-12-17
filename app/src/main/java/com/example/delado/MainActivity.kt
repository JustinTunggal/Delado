package com.example.delado

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private lateinit var auth: FirebaseAuth
    private lateinit var userTodoRef: DatabaseReference
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvAdaptor: RVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        checkUserAuthentication()
        val currentUser: FirebaseUser? = auth.currentUser

        currentUser?.let {
            // Initialize DatabaseReference for the user's to-do items
            val userId: String = auth.currentUser!!.uid
            userTodoRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("todo")
        }

        recyclerView = findViewById(R.id.rvtodo)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (::userTodoRef.isInitialized) {
            val options: FirebaseRecyclerOptions<RVModel> = FirebaseRecyclerOptions.Builder<RVModel>()
                .setQuery(userTodoRef, RVModel::class.java)
                .build()

            rvAdaptor = RVAdapter(options, userId)
            recyclerView.adapter = rvAdaptor
        }

        floatingActionButton = findViewById(R.id.addbtn)
        floatingActionButton.setOnClickListener {
            startActivity(Intent(applicationContext, NewTodo::class.java))
        }
    }

    private fun checkUserAuthentication() {
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser == null) {
            // If the user is not signed in, redirect to the login activity
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        rvAdaptor.startListening()
    }

    override fun onStop() {
        super.onStop()
        rvAdaptor.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val item: MenuItem = menu.findItem(R.id.search)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                textSearch(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                textSearch(query)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun textSearch(str: String) {
        val options: FirebaseRecyclerOptions<RVModel>

        options = if (str.isEmpty()) {
            FirebaseRecyclerOptions.Builder<RVModel>()
                .setQuery(
                    FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                        .child("todo"), RVModel::class.java
                )
                .build()
        } else {
            FirebaseRecyclerOptions.Builder<RVModel>()
                .setQuery(
                    FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                        .child("todo").orderByChild("namakegiatan").startAt(str).endAt(str + "-"),
                    RVModel::class.java
                )
                .build()
        }

        rvAdaptor = RVAdapter(options, userId)
        rvAdaptor.startListening()
        recyclerView.adapter = rvAdaptor
    }
}
