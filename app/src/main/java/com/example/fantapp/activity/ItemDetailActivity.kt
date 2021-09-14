package com.example.fantapp.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.fantapp.CarouselAdapter
import com.example.fantapp.R
import com.example.fantapp.fragment.ImageSlideFragment
import com.example.fantapp.model.Product

class ItemDetailActivity(): AppCompatActivity() {

    class FragmentAdapter(fa: FragmentActivity, private val imageURLs: MutableList<String>): FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return imageURLs.size
        }

        override fun createFragment(position: Int): Fragment {
            val fragment: Fragment = ImageSlideFragment(imageURLs[position])
            return fragment
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_details)
        val bundle: Bundle? = intent.extras
        if (bundle == null) finish()
        val product: Product = bundle?.get("product") as Product

        val titleText: TextView = findViewById(R.id.idTitleText)
        val descriptionText: TextView = findViewById(R.id.idDescriptionText)
        val debugText: TextView = findViewById(R.id.idDebug)
        val priceText: TextView = findViewById(R.id.idPriceText)
        val buyButton: Button = findViewById(R.id.idBuyButton)
        val cancelButton: Button = findViewById(R.id.idCancelButton)
        cancelButton.setOnClickListener {
            finish()
        }

        titleText.text = product.title
        descriptionText.text = product.description
        priceText.text = product.price

        val imageSlider: ViewPager2 = findViewById(R.id.idViewPager)
        imageSlider.adapter = FragmentAdapter(this, product.imageURLs)
    }
}