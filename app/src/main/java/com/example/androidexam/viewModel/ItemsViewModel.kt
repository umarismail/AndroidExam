package com.example.androidexam.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.example.androidexam.model.ImageModel
import com.example.androidexam.model.ItemsModel
import com.example.androidexam.repository.ImageRepository
import com.example.androidexam.repository.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class ItemsViewModel @Inject constructor(private val itemsRepository: ItemsRepository)  : ViewModel() {

    private val _itemData = MutableLiveData<List<ItemsModel>>()
    val itemData: LiveData<List<ItemsModel>> = _itemData.distinctUntilChanged()

    fun refreshItemData() {
        viewModelScope.launch {
            val menuItems = withContext(Dispatchers.IO) {
                itemsRepository.getItems()
            }
            _itemData.postValue(menuItems)
        }
    }
}