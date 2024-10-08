package com.example.androidexam.repository

import android.content.Context
import com.example.androidexam.model.ItemsModel
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemsRepository@Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun getItems(): List<ItemsModel> = withContext(Dispatchers.IO) {
        val json: String = context.assets.open("item.json").bufferedReader().use {
            it.readText()
        }

        val gson = Gson()
        val imageList: List<ItemsModel> = gson.fromJson(json, Array<ItemsModel>::class.java).toList()
        imageList
    }
}