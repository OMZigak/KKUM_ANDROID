package com.teamkkumul.feature.meetupcreate.friend

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MeetUpCreateFriendItemDecoration(private val context: Context) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spanCount = 3
        val column = position % spanCount

        when (column) {
            0 -> {
                outRect.left = 0
                outRect.right = 4
                outRect.top = 8
            }

            1 -> {
                outRect.left = 4
                outRect.right = 4
                outRect.top = 8
            }

            2 -> {
                outRect.left = 4
                outRect.right = 0
                outRect.top = 8
            }
        }
    }
}
