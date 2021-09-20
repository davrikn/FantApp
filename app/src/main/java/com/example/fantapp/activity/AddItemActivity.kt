package com.example.fantapp.activity

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.example.fantapp.R
import com.example.fantapp.model.User
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import com.example.fantapp.DebugObserver
import com.example.fantapp.model.Product
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random
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


class AddItemActivity: AppCompatActivity(), DebugObserver {
    private val REQUEST_IMAGE_CAPTURE = 1
    private var addImageButton: Button? = null
    private var imageContainer: LinearLayout? = null
    private var apiURL = "http://10.0.2.2:8080/api/"
    private var addURL = apiURL + "fant/create"
    private val boundary: String = "------WebKitFormBoundary6EWVXJwLnzkzGNaD"
    private val retnew: String = "\r\n"
    private val dretnew: String = retnew + retnew
    private val contdis: String = "Content-Disposition: form-data; name=\""
    private var debug: TextView? = null
    private val debugText: DebugText = DebugText()
    private val selected: ArrayList<View> = ArrayList()
    private var removeSelectedButton: Button? = null
    val EXTRA_CONVERSATION = "com.example.fantapp.CONVERSATION"
    val FILEPROVIDER = "com.example.fantapp.fileprovider"
    var currentPhoto: File? = null

    private class DebugText() {
        private var busy = false;
        private var text: String = ""
        private val observers: ArrayList<DebugObserver> = ArrayList()
        override fun toString(): String {
            return text;
        }

        fun updateText(text: String) {
            while (busy) {}
            busy = true
            this.text = text
            notifyObservers()
            busy = false
        }

        private fun notifyObservers() {
            observers.forEach{
                it.debugChange()
            }
        }
    }

    private class FuckYou(
        private val data: String,
        private val addURL: String,
        private val debug: DebugText,
        private val imageContainer: LinearLayout?
    ): Thread() {

        private fun doRequest(data: String): String {
            val url = URL(addURL)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary6EWVXJwLnzkzGNaD")
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("Content-Length", data.encodeToByteArray().size.toString())
            connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept","*/*");
            connection.setRequestProperty("Authorization", "Bearer " + User.getInstance().getToken())
            connection.useCaches = false;
            connection.doInput = true;
            connection.doOutput = true;
            val wr = DataOutputStream(connection.outputStream)
            wr.writeBytes(data)
            wr.flush()
            wr.close()
            if (connection.responseCode == 200) {
                val instream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(instream))
                var line: String = ""
                val response = StringBuffer()
                while ((reader.readLine().also { line = it }) != null) {
                    response.append("$line\r")
                }
                reader.close()
                return response.toString()
            }

            println("============================================================================================================================================================================================================================")
            println(connection.responseCode)
            println(connection.responseMessage)
            println(connection.errorStream)
            return connection.responseCode.toString()

        }

        override fun run() {
            uploadPics(imageContainer)
            val response: String = doRequest(data)
            debug.updateText(response)
        }

        private fun uploadPics(imageContainer: LinearLayout?) {

        }
    }

    override fun debugChange() {
        debug?.text = debugText.toString()
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
        removeSelectedButton = findViewById(R.id.aiDeleteSelectedButton)
        removeSelectedButton?.setOnClickListener {
            selected.forEach {
                imageContainer?.removeView(it)
            }
        }
        //removeSelectedButton?.isVisible = false
        val addButton: Button = findViewById(R.id.aiAddButton)
        addButton.setOnClickListener {
            var data: String = boundary + retnew + contdis + "title\"" + dretnew + productTitle.text.toString() + retnew +
                    boundary + retnew + contdis + "description\"" + dretnew + productDescription.text.toString() + retnew +
                    boundary + retnew + contdis + "price\"" + dretnew + priceText.text.toString() + retnew + boundary

            /*
                    boundary + retnew + contdis + "files\";"
            imageContainer?.forEach {
                it as ImageView
                val fname = Random(69420).nextInt().toString()
                data += "filename=\"" + fname + "\"" + retnew + "Content-Type: image/jpeg" + dretnew + it.drawable.toBitmap().toString() + retnew
            }
             */
            data += "--" + retnew
            //var data: String = "title:" + productTitle.text + retnew +"description:" + productDescription.text + retnew +"price:" + priceText.text

            val t = FuckYou(data, addURL, debugText, imageContainer)
            t.start()
            /*
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
                    params.put("title", productTitle?.text.toString())
                    params.put("description", productDescription?.text.toString())
                    params.put("price", priceText.text.toString())
                    val images = ArrayList<Bitmap>()
                    print("imgs")
                    imageContainer?.forEach {
                        it as ImageView
                        print(it.drawable.toString())
                        images.add((it.getDrawable() as BitmapDrawable).bitmap)
                    }

                    return params
                }
            }
            queue.add(request)

            // TODO post product
             */
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
            image.setOnLongClickListener {
                /*
                    Kunne enkelt endra visibility til removeSelected her men kotlin e fette retarda
                */
                if (selected.contains(it)) {
                    selected.remove(it)
                } else {
                    selected.add(it)
                }
                removeSelectedButton?.isVisible = selected.size != 0

                false
            }
            image.setImageBitmap(imageBitmap)
            image.maxWidth = 100
            image.maxHeight = 100
            imageContainer?.addView(image)
        }

    }

}