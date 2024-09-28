package com.example.androidexam.repository

import android.content.Context
import com.example.androidexam.model.ImageModel
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageRepository@Inject constructor(@ApplicationContext private val context: Context) {
    private val imageList: MutableList<ImageModel> = mutableListOf()

    suspend fun getImageUrls(): List<ImageModel> {
        if (imageList.isEmpty()) {
            val json: String = context.assets.open("images.json").bufferedReader().use {
                it.readText()
            }

            val gson = Gson()
            imageList.addAll(gson.fromJson(json, Array<ImageModel>::class.java).toList())
        }
        return imageList
    }
}