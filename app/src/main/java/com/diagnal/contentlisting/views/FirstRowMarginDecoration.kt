package com.diagnal.contentlisting.views

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FirstRowMarginDecoration(
    private val topMargin: Float,
    private val otherMargins: Float
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        if (position < (parent.layoutManager as GridLayoutManager).spanCount) {
            outRect.top = topMargin.toInt()
        } else {
            outRect.top = otherMargins.toInt()
        }
    }
}
