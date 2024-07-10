package com.teamkkumul.feature.utils

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar

class AnimateProgressBarCommon(
    private var progressBar: ProgressBar,
    private var from: Float,
    private var to: Float,
) : Animation() {
    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        super.applyTransformation(interpolatedTime, t)
        val value = from + (to - from) * interpolatedTime
        progressBar.progress = value.toInt()
    }
}
