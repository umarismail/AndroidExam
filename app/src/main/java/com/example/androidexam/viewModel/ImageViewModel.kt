package com.example.androidexam.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidexam.model.ImageModel
import com.example.androidexam.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.lifecycle.distinctUntilChanged
@HiltViewModel
class ImageViewModel  @Inject constructor(private val imageRepository: ImageRepository) : ViewModel() {



    private val _imageItemData = MutableLiveData<List<ImageModel>>()
    val imageItemData: LiveData<List<ImageModel>> = _imageItemData.distinctUntilChanged()

    fun refreshImageData() {
        viewModelScope.launch {
            val menuItems = withContext(Dispatchers.IO) {
                imageRepository.getImageUrls()
            }
            _imageItemData.postValue(menuItems)
        }
    }

}