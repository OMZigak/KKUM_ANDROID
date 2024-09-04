package com.teamkkumul.feature.utils.type

import androidx.annotation.StringRes
import com.teamkkumul.feature.R

enum class ReadyBtnTextType(@StringRes val label: Int) {
    READY_DEFAULT(R.string.home_btn_ready_default),
    READY_START(R.string.home_btn_ready_start),
    READY_COMPLETE(R.string.home_btn_ready_completed),
    MOVING_DEFAULT(R.string.home_btn_moving_default),
    MOVING_START(R.string.home_btn_moving_start),
    MOVING_COMPLETE(R.string.home_btn_moving_completed),
    COMPLETED(R.string.home_btn_completed),
}
