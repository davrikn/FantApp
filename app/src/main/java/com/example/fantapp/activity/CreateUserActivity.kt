package com.example.fantapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fantapp.model.DataTypes
import com.example.fantapp.R
import com.example.fantapp.model.User
import org.json.JSONObject

class CreateUserActivity : AppCompatActivity() {
    private var usernameText: TextView? = null
    private var passwordText: TextView? = null
    private var createUserJson: JSONObject = JSONObject()
    private var queue: RequestQueue? = null
    private val url: String = "http://10.0.2.2:8080/api/"
    private var errors: ArrayList<String> = ArrayList<String>()
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
            val requrl = url + "auth/create?" +
                    DataTypes.USERNAME + "=" +
                    usernameText?.text.toString() + "&" +
                    DataTypes.PASSWORD + "=" +
                    passwordText?.text.toString()


            val request: StringRequest = object : StringRequest(
                Method.POST, requrl,
                Response.Listener { response ->
                    doLogin(usernameText?.text.toString(), passwordText?.text.toString())
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    debug?.text = error.toString()
                }) {
                override fun getBodyContentType(): String {
                    return "application/x-www-form-urlencoded; charset=UTF-8"
                }

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["uid"] = usernameText?.text.toString()
                    params["pwd"] = passwordText?.text.toString()
                    return params
                }
            }
            queue?.add(request)
        }
    }

    private fun doLogin(uid: String, pwd: String) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, url + "auth/login?uid=" + uid + "&pwd=" + pwd,
            { response ->
                //set token of global user object
                val user = User.getInstance()
                user.setPassword(passwordText?.text?.toString())
                user.setUsername(usernameText?.text?.toString())
                user.setToken(response.toString())
                user.login()

                //goto browse
                finish()
            },
            { error ->
                debug?.text = error.toString()
            }
        )
        //wait for magic to happen
        queue.add(stringRequest)
    }
}
