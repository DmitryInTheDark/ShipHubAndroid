package com.app.base

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object FileUtils {
    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        result = cursor.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }

    fun getMimeType(context: Context, uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }

    fun getSafeFileName(fileName: String): String {
        val decodedName = Uri.decode(fileName)
        val maxLength = 100 // Safe length to avoid ENAMETOOLONG
        
        val extension = decodedName.substringAfterLast('.', "")
        val nameWithoutExtension = decodedName.substringBeforeLast('.')
        
        val truncatedName = if (nameWithoutExtension.length > maxLength) {
            nameWithoutExtension.take(maxLength)
        } else {
            nameWithoutExtension
        }
        
        return if (extension.isNotEmpty()) {
            "$truncatedName.${extension.take(10)}" // Also truncate extension just in case
        } else {
            truncatedName
        }
    }

    fun createDocumentPart(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
        val fileName = getFileName(context, uri) ?: "document"
        val safeFileName = getSafeFileName(fileName)
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val bytes = inputStream.readBytes()
        val requestBody = bytes.toRequestBody("application/octet-stream".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, safeFileName, requestBody)
    }

    fun saveFileToDownloads(context: Context, file: java.io.File, fileName: String): Uri? {
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, getMimeType(context, Uri.fromFile(file)))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS)
            }
        }

        val resolver = context.contentResolver
        val uri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            resolver.insert(android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            // Fallback for older versions if needed, or use Environment.getExternalStoragePublicDirectory
            // For simplicity, let's stick to MediaStore for Q+ and handle older if necessary
            // Insert into MediaStore.Files
            resolver.insert(android.provider.MediaStore.Files.getContentUri("external"), contentValues)
        }

        return uri?.also {
            resolver.openOutputStream(it)?.use { outputStream ->
                file.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }


}
