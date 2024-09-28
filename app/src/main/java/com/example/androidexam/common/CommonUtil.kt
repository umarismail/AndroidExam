package com.example.androidexam.common
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.androidexam.R
import com.example.androidexam.model.ItemsModel
import com.example.androidexam.view.MainActivity


fun filterItemsByName(searchText: String, itemsList: List<ItemsModel>): List<ItemsModel> {
    return itemsList.filter { it.itemName.contains(searchText, ignoreCase = true) }
}
fun filterItemsByCatId(catId: String, itemsList: List<ItemsModel>): List<ItemsModel> {
    return itemsList.filter { it.catId == catId }
}
 fun getTopCharacters(itemList: List<ItemsModel>): List<Map.Entry<Char, Int>> {
    val charCount = mutableMapOf<Char, Int>()

    // Iterate through each item's name
    itemList.forEach { item ->
        item.itemName.forEach { char ->
            if (char.isLetter()) {
                charCount[char] = charCount.getOrDefault(char, 0) + 1
            }
        }
    }

    // Sort characters by their occurrence and get the top 3
    return charCount.entries.sortedByDescending { it.value }.take(3)
}

 fun updateIndicator(position: Int, indicatorLayout: LinearLayout, mainActivity: MainActivity) {
    val count = indicatorLayout.childCount
    for (i in 0 until count) {
        val imageView = indicatorLayout.getChildAt(i) as ImageView
        if (i == position) {
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    mainActivity,
                    R.drawable.circle_selected
                )
            )
        } else {
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    mainActivity,
                    R.drawable.circle_unselected
                )
            )
        }
    }
}
fun loadImage(url: String?, view: ImageView?, context: Context) {
    try {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(view!!)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}
 fun setUpIndicator(count: Int, indicatorLayout: LinearLayout, mainActivity: MainActivity) {
    val indicators = arrayOfNulls<ImageView>(count)
    val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.setMargins(8, 0, 8, 0)

    for (i in indicators.indices) {
        indicators[i] = ImageView(mainActivity)
        indicators[i]?.setImageDrawable(
            ContextCompat.getDrawable(
                mainActivity,
                R.drawable.circle_unselected
            )
        )
        indicators[i]?.layoutParams = params
        indicatorLayout.addView(indicators[i])
    }
}