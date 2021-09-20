package com.example.fantapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class FrontPageActivity: AppCompatActivity(), UserObserver{
    private var userlabel: TextView? = null
    private var apiURL = "http://10.0.2.2:8080/api/"
    private var productURL: String = apiURL+"fant"
    private var products: ArrayList<Product> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var addItemButton: FloatingActionButton? = null
    private var logoutButton: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO search functionality
        User.observe(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.front_page)
        recyclerView = findViewById(R.id.fpRecyclerView)
        fun adapterOnClick(product: Product) {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("product", product)
            this.startActivity(intent)
        }

        recyclerView?.adapter = ProductAdapter(products, this, {product: Product -> adapterOnClick(product) })
        recyclerView?.layoutManager = LinearLayoutManager(this)

        userlabel = findViewById(R.id.fpUsernameText)
        userlabel?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        userlabel?.bringToFront()

        addItemButton = findViewById(R.id.fpAddItemButton)
        addItemButton?.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        logoutButton = findViewById(R.id.fpLogout)
        logoutButton?.setOnClickListener {
            User.logout()
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        logoutButton?.text = "Logout"
        logoutButton?.isVisible = false

    }

    override fun userUpdate() {
        super.userUpdate()
        userlabel?.text = User.getInstance().getUsername()
    }

    private fun loadProducts() {
        products.clear()
        /*
        products.add(Product(123123,"123", "Long description", "Wow what a product", ArrayList()))
        products.add(Product(1111, "13123", "Cool things", "Selling cool things", ArrayList()))
        products.add(Product(312312,"12323", "Nvidia rtx 3080, ryzen 7 5800h", "Selling pc", ArrayList()))
        products.add(Product(8753455,"3", "Alrite", "Item for sale", ArrayList()))
         */
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(Request.Method.GET, productURL, null, { response ->
            val n = response.length()
            for (i in 0 until n) {
                var obj: JSONObject = response.getJSONObject(i)
                var description: String = obj["description"].toString()
                var price: String = obj["price"].toString()
                val id: Int = obj["id"] as Int
                val imageURLs: ArrayList<String> = ArrayList()
                val photos = obj.getJSONArray("photos")
                val n: Int = photos.length()
                for (i in 0..n-1) {
                    imageURLs.add(photos.getJSONObject(i)["subpath"].toString())
                }
                val title: String = obj["title"].toString()
                val product = Product(id, price, description, title, imageURLs)
                products.add(product)
                recyclerView?.adapter?.notifyDataSetChanged()
            }

        }, {error ->
            error.printStackTrace()
        } )
        queue.add(request)
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
        if (!User.isLoggedIn()) {
            userlabel?.text = "Login"
            addItemButton?.hide()
        } else {
            userlabel?.text = User.getInstance().getUsername()
            addItemButton?.show()
            logoutButton?.isVisible = true
        }
    }

}