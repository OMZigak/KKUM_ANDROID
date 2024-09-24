package com.teamkkumul.feature.utils.extension

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.teamkkumul.core.ui.util.context.colorOf
import com.teamkkumul.feature.R
import com.teamkkumul.feature.utils.type.LevelColorType

fun Context.updateLevelText(level: Int, colorType: LevelColorType): SpannableString {
    val levelText = getString(
        when (level) {
            1 -> R.string.home_lv1
            2 -> R.string.home_lv2
            3 -> R.string.home_lv3
            4 -> R.string.home_lv4
            else -> R.string.home_lv1
        },
    )

    return createSpannableLevelText(level, levelText, colorType)
}

fun Context.createSpannableLevelText(
    level: Int,
    text: String,
    colorType: LevelColorType,
): SpannableString {
    val fullText = getString(R.string.home_level_text, level, text)
    val spannable = SpannableString(fullText)

    // 레벨 텍스트에 전달받은 색상으로 스타일 적용
    spannable.setSpan(
        ForegroundColorSpan(colorOf(colorType.colorResId)),
        0,
        4,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
    )

    return spannable
}

fun getLevelImageResId(level: Int): Int = when (level) {
    1 -> R.drawable.ic_home_lv_1
    2 -> R.drawable.ic_home_lv_2
    3 -> R.drawable.ic_home_lv_3
    4 -> R.drawable.ic_home_lv_4
    else -> R.drawable.ic_home_lv_1
}

fun getLevelFenceText(level: Int): Int = when (level) {
    1 -> R.string.home_fence_lv1
    2 -> R.string.home_fence_lv2
    3 -> R.string.home_fence_lv3
    4 -> R.string.home_fence_lv4
    else -> R.string.home_fence_lv1
}
