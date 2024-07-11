package com.teamkkumul.feature.utils.itemdecorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.core.ui.util.context.pxToDp

class MeetUpFriendItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
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
                outRect.left = context.pxToDp(14)
                outRect.right = context.pxToDp(6)
            }

            itemCount - 1 -> {
                outRect.left = context.pxToDp(6)
                outRect.right = context.pxToDp(14)
            }

            else -> {
                outRect.left = context.pxToDp(6)
                outRect.right = context.pxToDp(12)
            }
        }
    }
}
