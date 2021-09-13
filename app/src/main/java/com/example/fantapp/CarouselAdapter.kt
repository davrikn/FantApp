package com.example.fantapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso

class CarouselAdapter(context: Context, private val imagePaths: MutableList<String>): PagerAdapter() {
    private var apiURL = "http://10.0.2.2:8080/api/"
    private var imageURL: String = apiURL+"fant/photo/"
    val context: Context = context

    override fun getCount(): Int {
        return imagePaths.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj;
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
        Picasso.get()
            .load(imageURL + imagePaths[position])
            .placeholder(R.drawable.beans)
            .centerCrop()
            .fit()
            .noFade()
            .into(imageView);
        container.addView(imageView, 0)
        return imageView
    }
    //https://youtu.be/Q2FPDI99-as
}