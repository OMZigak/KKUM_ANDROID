package com.teamkkumul.feature.utils.extension

import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.teamkkumul.core.ui.util.context.colorOf
import com.teamkkumul.core.ui.view.setInVisible
import com.teamkkumul.feature.utils.model.BtnState

fun setUpButton(
    state: BtnState,
    button: MaterialButton,
    circle: ImageView,
    progressBar: LinearProgressIndicator,
    progressBarEnd: LinearProgressIndicator? = null,
    helpText: TextView,
) {
    button.apply {
        setStrokeColorResource(state.strokeColor)
        setTextColor(context.colorOf(state.textColor))
        setBackgroundColor(context.colorOf(state.backGroundColor))
        isEnabled = state.isEnabled
    }
    circle.setImageResource(state.circleImage)
    progressBar.progress = state.progress
    progressBarEnd?.progress = state.progress
    helpText.setInVisible(state.isHelpTextVisible)
}
