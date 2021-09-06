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

    private val CREATE_USER: Int = 1
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
            startActivityForResult(intent, CREATE_USER)
        }

        val loginButton = findViewById<Button>(R.id.loLoginButton)
        loginButton.setOnClickListener{
            var requrl = url + "auth/login?" +
                    DataTypes.USERNAME + "=" +
                    usernameText?.text?.toString() + "&" +
                    DataTypes.PASSWORD + "=" +
                    passwordText?.text?.toString()
            println(requrl)
            val request = StringRequest(Request.Method.GET, requrl, { response ->
                var resultintent = Intent()
                resultintent.putExtra(DataTypes.USERNAME, usernameText?.text?.toString())
                resultintent.putExtra(DataTypes.PASSWORD, passwordText?.text?.toString())
                setResult(Activity.RESULT_OK, resultintent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CREATE_USER -> {
                val result = Intent()
                data?.extras?.keySet()?.forEach( fun(key: String) {
                    val value: String? = data?.extras?.get(key) as String?
                    result?.putExtra(key, value)
                })
                setResult(resultCode, result)
                println("Login from create user")
                println(data)
                println("Data uname:")
                println(data?.extras?.get(DataTypes.USERNAME))
                println("Result uname:")
                println(result?.extras?.get(DataTypes.USERNAME))
                result.extras?.keySet()?.forEach(fun(key: String) {
                    println("key: " + key + " value: " + result?.extras?.get(key))
                })
                finish()
            }
        }
    }
}