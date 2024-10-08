package com.teamkkumul.core.ui.view

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged

fun EditText.doAfterTextChangedWithCursor(onTextChanged: (String) -> Unit) {
    this.doAfterTextChanged { text ->
        if (!text.isNullOrEmpty()) {
            this.setSelection(text.length)
            onTextChanged(text.toString())
        }
    }
}
