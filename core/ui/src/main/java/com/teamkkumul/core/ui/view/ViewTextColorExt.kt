package com.teamkkumul.core.ui.view

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat

fun TextView.setTextColorRes(colorResId: Int) {
    try {
        setTextColor(ContextCompat.getColor(context, colorResId))
    } catch (e: Resources.NotFoundException) {
        setTextColor(colorResId)
    }
}

fun Group.setTextColor(colorResId: Int) {
    referencedIds.forEach { id ->
        (rootView.findViewById<View>(id) as? TextView)?.setTextColorRes(colorResId)
    }
}
