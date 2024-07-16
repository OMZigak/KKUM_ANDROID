package com.teamkkumul.core.data.repositoryimpl.utils

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ContentUriRequestBody(
    private val contentResolver: ContentResolver,
    private val uri: Uri,
) : MultipartBody.Part() {
}