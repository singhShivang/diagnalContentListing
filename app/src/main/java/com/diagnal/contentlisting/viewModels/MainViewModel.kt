package com.diagnal.contentlisting.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diagnal.contentlisting.model.PageData
import com.diagnal.contentlisting.repo.IPageRepository
import com.diagnal.contentlisting.views.ContentAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val loader: IPageRepository) : ViewModel() {
    private val _contentListData = MutableLiveData<PageData>()
    val contentListData: LiveData<PageData> = _contentListData
    val adapter = ContentAdapter(mutableListOf())
    private val loadedPageNumbers = mutableSetOf<Int>()
    var backPressedTime: Long = 0L
    var currentPageNumber = 0
    private var isLoading = false

    // Function to load the next page of content
    fun loadNextPage() {
        if (!isLoading) {
            val nextPageNumber = findNextPageNumberToLoad()
            isLoading = true
            fetchPageData(nextPageNumber)
        }
    }

    // Find the next page number to load
    private fun findNextPageNumberToLoad(): Int {
        currentPageNumber = loadedPageNumbers.maxOrNull() ?: 0
        return (++currentPageNumber)
    }

    // Fetch data for a specific page number
    private fun fetchPageData(pageNumber: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newDataChunk = loader.loadJson(pageNumber)
            newDataChunk?.let {
                isLoading = false
                loadedPageNumbers.add(it.pageNum.toInt())
                _contentListData.postValue(it)
            }
        }
    }
}

