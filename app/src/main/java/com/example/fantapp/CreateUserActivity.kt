package com.example.fantapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CreateUserActivity : AppCompatActivity() {
    private var usernameText: TextView? = null
    private var passwordText: TextView? = null
    private var createUserJson: JSONObject = JSONObject()
    private var queue: RequestQueue? = null
    private val url: String = "http://127.0.0.1:8080"
    private var errors = null
    private var result: Intent = Intent()
    private var debug: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_user)
        usernameText = findViewById(R.id.cuNewUsernameText)
        passwordText = findViewById(R.id.cuNewPasswordText)
        debug = findViewById(R.id.cuDebug)
        createUserJson.put(DataTypes.USERNAME, usernameText?.text?.toString())
        createUserJson.put(DataTypes.PASSWORD, passwordText?.text?.toString())

        queue = Volley.newRequestQueue(this)
        val createUserButton = findViewById<Button>(R.id.cuCreateUserButton)
        createUserButton.setOnClickListener {
            debug?.text = "Clicked"
            val request = JsonObjectRequest(Request.Method.POST, url, createUserJson,
                Response.Listener { response ->
                    debug?.text = "Response: %s".format(response.toString())
                    result.putExtra(DataTypes.USERNAME, usernameText?.text?.toString())
                    result.putExtra(DataTypes.PASSWORD, passwordText?.text?.toString())
                    finish()
                },
                Response.ErrorListener {
                    debug?.text = "Well shiiiiiet"
                    //TODO: Handle errors
                })
            queue?.add(request)


        }
    }

    override fun onDestroy() {
        var resultCode: Int = Activity.RESULT_CANCELED
        if (errors != null) {

        } else {
            resultCode = Activity.RESULT_OK
        }
        setResult(resultCode, result)
        super.onDestroy()
    }
}
