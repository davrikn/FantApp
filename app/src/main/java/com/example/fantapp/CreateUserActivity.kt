package com.example.fantapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CreateUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_user)

        val createUserButton = findViewById<Button>(R.id.createNewUserButton)
        createUserButton.setOnClickListener {
            val intent = Intent(this, FrontPageActivity::class.java)
            startActivity(intent)
        }
    }
}