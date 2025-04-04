package com.katemedia.android.lib.kate.file.manager.strategy

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import com.katemedia.android.lib.kate.file.manager.strategy.base.FileStrategy
import com.katemedia.android.lib.kate.file.manager.strategy.interfaces.ILocalFileStrategy
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy
import com.katemedia.android.lib.kate.file.utils.logging.LogUtils
import java.io.File
import java.security.MessageDigest


open class LocalFileStrategy(appContext: Context) : FileStrategy(appContext), ILocalFileStrategy {

    override fun isExist(localPath: String): Boolean {
        return getFile(localPath).exists()
    }

    override fun isExist(localPath: String, creationStrategy: FileCreationStrategy): Boolean {
        return getFile(localPath, creationStrategy).exists()
    }

    override fun getFile(localPath: String): File {
        return File(localPath)
    }

    override fun getFile(localPath: String, creationStrategy: FileCreationStrategy): File {
        return File(getRootDir(creationStrategy), localPath)
    }

    override fun getBitmap(localPath: String): Bitmap? {
        val options = BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.ARGB_8888 }
        val bitmap = BitmapFactory.decodeFile(localPath, options)

        return if (options.outWidth != -1 && options.outHeight != -1) bitmap else run {
            LogUtils.logGetBitmap(localPath); null
        }
    }

    override fun getBitmap(localPath: String, creationStrategy: FileCreationStrategy): Bitmap? {
        val options = BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.ARGB_8888 }
        val pathName = getFile(localPath, creationStrategy).absolutePath
        val bitmap = BitmapFactory.decodeFile(pathName, options)

        return if (options.outWidth != -1 && options.outHeight != -1) bitmap else run {
            LogUtils.logGetBitmap(pathName); null
        }
    }

    override fun moveTo(oldPath: String, newPath: String): String? {
        if (oldPath == newPath) return oldPath

        val file = getFile(oldPath).copyTo(getFile(newPath), true)

        return file.also { deleteFile(getFile(oldPath)) }.absolutePath
    }

    override fun moveTo(
        oldPath: String,
        newPath: String,
        creationStrategy: FileCreationStrategy
    ): String? {
        if (oldPath == getFile(newPath, creationStrategy).absolutePath) return oldPath

        val file = getFile(oldPath).copyTo(getFile(newPath, creationStrategy), true)

        return file.also { deleteFile(getFile(oldPath)) }.absolutePath
    }

    override fun saveTo(inPath: String, outPath: String): String? {
        if (inPath == outPath) return inPath

        return getFile(inPath).copyTo(getFile(outPath), true).absolutePath
    }

    override fun saveTo(
        inPath: String,
        outPath: String,
        creationStrategy: FileCreationStrategy
    ): String? {
        if (inPath == getFile(outPath, creationStrategy).absolutePath) return inPath

        return getFile(inPath).copyTo(getFile(outPath, creationStrategy), true).absolutePath
    }

    override fun saveTo(
        bitmap: Bitmap,
        outPath: String,
        creationStrategy: FileCreationStrategy
    ): String? {
        val file = createFile(outPath, creationStrategy) ?: return null

        return write(file, bitmap).absolutePath
    }

    override fun saveTo(
        bitmap: Bitmap,
        outPath: String
    ): String? {
        val file = createFile(outPath) ?: return null

        return write(file, bitmap).absolutePath
    }

    override fun writeTo(inPath: String, outPath: String, overwrite: Boolean): Boolean {
        if (inPath == outPath) return false

        return write(getFile(inPath), getFile(outPath), overwrite)
    }

    override fun writeTo(
        inPath: String,
        outPath: String,
        creationStrategy: FileCreationStrategy,
        overwrite: Boolean
    ): Boolean {
        if (inPath == getFile(outPath, creationStrategy).absolutePath) return false

        return write(getFile(inPath), getFile(outPath, creationStrategy), overwrite)
    }

    override fun deleteFile(localPath: String) {
        deleteFile(getFile(localPath))
    }

    override fun deleteFile(localPath: String, creationStrategy: FileCreationStrategy) {
        deleteFile(getFile(localPath, creationStrategy))
    }

    override fun createFile(localPath: String, data: ByteArray?, temp: Boolean): File? {
        val file = (if (temp) createTempFile(localPath) else createFile(localPath)) ?: return null

        return write(file, data)
    }

    override fun createFile(
        localPath: String,
        data: ByteArray?,
        creationStrategy: FileCreationStrategy,
        temp: Boolean
    ): File? {
        val file = createFile(localPath, creationStrategy, temp) ?: return null

        return write(file, data)
    }

    override fun createPdfFile(localPath: String, document: PdfDocument?): File? {
        deleteFile(localPath)

        val file = createFile(localPath) ?: return null

        return write(file, document)
    }

    override fun createPdfFile(
        localPath: String,
        document: PdfDocument?,
        creationStrategy: FileCreationStrategy
    ): File? {
        deleteFile(localPath, creationStrategy)

        val file = createFile(localPath, creationStrategy, false) ?: return null

        return write(file, document)
    }

    override fun getFileMD5(localPath: String): String? {
        val md5Digest = MessageDigest.getInstance("MD5")

        return getFileChecksum(md5Digest, getFile(localPath))
    }

    override fun getFileMD5(localPath: String, creationStrategy: FileCreationStrategy): String? {
        val md5Digest = MessageDigest.getInstance("MD5")

        return getFileChecksum(md5Digest, getFile(localPath, creationStrategy))
    }

    override fun getFileBase64(localPath: String): String? {
        return getFileBase64(getFile(localPath))
    }

    override fun getFileBase64(localPath: String, creationStrategy: FileCreationStrategy): String? {
        return getFileBase64(getFile(localPath, creationStrategy))
    }

    override fun getFileData(localPath: String): ByteArray? {
        return read(getFile(localPath))
    }

    override fun getFileData(
        localPath: String,
        creationStrategy: FileCreationStrategy
    ): ByteArray? {
        return read(getFile(localPath, creationStrategy))
    }

}
