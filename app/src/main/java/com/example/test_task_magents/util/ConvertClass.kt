package com.example.test_task_magents.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException

object ConvertClass {
    fun convertStringToBitmap(encodedString: String?): Bitmap? {
        if (encodedString == null) return null
        val decodeString = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
    }

    fun convertBitmapToString(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        try {
            stream.flush()
            stream.close()
        } catch (ignored: IOException) {
        }
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}