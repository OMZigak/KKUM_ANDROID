package com.teamkkumul.feature.utils.model

import com.teamkkumul.feature.R

sealed class BtnState(
    open val strokeColor: Int,
    open val textColor: Int,
    open val backGroundColor: Int,
    open val circleImage: Int,
    open val isEnabled: Boolean,
    open val progress: Int,
    open val isHelpTextVisible: Boolean,
    open val btnText: String,
) {
    data class Default(
        override val strokeColor: Int = R.color.main_color,
        override val textColor: Int = R.color.main_color,
        override val backGroundColor: Int = R.color.white0,
        override val circleImage: Int = R.drawable.shape_circle_gray02_16,
        override val isEnabled: Boolean,
        override val progress: Int = 0,
        override val isHelpTextVisible: Boolean = true,
        override val btnText: String,
    ) : BtnState(
            strokeColor,
            textColor,
            backGroundColor,
            circleImage,
            isEnabled,
            progress,
            isHelpTextVisible,
            btnText,
        )

    data class DefaultGray(
        override val strokeColor: Int = R.color.gray2,
        override val textColor: Int = R.color.gray2,
        override val backGroundColor: Int = R.color.white0,
        override val circleImage: Int = R.drawable.shape_circle_gray02_16,
        override val isEnabled: Boolean = false,
        override val progress: Int = 0,
        override val isHelpTextVisible: Boolean = false,
        override val btnText: String,
    ) : BtnState(
            strokeColor,
            textColor,
            backGroundColor,
            circleImage,
            isEnabled,
            progress,
            isHelpTextVisible,
            btnText,
        )

    data class InProgress(
        override val strokeColor: Int = R.color.main_color,
        override val textColor: Int = R.color.main_color,
        override val backGroundColor: Int = R.color.green2,
        override val circleImage: Int = R.drawable.shape_circle_green2_16,
        override val isEnabled: Boolean,
        override val progress: Int = 100,
        override val isHelpTextVisible: Boolean = false,
        override val btnText: String,
    ) : BtnState(
            strokeColor,
            textColor,
            backGroundColor,
            circleImage,
            isEnabled,
            progress,
            isHelpTextVisible,
            btnText,
        )

    data class Complete(
        override val strokeColor: Int = R.color.main_color,
        override val textColor: Int = R.color.white0,
        override val backGroundColor: Int = R.color.main_color,
        override val circleImage: Int = R.drawable.ic_check_green_24,
        override val isEnabled: Boolean = false,
        override val progress: Int = 100,
        override val isHelpTextVisible: Boolean = false,
        override val btnText: String,
    ) : BtnState(
            strokeColor,
            textColor,
            backGroundColor,
            circleImage,
            isEnabled,
            progress,
            isHelpTextVisible,
            btnText,
        )
}
