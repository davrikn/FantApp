package com.example.fantapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fantapp.model.Product

class ProductAdapter( private val products: ArrayList<Product>): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descriptionText: TextView
        val priceText: TextView

        init {
            descriptionText = view.findViewById(R.id.productDescription)
            priceText = view.findViewById(R.id.priceText)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fragment, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.descriptionText.text = products[position].description
        holder.priceText.text = products[position].price
    }

    override fun getItemCount(): Int {
        return products.size
    }
}