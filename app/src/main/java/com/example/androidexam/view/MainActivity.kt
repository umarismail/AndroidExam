package com.example.androidexam.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.androidexam.databinding.BottomSheetStatisticsBinding
import com.example.androidexam.model.ItemsModel
import com.example.androidexam.viewModel.ImageViewModel
import com.example.androidexam.viewModel.ItemsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var updatedItemList: List<ItemsModel>
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
        //fab btn click
        binding.countBtn.setOnClickListener {
            showBottomSheetDialog()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showBottomSheetDialog() {
        val bottomSheetBinding = BottomSheetStatisticsBinding.inflate(layoutInflater)

        // Create BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        // Set the data in the Bottom Sheet
        val itemCount = updatedItemList.size
        val characterOccurrences = getTopCharacters(updatedItemList)

        bottomSheetBinding.itemsCountTextView.text =
            """${getString(R.string.total_items)} ($itemCount)"""
        bottomSheetBinding.topCharactersTextView.text = characterOccurrences.joinToString("\n") { "${it.key} = ${it.value}" }

        // Show  bottom sheet
        bottomSheetDialog.show()
    }

    private fun getTopCharacters(itemList: List<ItemsModel>): List<Map.Entry<Char, Int>> {
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

    private fun viewPagerControl() {

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                catId=(position+1).toString()

                 updatedItemList=filterItemsByCatId(catId,itemList)
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