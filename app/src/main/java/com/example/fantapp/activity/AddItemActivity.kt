package com.example.fantapp.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fantapp.R

class AddItemActivity: AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    var productImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        val cancelButton: Button = findViewById(R.id.aiCancelButton)
        cancelButton.setOnClickListener {
            finish()
        }

        val productDescription: TextView = findViewById(R.id.aiDescriptionText)
        val productTitle: TextView = findViewById(R.id.aiTitleText)
        val priceText: TextView = findViewById(R.id.aiPriceText)
        productImage = findViewById(R.id.aiProductImage)
        val addButton: Button = findViewById(R.id.aiAddButton)
        addButton.setOnClickListener {
            // TODO post product
        }

        productImage?.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            productImage?.setImageBitmap(imageBitmap)
        }

    }

}