package com.example.fantapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fantapp.model.DataTypes
import com.example.fantapp.R

class FrontPageActivity: AppCompatActivity() {
    private val LOGIN_ACTION: Int = 1
    private var userlabel: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.front_page)

        userlabel = findViewById<TextView>(R.id.fpUsernameText)
        userlabel?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, LOGIN_ACTION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOGIN_ACTION -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        println("Login successful")
                        val username = data?.extras?.get(DataTypes.USERNAME).toString()
                        if (username != null) {
                            userlabel?.text = username
                        }
                    }
                }
            }
        }
    }
}