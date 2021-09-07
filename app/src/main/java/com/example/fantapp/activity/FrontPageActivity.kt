package com.example.fantapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.fantapp.ProductAdapter
import com.example.fantapp.R
import com.example.fantapp.UserObserver
import com.example.fantapp.model.Product
import com.example.fantapp.model.User
import org.json.JSONObject

class FrontPageActivity: AppCompatActivity(), UserObserver{
    private var userlabel: TextView? = null
    private var productURL: String = "http://host.docker.internal:8080/api/get"
    private var products: ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        products.add(Product("123", "Freaky stuff", null))
        products.add(Product("13123", "Breaky stuff", null))
        products.add(Product("12323", "Sneaky stuff", null))
        products.add(Product("3", "Keeky stuff", null))

        User.observe(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.front_page)
        println("init")
        //loadProducts()
        val list: RecyclerView = findViewById(R.id.fpRecyclerView)
        list.adapter = ProductAdapter(products)
        list.layoutManager = LinearLayoutManager(this)
        userlabel = findViewById(R.id.fpUsernameText)
        userlabel?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun userUpdate() {
        super.userUpdate()
        userlabel?.text = User.getInstance().getUsername()
    }

    /*
    fun loadProducts() {
        val queue = Volley.newRequestQueue(this)
        println("Load prods")
        val request = JsonArrayRequest(Request.Method.GET, productURL, null, { response ->
            println("BEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEANS")
            val n = response.length()
            for (i in 0 until n) {
                var obj: JSONObject = response.getJSONObject(i)
                var description: String = obj["description"].toString()
                var price: String = obj["price"].toString()
                val product = Product(price, description, null)

                println(product)
            }
        }, {error ->

        } )
        queue.add(request)
    }
    */
}