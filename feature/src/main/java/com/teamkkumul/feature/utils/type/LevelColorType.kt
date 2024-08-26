package com.teamkkumul.feature.utils.type

import androidx.annotation.ColorRes
import com.teamkkumul.feature.R

enum class LevelColorType(
    @ColorRes val colorResId: Int,
) {
    HOME(colorResId = R.color.main_color),
    MY_PAGE(colorResId = R.color.light_green),
}
