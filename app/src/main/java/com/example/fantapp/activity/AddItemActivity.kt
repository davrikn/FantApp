package com.example.fantapp.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.fantapp.R
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import android.os.Environment

import androidx.core.content.FileProvider
import com.example.fantapp.model.User
import org.apache.http.HttpEntity
import org.apache.http.HttpHeaders
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.DefaultHttpClient
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils


class AddItemActivity: AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private var addImageButton: Button? = null
    private var imageContainer: LinearLayout? = null
    private var apiURL = "http://10.0.2.2:8080/api/"
    private var addURL = apiURL + "fant/create"
    private var debug: TextView? = null
    private val selected: ArrayList<View> = ArrayList()
    private var removeSelectedButton: Button? = null
    val FILEPROVIDER = "com.example.fantapp.fileprovider"
    var currentPhoto: File? = null
    var photos: ArrayList<File> = ArrayList()


    private class Requester(private val activity: AppCompatActivity, private val title: String,
                            private val description: String, private val price: String,
                            private val photos: ArrayList<File>, private val addURL: String): Thread() {
        override fun run() {
            super.run()
            val httpClient: CloseableHttpClient = HttpClients.createDefault()
            val httpPost: HttpPost = HttpPost(addURL)
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + User.getInstance().getToken())

            val titleBody: StringBody = StringBody(title, ContentType.TEXT_PLAIN)
            val descriptionBody: StringBody = StringBody(description, ContentType.TEXT_PLAIN)
            val priceBody: StringBody = StringBody(price, ContentType.TEXT_PLAIN)

            val builder: MultipartEntityBuilder = MultipartEntityBuilder.create()

            builder.addPart("title", titleBody)
            builder.addPart("description", descriptionBody)
            builder.addPart("price", priceBody)

            if (photos.size != 0) {
                photos.forEach {
                    val bin: FileBody = FileBody(it)
                    builder.addPart("files", bin)
                }
            }

            val httpEntity: HttpEntity = builder.build()

            httpPost.entity = httpEntity

            try {
                val response: CloseableHttpResponse = httpClient.execute(httpPost)
                println("========================================================================================================================================================================================================")
                println(response)
                val resEntity = response.entity

                if (resEntity != null) {
                    println("Response content length " + resEntity.contentLength)
                }
                EntityUtils.consume(resEntity)
                activity.finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        val cancelButton: Button = findViewById(R.id.aiCancelButton)
        cancelButton.setOnClickListener {
            finish()
        }

        imageContainer = findViewById(R.id.aiImageLayout)
        debug = findViewById(R.id.aiDebug)
        val productDescription: TextView = findViewById(R.id.aiDescriptionText)
        val productTitle: TextView = findViewById(R.id.aiTitleText)
        val priceText: TextView = findViewById(R.id.aiPriceText)
        addImageButton = findViewById(R.id.aiAddImageButton)
        addImageButton?.setOnClickListener {
            onCameraClick()
            debug?.text = "Clicked addImg";
        }
        removeSelectedButton = findViewById(R.id.aiDeleteSelectedButton)
        removeSelectedButton?.setOnClickListener {
            selected.forEach {
                imageContainer?.removeView(it)
            }
            removeSelectedButton?.isVisible = false
        }
        removeSelectedButton?.isVisible = false
        val addButton: Button = findViewById(R.id.aiAddButton)
        addButton.setOnClickListener {
            val requester: Requester = Requester(this, productTitle.text.toString(), productDescription.text.toString(),
                priceText.text.toString(), photos, addURL)
            requester.start()

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
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        try {
            result = File.createTempFile(imageFileName, ".jpg", storageDir)
            println("======================================================================================================================================================================================================================================================================================================================================================")
            println("File is $result")
        } catch (e: IOException) {
            println("======================================================================================================================================================================================================================================================================================================================================================")
            e.printStackTrace()
        }
        return result
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(currentPhoto != null) {
                try {
                    val image: ImageView = ImageView(this)
                    val options = BitmapFactory.Options()
                    options.outWidth = 100
                    options.outHeight = 100
                    var bitmap: Bitmap = BitmapFactory.decodeFile((currentPhoto!!.absolutePath), options)
                    bitmap = Bitmap.createScaledBitmap(bitmap, 250, 250, true);
                    image.setImageBitmap(bitmap)
                    image.setOnLongClickListener {
                        if (selected.contains(it)) {
                            selected.remove(it)
                        } else {
                            selected.add(it)
                        }
                        removeSelectedButton?.isVisible = selected.size != 0

                        false
                    }
                    //image.maxHeight = 10
                    //image.maxWidth = 10
                    imageContainer?.addView(image)
                    photos.add(currentPhoto!!)
                    currentPhoto = null
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

    }

}