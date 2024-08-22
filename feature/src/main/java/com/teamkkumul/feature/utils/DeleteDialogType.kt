package com.teamkkumul.feature.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.teamkkumul.feature.R

enum class DeleteDialogType(
    @StringRes val question: Int,
    @StringRes val questionDescription: Int,
    @DrawableRes val imageResId: Int,
    @StringRes val btnText: Int = R.string.tv_btn_leave,
) {
    MY_GROUP_LEAVE_DIALOG(
        question = R.string.tv_leave_my_group_question,
        questionDescription = R.string.tv_leave_my_group_question_description,
        imageResId = R.drawable.ic_dialog_leave_my_group,
    ),
    PROMISE_LEAVE_DIALOG(
        question = R.string.tv_leave_question,
        questionDescription = R.string.tv_leave_question_description,
        imageResId = R.drawable.ic_dialog_leave_meet_up,
    ),
    PROMISE_DELETE_DIALOG(
        question = R.string.tv_delete_meet_up_question,
        questionDescription = R.string.tv_delete_meet_up_question_description,
        imageResId = R.drawable.ic_dialog_delete_meet_up,
        btnText = R.string.tv_btn_delete,
    ),
}
