package com.example.fantapp.repository

import com.android.volley.toolbox.Volley
import com.example.fantapp.model.Product

class ProductRepository {
    private var products: ArrayList<Product> = ArrayList()

    init {
        updateProducts()
    }

    private fun updateProducts() {
    }
}