package com.teamkkumul.feature.utils

import com.teamkkumul.feature.R

enum class DeleteDialogType(
    val question: String,
    val questionDescription: String,
    val imageResId: Int,
    val btnText: String = "나가기",
) {
    MEET_UP_LEAVE_DIALOG(
        question = "약속에서 나가시겠어요?",
        questionDescription = "약속에서 나가도\n참여 인원 추가를 통해 다시 참여 가능해요.",
        imageResId = R.drawable.ic_dialog_leave_meet_up,
    ),
    MY_GROUP_LEAVE_DIALOG(
        question = "모임에서 나가시겠어요?",
        questionDescription = "모임에서 나가도\n초대 코드를 통해 다시 들어올 수 있어요.",
        imageResId = R.drawable.ic_dialog_leave_my_group,
    ),
    MEET_UP_DELETE_DIALOG(
        question = "정말 약속을 삭제하시겠어요?",
        questionDescription = "약속을 삭제하면\n모든 참여 인원의 약속 목록에서 사라져요.",
        imageResId = R.drawable.ic_dialog_delete_meet_up,
        btnText = "삭제하기",
    ),
}
