package com.example.fantapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fantapp.activity.AddItemActivity
import com.example.fantapp.activity.LoginActivity
import com.example.fantapp.model.Product
import com.squareup.picasso.Picasso

class ProductAdapter( private val products: MutableList<Product>,
                      private val context: Context,
                      private val onClick: (Product) -> Unit ): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var apiURL = "http://10.0.2.2:8080/api/"
    private var imageURL: String = apiURL+"fant/photo/"

    class ViewHolder(view: View, private val onClick: (Product) -> Unit) : RecyclerView.ViewHolder(view) {
        val descriptionText: TextView
        val priceText: TextView
        val image: ImageView
        val titleText: TextView
        var product: Product? = null
    
        init {
            view.setOnClickListener {
                product?.let {
                    println(product.toString())
                    onClick(it)
                }
            }
            descriptionText = view.findViewById(R.id.ifProductDescription)
            priceText = view.findViewById(R.id.ifPriceText)
            image = view.findViewById(R.id.ifProductImage)
            titleText = view.findViewById(R.id.ifProductTitle)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fragment, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.descriptionText.text = products[position].description
        holder.priceText.text = products[position].price
        holder.titleText.text = products[position].title
        holder.product = products[position]
        //holder.image.setImageBitmap(getImageBitmap(products[position].imageURL))
        if (products[position].imageURLs.size > 0) {
            setImageBitmap(products[position].imageURLs?.get(0), holder.image)
        } else {
            setImageBitmap(null, holder.image)
        }
    }

    private fun setImageBitmap(subpath: String?, imageView: ImageView) {
        Picasso.get()
            .load(imageURL + subpath)
            .placeholder(R.drawable.beans)
            .centerCrop()
            .fit()
            .noFade()
            .into(imageView);
    }

    override fun getItemCount(): Int {
        return products.size
    }

}