package com.teamkkumul.feature.utils

import com.teamkkumul.feature.R

enum class DeleteDialogType(
    val question: String,
    val questionDescription: String,
    val imageResId: Int,
    val btnText: String = R.string.tv_btn_leave.toString(),
) {
    MEET_UP_LEAVE_DIALOG(
        question = R.string.tv_leave_question.toString(),
        questionDescription = R.string.tv_leave_question_description.toString(),
        imageResId = R.drawable.ic_dialog_leave_meet_up,
    ),
    MY_GROUP_LEAVE_DIALOG(
        question = R.string.tv_leave_my_group_question.toString(),
        questionDescription = R.string.tv_leave_my_group_question_description.toString(),
        imageResId = R.drawable.ic_dialog_leave_my_group,
    ),
    MEET_UP_DELETE_DIALOG(
        question = R.string.tv_delete_meet_up_question.toString(),
        questionDescription = R.string.tv_delete_meet_up_question_description.toString(),
        imageResId = R.drawable.ic_dialog_delete_meet_up,
        btnText = R.string.tv_btn_delete.toString(),
    ),
}
