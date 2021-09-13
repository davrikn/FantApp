package com.example.fantapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.fantapp.R
import com.squareup.picasso.Picasso

class ImageSlideFragment(private val subpath: String) : Fragment() {
    private var apiURL = "http://10.0.2.2:8080/api/"
    private var imageURL: String = apiURL+"fant/photo/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.swipe_view, container, false)
        val image: ImageView = view.findViewById(R.id.swImageView)
        setImageBitmap(subpath, image)
        return view
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
}