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
    private val queue: RequestQueue = Volley.newRequestQueue(this)
    private val url: String = "http://127.0.0.1:8080"
    private var errors = null
    private var result: Intent = Intent()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_user)
        usernameText = findViewById(R.id.cuNewUsernameText)
        passwordText = findViewById(R.id.cuNewPasswordText)
        createUserJson.put(DataTypes.USERNAME, usernameText?.text?.toString())
        createUserJson.put(DataTypes.PASSWORD, passwordText?.text?.toString())

        val createUserButton = findViewById<Button>(R.id.cuCreateUserButton)
        createUserButton.setOnClickListener {

            val request = JsonObjectRequest(Request.Method.POST, url, createUserJson,
                { response ->
                    run {
                        "Response: %s".format(response.toString())
                        result.putExtra(DataTypes.USERNAME, usernameText?.text?.toString())
                        result.putExtra(DataTypes.PASSWORD, passwordText?.text?.toString())
                        finish()
                    }
                },
                {
                    //TODO: Handle errors
                })
            queue.add(request)
            

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
