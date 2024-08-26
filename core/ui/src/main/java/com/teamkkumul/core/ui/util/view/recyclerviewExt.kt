package com.teamkkumul.core.ui.util.view

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.colorOf(@ColorRes resId: Int): Int =
    ContextCompat.getColor(itemView.context, resId)
