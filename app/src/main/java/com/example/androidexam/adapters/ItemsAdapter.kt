package com.example.androidexam.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidexam.model.ItemsModel
import com.example.androidexam.R
import com.example.androidexam.common.loadImage

class ItemsAdapter(private var items: List<ItemsModel>, context: Activity) :
    RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {
    private var mContext: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("sdfsd", "onBindViewHolder   $items")
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newData: List<ItemsModel>) {
        items = newData
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ItemsModel) {
            itemView.findViewById<TextView>(R.id.itemNameTv).text = item.itemName
            itemView.findViewById<TextView>(R.id.itemSubTitleTv).text = item.subtitle
            val itemImageView = itemView.findViewById<ImageView>(R.id.imageView)
            loadImage(item.imageUrl, itemImageView, mContext)
        }
    }
}