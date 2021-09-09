package com.example.fantapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fantapp.model.DataTypes
import com.example.fantapp.R
import com.example.fantapp.model.User

class LoginActivity : AppCompatActivity() {

    private var usernameText: TextView? = null
    private var passwordText: TextView? = null
    private var debugText: TextView? = null
    private val url: String = "http://10.0.2.2:8080/api/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        usernameText = findViewById(R.id.loUsernameText)
        passwordText = findViewById(R.id.loPasswordText)
        debugText = findViewById(R.id.loDebugText)

        var queue = Volley.newRequestQueue(this)
        val newUserButton = findViewById<Button>(R.id.loCreateNewUserButton)
        newUserButton.setOnClickListener{
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
            finish()
        }

        val loginButton = findViewById<Button>(R.id.loLoginButton)
        loginButton.setOnClickListener{
            var requrl = url + "auth/login?" +
                    DataTypes.USERNAME + "=" +
                    usernameText?.text?.toString() + "&" +
                    DataTypes.PASSWORD + "=" +
                    passwordText?.text?.toString()
            val request = StringRequest(Request.Method.GET, requrl, { response ->
                val user = User.getInstance()
                user.setPassword(passwordText?.text?.toString())
                user.setUsername(usernameText?.text?.toString())
                user.setToken(response.toString())
                finish()
            }, { error ->

                debugText?.text = error.toString()
            })
            queue.add(request)
        }
    }


}