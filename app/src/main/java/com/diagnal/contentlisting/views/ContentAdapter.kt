package com.diagnal.contentlisting.views

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diagnal.contentlisting.R
import com.diagnal.contentlisting.databinding.ItemHorizontalBinding
import com.diagnal.contentlisting.model.ContentData

class ContentAdapter(private val contentList: MutableList<ContentData>) :
    RecyclerView.Adapter<ContentAdapter.MovieViewHolder>() {
    private val filteredItems = mutableListOf<ContentData>()

    // Create view holder and inflate layout
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemHorizontalBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    // Add new items to the adapter and update the RecyclerView
    fun addItems(newChunk: List<ContentData>) {
        val oldSize = contentList.size
        newChunk.apply {
            contentList.addAll(this)
            filteredItems.clear()
            filteredItems.addAll(contentList)
        }
        notifyItemRangeInserted(oldSize, newChunk.size)
    }

    // Get the total number of items in the adapter
    override fun getItemCount(): Int {
        return filteredItems.size
    }

    // Bind data to view holder
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val contentData = filteredItems[position]
        // Set the movie name text to highlighted text or the original name
        holder.binding.movieName.text = contentData.highlightedText ?: contentData.name
        val id = contentData.posterImage.split(".")[0]
        // Get the resource ID for the poster image
        val resourceId = holder.binding.poster.context.resources.getIdentifier(
            id,
            "drawable",
            holder.binding.poster.context.packageName
        )
        // Set the poster image if a valid resource ID is found, otherwise use a placeholder
        if (resourceId != 0) holder.binding.poster.setImageResource(resourceId)
        else holder.binding.poster.setImageResource(R.drawable.placeholder_for_missing_posters)
    }

    // Filter and highlight items based on a query
    fun filter(query: String) {
        filteredItems.clear()
        if (query.length >= 3) {
            filteredItems.addAll(contentList.filter { contentItem ->
                val name = contentItem.name
                val lowerCaseName = name.lowercase()
                val lowerCaseQuery = query.lowercase()
                val startIndex = lowerCaseName.indexOf(lowerCaseQuery)
                if (startIndex != -1) {
                    val endIndex = startIndex + query.length
                    // Set highlighted text and apply the yellow foreground color
                    contentItem.highlightedText = highlightText(name, startIndex, endIndex)
                    true
                } else {
                    false
                }
            })
        } else {
            // If query length is less than 3, clear the highlightedText property
            contentList.forEach { it.highlightedText = null }
            filteredItems.addAll(contentList)
        }
        notifyDataSetChanged()
    }

    // Highlight text within a given range and return a SpannableString
    private fun highlightText(text: String, startIndex: Int, endIndex: Int): SpannableString {
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            ForegroundColorSpan(
                Color.YELLOW
            ),
            startIndex,
            endIndex,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    // Define a view holder for the RecyclerView
    class MovieViewHolder(val binding: ItemHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root)
}

