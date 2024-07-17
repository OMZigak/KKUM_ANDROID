package com.teamkkumul.core.data.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.ByteArrayOutputStream

class ContentUriRequestBody(
    private val contentResolver: ContentResolver,
    private val uri: Uri,
) : RequestBody() {

    private var size = -1L
    private var compressedImage: ByteArray? = null

    init {
        contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME),
            null,
            null,
            null,
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
            }
        }

        compressBitmap()
    }

    private fun compressBitmap() {
        var originalBitmap: Bitmap? = null
        val exif: ExifInterface

        contentResolver.openInputStream(uri).use { inputStream ->
            if (inputStream == null) return
            exif = ExifInterface(inputStream)
        }

        // 이미지 크기를 계산하여 imageSizeMb 선언
        val imageSizeMb = size / (1024.0 * 1024.0)

        contentResolver.openInputStream(uri).use { inputStream ->
            if (inputStream == null) return
            // 이미지 크기를 계산하여 3MB를 초과하는 경우에만 inSampleSize 설정
            val option = BitmapFactory.Options().apply {
                if (imageSizeMb >= 3) {
                    inSampleSize = calculateInSampleSize(this, MAX_WIDTH, MAX_HEIGHT)
                }
            }
            originalBitmap = BitmapFactory.decodeStream(inputStream, null, option)
        }

        originalBitmap?.let { bitmap ->
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL,
            )
            val rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                else -> bitmap
            }

            val outputStream = ByteArrayOutputStream()
            val compressRate = if (imageSizeMb >= 3) {
                (300 / imageSizeMb).toInt()
            } else {
                75 // 기본 압축률을 더 낮게 설정
            }

            outputStream.use { stream ->
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressRate, stream)
            }
            compressedImage = outputStream.toByteArray()
            size = compressedImage?.size?.toLong() ?: -1L
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(degrees)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int,
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    // private fun getFileName() = fileName

    override fun contentLength(): Long = size

    override fun contentType(): MediaType? =
        uri.let { contentResolver.getType(it)?.toMediaTypeOrNull() }

    override fun writeTo(sink: BufferedSink) {
        compressedImage?.let(sink::write)
    }

    fun toMultiPartData(name: String) = MultipartBody.Part.createFormData(name, null, this)

    companion object {
        const val MAX_WIDTH = 1024
        const val MAX_HEIGHT = 1024
    }
}
