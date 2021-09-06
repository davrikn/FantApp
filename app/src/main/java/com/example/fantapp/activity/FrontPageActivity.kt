package com.example.fantapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fantapp.model.DataTypes
import com.example.fantapp.R
import com.example.fantapp.UserObserver
import com.example.fantapp.model.User

class FrontPageActivity: AppCompatActivity(), UserObserver{
    private val LOGIN_ACTION: Int = 1
    private var userlabel: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        User.observe(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.front_page)

        userlabel = findViewById<TextView>(R.id.fpUsernameText)
        userlabel?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    
    override fun userUpdate() {
        super.userUpdate()
        userlabel?.text = User.getInstance().getUsername()
    }
}