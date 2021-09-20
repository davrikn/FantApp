package com.example.fantapp.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fantapp.R
import com.example.fantapp.model.User
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.content.FileProvider
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AddItemActivity: AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private var addImageButton: Button? = null
    private var imageContainer: LinearLayout? = null
    private var apiURL = "http://10.0.2.2:8080/api/"
    private var addURL = apiURL + "fant/create"
    val EXTRA_CONVERSATION = "com.example.fantapp.CONVERSATION"
    val FILEPROVIDER = "com.example.fantapp.fileprovider"
    var currentPhoto: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        val cancelButton: Button = findViewById(R.id.aiCancelButton)
        cancelButton.setOnClickListener {
            finish()
        }
        imageContainer = findViewById(R.id.aiImageLayout)
        val debug: TextView = findViewById(R.id.aiDebug)
        val productDescription: TextView = findViewById(R.id.aiDescriptionText)
        val productTitle: TextView = findViewById(R.id.aiTitleText)
        val priceText: TextView = findViewById(R.id.aiPriceText)
        addImageButton = findViewById(R.id.aiAddImageButton)
        val addButton: Button = findViewById(R.id.aiAddButton)
        addButton.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val request: StringRequest = object : StringRequest(
                Method.POST, addURL,
                Response.Listener { response ->
                    // TODO handle response
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    debug?.text = error.toString()
                }) {
                override fun getBodyContentType(): String {
                    return "multipart/form-data"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", "Bearer " + User.getInstance().getToken() )
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["title"] = productTitle?.text.toString()
                    params["description"] = productDescription?.text.toString()
                    params["price"] = priceText.text.toString()
                    val images = ArrayList<Bitmap>()
                    imageContainer?.forEach {
                        it as ImageView
                        images.add((it.getDrawable() as BitmapDrawable).bitmap)
                    }

                    return params
                }
            }
            queue.add(request)

            // TODO post product
        }

        addImageButton?.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }

    }

    fun onCameraClick() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            currentPhoto = createImageFile()
            if (currentPhoto != null) {
                val photoURI: Uri = FileProvider.getUriForFile(this, FILEPROVIDER, currentPhoto!!)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun createImageFile(): File? {
        var result: File? = null

        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        try {
            result = File.createTempFile(imageFileName, ".jpg", storageDir)
            println("File is $result")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val image: ImageView = ImageView(this)
            image.setImageBitmap(imageBitmap)
            image.maxWidth = 100
            image.maxHeight = 100
            imageContainer?.addView(image)
        }

    }

}