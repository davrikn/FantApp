package com.example.fantapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.fantapp.model.DataTypes
import com.example.fantapp.R

class LoginActivity : AppCompatActivity() {

    private val CREATE_USER: Int = 1
    private var usernameText: TextView? = null
    private var passwordText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        usernameText = findViewById(R.id.loUsernameText)
        passwordText = findViewById(R.id.loPasswordText)

        val newUserButton = findViewById<Button>(R.id.loCreateNewUserButton)
        newUserButton.setOnClickListener{
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivityForResult(intent, CREATE_USER)
        }

        val loginButton = findViewById<Button>(R.id.loLoginButton)
        loginButton.setOnClickListener{
            var resultintent = Intent()
            resultintent.putExtra(DataTypes.USERNAME, usernameText?.text?.toString())
            resultintent.putExtra(DataTypes.PASSWORD, passwordText?.text?.toString())
            setResult(Activity.RESULT_OK, resultintent)
            finish()
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