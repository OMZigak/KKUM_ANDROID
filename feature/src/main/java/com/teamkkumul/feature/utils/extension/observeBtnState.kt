package com.teamkkumul.feature.utils.extension

import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.teamkkumul.core.ui.util.context.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.setVisible
import com.teamkkumul.feature.utils.model.BtnState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun Fragment.observeBtnState(
    stateFlow: StateFlow<BtnState>,
    onStateChanged: (BtnState) -> Unit,
) {
    stateFlow.flowWithLifecycle(this.viewLifeCycle).onEach { state ->
        onStateChanged(state)
    }.launchIn(this.viewLifeCycleScope)
}

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
    helpText.setVisible(state.isHelpTextVisible)
}
