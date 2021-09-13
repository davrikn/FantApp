package com.example.fantapp.model

import android.os.Parcelable
import java.io.Serializable

data class Product(var id: Int, var price: String, var description: String, var title: String, var imageURLs: ArrayList<String>): Serializable
