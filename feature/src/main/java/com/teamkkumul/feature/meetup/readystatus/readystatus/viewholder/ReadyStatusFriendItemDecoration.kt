package com.teamkkumul.feature.meetup.readystatus.readystatus.viewholder

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.core.ui.util.context.pxToDp

class ReadyStatusFriendItemDecoration(val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        outRect.bottom = when (position) {
            itemCount - 1 -> context.pxToDp(50)
            else -> context.pxToDp(8)
        }
    }
}
