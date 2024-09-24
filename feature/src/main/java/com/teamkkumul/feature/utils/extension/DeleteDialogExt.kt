package com.teamkkumul.feature.utils.extension

import com.teamkkumul.feature.utils.type.DeleteDialogType

fun DeleteDialogType.isImageVisible(): Boolean =
    this != DeleteDialogType.Logout && this != DeleteDialogType.Withdrawal

fun DeleteDialogType.shouldChangeDescriptionColor(): Boolean =
    this == DeleteDialogType.PROMISE_DELETE_DIALOG || this == DeleteDialogType.Withdrawal
