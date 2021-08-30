package com.example.fantapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CreateUserActivity : AppCompatActivity() {
    var usernameText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_user)
        usernameText = (TextView) findViewById(R.id.cuNewUsernameText)

        val createUserButton = findViewById<Button>(R.id.cuCreateUserButton)
        createUserButton.setOnClickListener {
            val intent = Intent(this, FrontPageActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putString('username', usernameText.text)
        }
        super.onSaveInstanceState(outState)
    }
}