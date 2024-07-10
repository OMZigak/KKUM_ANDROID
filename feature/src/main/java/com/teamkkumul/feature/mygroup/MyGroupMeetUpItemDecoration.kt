package com.teamkkumul.feature.mygroup

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.core.ui.util.context.pxToDp

class MyGroupMeetUpItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        when (position) {
            0 -> {
                outRect.left = context.pxToDp(16)
                outRect.right = context.pxToDp(12)
            }

            itemCount - 1 -> {
                outRect.left = context.pxToDp(12)
                outRect.right = context.pxToDp(12)
            }

            else -> {
                outRect.left = context.pxToDp(12)
                outRect.right = context.pxToDp(12)
            }
        }
    }
}
