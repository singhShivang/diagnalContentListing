package com.diagnal.contentlisting.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diagnal.contentlisting.viewModels.MainViewModel
import com.diagnal.contentlisting.R
import com.diagnal.contentlisting.databinding.ActivityMainBinding
import com.diagnal.contentlisting.di.ContextProvider
import com.diagnal.contentlisting.gone
import com.diagnal.contentlisting.hideKeyboard
import com.diagnal.contentlisting.onTextChanged
import com.diagnal.contentlisting.visible
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextProvider.context = WeakReference(applicationContext)

        // Initialize the DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Load the initial page data
        viewModel.loadNextPage()

        // Configure the RecyclerView
        binding.rvPage.apply {
            adapter = viewModel.adapter

            // Define margins and decorations for RecyclerView items
            val margin = resources.getDimension(R.dimen.margin_default)
            val firstRowMargin = resources.getDimension(R.dimen.margin_first_row)
            val decoration = FirstRowMarginDecoration(firstRowMargin, margin)
            addItemDecoration(decoration)

            // Add a scroll listener to load more data when reaching the end
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    if (lastVisibleItemPosition >= totalItemCount - 1) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }

        // Handle back button click
        binding.back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })

        // Observe changes in content data
        viewModel.contentListData.observe(this) { pageData ->
            binding.textView.text = pageData.title
            if (viewModel.currentPageNumber == pageData.pageNum.toInt()) {
                viewModel.adapter.addItems(pageData.contentList)
            }
        }

        // Handle search functionality
        binding.search.setOnClickListener {
            if (binding.nonSearchGroup.visibility == View.VISIBLE) {
                showSearchView()
            } else {
                hideSearchView()
            }
        }

        (binding.etSearch as EditText).onTextChanged {
            viewModel.adapter.filter(it)
        }
    }

    // Show the search view
    private fun showSearchView() {
        binding.nonSearchGroup.gone()
        binding.etSearch.visible()
        binding.search.setImageResource(R.drawable.search_cancel)
        binding.etSearch.requestFocus()
    }

    // Hide the search view
    private fun hideSearchView() {
        binding.nonSearchGroup.visible()
        binding.etSearch.gone()
        (binding.etSearch as EditText).text.clear()
        binding.etSearch.clearFocus()
        binding.search.setImageResource(R.drawable.search)
        binding.etSearch.hideKeyboard(this@MainActivity)
    }

    // Handle back button press to exit the app
    private fun handleBackPressed() {
        if (viewModel.backPressedTime + 2000 > System.currentTimeMillis()) {
            finish()
        } else {
            Toast.makeText(
                this@MainActivity,
                "Press back again to exit",
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.backPressedTime = System.currentTimeMillis()
    }
}
