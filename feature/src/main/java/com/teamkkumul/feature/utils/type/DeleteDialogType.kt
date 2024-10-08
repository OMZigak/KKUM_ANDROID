package com.teamkkumul.feature.utils.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.teamkkumul.feature.R

enum class DeleteDialogType(
    @StringRes val question: Int,
    @StringRes val questionDescription: Int,
    @DrawableRes val imageResId: Int = R.drawable.ic_dialog_leave_my_group,
    @StringRes val btnLeaveText: Int = R.string.tv_btn_leave,
) {
    MY_GROUP_LEAVE_DIALOG(
        question = R.string.tv_leave_my_group_question,
        questionDescription = R.string.tv_leave_my_group_question_description,
        imageResId = R.drawable.ic_dialog_leave_my_group,
    ),
    PROMISE_LEAVE_DIALOG(
        question = R.string.tv_leave_question,
        questionDescription = R.string.tv_leave_question_description,
        imageResId = R.drawable.ic_meet_up_detail_receipt_gray,
    ),
    PROMISE_DELETE_DIALOG(
        question = R.string.tv_delete_meet_up_question,
        questionDescription = R.string.tv_delete_meet_up_question_description,
        imageResId = R.drawable.ic_dialog_delete_meet_up,
        btnLeaveText = R.string.tv_btn_delete,
    ),
    Logout(
        question = R.string.dialog_logout_title,
        questionDescription = R.string.dialog_logout_description,
        btnLeaveText = R.string.dialog_logout_btn_right,
    ),
    Withdrawal(
        question = R.string.dialog_withdrawal_title,
        questionDescription = R.string.dialog_withdrawal_description,
        btnLeaveText = R.string.dialog_withdrawal_btn_right,
    ),
}
