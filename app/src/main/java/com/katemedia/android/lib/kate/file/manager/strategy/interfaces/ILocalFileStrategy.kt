package com.katemedia.android.lib.kate.file.manager.strategy.interfaces

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy
import java.io.File

interface ILocalFileStrategy {

    fun isExist(localPath: String): Boolean
    fun isExist(localPath: String, creationStrategy: FileCreationStrategy): Boolean

    fun getFile(localPath: String): File
    fun getFile(localPath: String, creationStrategy: FileCreationStrategy): File
    fun getBitmap(localPath: String): Bitmap?
    fun getBitmap(localPath: String, creationStrategy: FileCreationStrategy): Bitmap?

    fun moveTo(oldPath: String, newPath: String): String?
    fun moveTo(oldPath: String, newPath: String, creationStrategy: FileCreationStrategy): String?

    fun saveTo(inPath: String, outPath: String): String?
    fun saveTo(inPath: String, outPath: String, creationStrategy: FileCreationStrategy): String?
    fun saveTo(bitmap: Bitmap, outPath: String, creationStrategy: FileCreationStrategy): String?
    fun saveTo(bitmap: Bitmap, outPath: String): String?

    fun writeTo(
        inPath: String,
        outPath: String,
        creationStrategy: FileCreationStrategy,
        overwrite: Boolean
    ): Boolean

    fun writeTo(inPath: String, outPath: String, overwrite: Boolean): Boolean

    fun deleteFile(localPath: String)
    fun deleteFile(localPath: String, creationStrategy: FileCreationStrategy)

    fun createFile(localPath: String, data: ByteArray?, temp: Boolean): File?
    fun createFile(
        localPath: String,
        data: ByteArray?,
        creationStrategy: FileCreationStrategy,
        temp: Boolean
    ): File?

    fun createPdfFile(localPath: String, document: PdfDocument?): File?
    fun createPdfFile(
        localPath: String,
        document: PdfDocument?,
        creationStrategy: FileCreationStrategy
    ): File?

    fun getFileMD5(localPath: String): String?
    fun getFileMD5(localPath: String, creationStrategy: FileCreationStrategy): String?

    fun getFileBase64(localPath: String): String?
    fun getFileBase64(localPath: String, creationStrategy: FileCreationStrategy): String?

    fun getFileData(localPath: String): ByteArray?
    fun getFileData(localPath: String, creationStrategy: FileCreationStrategy): ByteArray?

}