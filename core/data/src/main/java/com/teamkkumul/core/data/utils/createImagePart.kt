package com.teamkkumul.core.data.utils

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MultipartBody

fun createImagePart(contentResolver: ContentResolver, uriString: String?): MultipartBody.Part? {
    return when (uriString) {
        null -> null
        else -> {
            val uri = Uri.parse(uriString)
            val imageRequestBody = ContentUriRequestBody(contentResolver, uri)

            imageRequestBody.toMultiPartData("image")
        }
    }
}
