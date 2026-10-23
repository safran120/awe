package com.safranpulse.pulsechat.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageCompressor {
    fun compress(context: Context, uri: Uri, quality: Int = 80): Uri? {
        return try {
            val input = context.contentResolver.openInputStream(uri) ?: return null
            val bitmap = BitmapFactory.decodeStream(input)
            input.close()
            if (bitmap == null) return null
            val file = File(context.cacheDir, "img_${System.currentTimeMillis()}.webp")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.WEBP, quality, out)
            }
            bitmap.recycle()
            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }
}
