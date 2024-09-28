package com.example.androidexam.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.androidexam.R
import com.example.androidexam.adapters.ImageAdapter
import com.example.androidexam.adapters.ItemsAdapter
import com.example.androidexam.common.filterItemsByCatId
import com.example.androidexam.common.filterItemsByName
import com.example.androidexam.common.setUpIndicator
import com.example.androidexam.common.updateIndicator
import com.example.androidexam.databinding.ActivityMainBinding
import com.example.androidexam.model.ItemsModel
import com.example.androidexam.viewModel.ImageViewModel
import com.example.androidexam.viewModel.ItemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewModelItem: ItemsViewModel
    private  var catId: String=""
    private var itemList: List<ItemsModel> = emptyList()
    private lateinit  var binding: ActivityMainBinding
    private lateinit var itemsAdapter: ItemsAdapter
    private lateinit var viewModel: ImageViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)

// Make the status bar icons dark to be visible on the white background
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        getDataFromViewModel()
        viewPagerControl()
         listeners()
    }
    private fun getDataFromViewModel() {
        viewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        viewModel.imageItemData
            .observe(this@MainActivity, Observer { imageItemData ->


                if (imageItemData != null) {
                    binding.viewPager.adapter = ImageAdapter(imageItemData)
                    // Initialize the indicator with the number of pages
                    setUpIndicator(imageItemData.size, binding.indicatorLayout,this@MainActivity)

                }
            })

        viewModel.refreshImageData()

        //itemViewModel
        viewModelItem = ViewModelProvider(this)[ItemsViewModel::class.java]
        viewModelItem.itemData
            .observe(this@MainActivity) { itemsList ->


                if (itemsList != null) {
                    itemList = itemsList
                    val updatedItemList = filterItemsByCatId("1", itemList)
                    initItemRecyclerView(updatedItemList)
                }
            }


        viewModelItem.refreshItemData()
    }


    private fun listeners() {
        binding.searchEditText.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = s.toString()
                val filteredList = filterItemsByName(searchText, itemList)
                // Update your RecyclerView adapter with the filtered list
                itemsAdapter.updateData(filteredList)
            }
            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
    private fun viewPagerControl() {

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                catId=(position+1).toString()

                val updatedItemList=filterItemsByCatId(catId,itemList)
                Log.d("sdfsd"," updatedItemList   $updatedItemList")
                initItemRecyclerView(updatedItemList)
                updateIndicator(position, binding.indicatorLayout,this@MainActivity)
            }
        })
        binding.viewPager.currentItem = 0
    }

    private fun initItemRecyclerView(updatedItemList: List<ItemsModel>) {
        itemsAdapter = ItemsAdapter(updatedItemList,  this@MainActivity)
        binding.itemRv.adapter = itemsAdapter
        binding.itemRv.layoutManager = LinearLayoutManager(this@MainActivity)
    }


}