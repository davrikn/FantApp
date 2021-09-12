package com.example.fantapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fantapp.model.Product
import com.squareup.picasso.Picasso

class ProductAdapter( private val products: MutableList<Product>): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var apiURL = "http://10.0.2.2:8080/api/"
    private var imageURL: String = apiURL+"fant/photo/"

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descriptionText: TextView
        val priceText: TextView
        val image: ImageView
        val titleText: TextView

        init {
            descriptionText = view.findViewById(R.id.ifProductDescription)
            priceText = view.findViewById(R.id.ifPriceText)
            image = view.findViewById(R.id.ifProductImage)
            titleText = view.findViewById(R.id.ifProductTitle)
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
        holder.titleText.text = products[position].title
        //holder.image.setImageBitmap(getImageBitmap(products[position].imageURL))
        setImageBitmap(products[position].imageURL, holder.image)
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