package com.example.fantapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val newUserButton = findViewById<Button>(R.id.loCreateNewUserButton)
        newUserButton.setOnClickListener{
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.loLoginButton)
        loginButton.setOnClickListener{
            val intent = Intent(this, FrontPageActivity::class.java)
            startActivity(intent)
        }
    }
}