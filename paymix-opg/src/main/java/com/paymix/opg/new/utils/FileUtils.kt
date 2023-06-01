package com.walletmix.paymixbusiness.utils


import android.annotation.TargetApi
import android.content.Context
import android.content.CursorLoader
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.DecimalFormat


class FileUtils {

    companion object {
        val sharedInstance = FileUtils()
    }

    private val TAG = "FileUtils"
    private val DEBUG = false // Set to true to enable logging

    val MIME_TYPE_AUDIO = "audio/*"
    val MIME_TYPE_TEXT = "text/*"
    val MIME_TYPE_IMAGE = "image/*"
    val MIME_TYPE_VIDEO = "video/*"
    val MIME_TYPE_APP = "application/*"

    private val HIDDEN_PREFIX = "."

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if file was null.
     */
    private fun getExtension(uri: String?): String? {
        uri?.let {
            val dot = uri.lastIndexOf(".")
            return if (dot >= 0) {
                uri.substring(dot)
            } else null
        }
        return null
    }

    /**
     * Convert File into Uri.
     *
     * @param file
     * @return file
     */

    private fun getUri(file: File?): Uri? {
        return if (file != null) {
            Uri.fromFile(file)
        } else null
    }

    /**
     * Returns the path only (without file name).
     *
     * @param file
     * @return
     */

    fun getPathWithoutFilename(file: File?): File? {
        file?.let {
            return if (file.isDirectory) {
                file
            } else {
                val filename = file.name
                val filepath = file.absolutePath
                // Construct path without file name.
                var pathWithoutName = filepath.substring(
                    0,
                    filepath.length - filename.length
                )
                if (pathWithoutName.endsWith("/")) {
                    pathWithoutName = pathWithoutName.substring(0, pathWithoutName.length - 1)
                }
                File(pathWithoutName)
            }
        }
        return null
    }


    /**
     * @return The MIME type for the given file.
     */

    private fun getMimeType(file: File): String? {
        val extension = getExtension(file.name)
        extension?.let {
            return if (extension.isNotEmpty())
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1))
            else "application/octet-stream"

        }
        return null
    }

    /**
     * @return The MIME type for the give Uri.
     */

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun getMimeType(context: Context, uri: Uri): String? {
        val file = File(getRealPathFromUri(context, uri))
        return getMimeType(file)
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author Ahsen Saeed
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author Ahsen Saeed
     */
    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        if(uri == null) return null
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor!!.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor)

                val column_index = cursor!!.getColumnIndexOrThrow(column)
                return cursor!!.getString(column_index)
            }
        } finally {
            if (cursor != null)
                cursor!!.close()
        }
        return null
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author Ahsen Saeed
     * @see .getPath
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun getFile(context: Context, uri: Uri?): File? {
        if (uri != null) {
            val path = getRealPathFromUri(context, uri)
            if (isLocal(path)) {
                return File(path)
            }
        }
        return null
    }


    fun getRealPathFromUri(context: Context, fileUri: Uri): String? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, fileUri, filePathColumn, null, null, null)
        val cursor = loader.loadInBackground()
        return if (cursor == null) {
            fileUri.path
        } else {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            picturePath
        }
    }


    /**
     * @return Whether the URI is a local one.
     */

    private fun isLocal(url: String?): Boolean {
        return url != null && !url.startsWith("http://") && !url.startsWith("https://")
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author Ahsen Saeed
     */

    private fun isMediaUri(uri: Uri): Boolean {
        return "media".equals(uri.authority!!, ignoreCase = true)
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author Ahsen Saeed
     */

    fun getReadableFileSize(size: Int): String {
        val BYTES_IN_KILOBYTES = 1024
        val dec = DecimalFormat("###.#")
        val KILOBYTES = " KB"
        val MEGABYTES = " MB"
        val GIGABYTES = " GB"
        var fileSize = 0f
        var suffix = KILOBYTES

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = (size / BYTES_IN_KILOBYTES).toFloat()
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize /= BYTES_IN_KILOBYTES
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize /= BYTES_IN_KILOBYTES
                    suffix = GIGABYTES
                } else {
                    suffix = MEGABYTES
                }
            }
        }
        return (dec.format(fileSize) + suffix)
    }

    fun createRequestBodyFromString(text: String): RequestBody {
        return RequestBody.create(MultipartBody.FORM, text)
    }

    fun createMultiPartBodyFromFileUri(
        context: Context,
        fileName: String,
        fileUri: Uri?
    ): MultipartBody.Part? {
        fileUri?.let {
            val file = File(getRealPathFromUri(context, fileUri))
            val requestFile = RequestBody.create(
                context.contentResolver.getType(fileUri)!!.toMediaTypeOrNull(), file)
            return MultipartBody.Part.createFormData(fileName, file.name, requestFile)
        }
        return null
    }

    fun createMultipartBodyFromFile(fileName: String, file: File?): MultipartBody.Part? {
        return if (file != null) {
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData(fileName, file.name, requestFile)
        } else {
            null
        }
    }
}