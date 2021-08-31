package com.example.fantapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

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
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
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
                setResult(resultCode, data)
                finish()
            }
        }
    }
}