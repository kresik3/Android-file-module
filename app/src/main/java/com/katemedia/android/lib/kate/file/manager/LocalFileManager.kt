package com.katemedia.android.lib.kate.file.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import com.katemedia.android.lib.kate.file.manager.interfaces.ILocalFileManager
import com.katemedia.android.lib.kate.file.manager.strategy.LocalFileStrategy
import com.katemedia.android.lib.kate.file.manager.strategy.interfaces.ILocalFileStrategy
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy
import java.io.File
import java.util.*


internal class LocalFileManager(context: Context) : ILocalFileManager {

    private val strategy: ILocalFileStrategy = LocalFileStrategy(context)

    override fun isExist(localPath: String): Boolean {
        return if (localPath.isNotEmpty()) strategy.isExist(localPath) else false
    }

    override fun isExist(localPath: String, creationStrategy: FileCreationStrategy): Boolean {
        return if (localPath.isNotEmpty()) strategy.isExist(localPath, creationStrategy) else false
    }

    override fun getFile(localPath: String): File? {
        return if (isExist(localPath)) strategy.getFile(localPath) else null
    }

    override fun getFile(localPath: String, creationStrategy: FileCreationStrategy): File? {
        return if (isExist(localPath, creationStrategy)) strategy.getFile(
            localPath,
            creationStrategy
        ) else null
    }

    override fun getBitmap(localPath: String): Bitmap? {
        return if (isExist(localPath)) strategy.getBitmap(localPath) else null
    }

    override fun getBitmap(localPath: String, creationStrategy: FileCreationStrategy): Bitmap? {
        return if (isExist(localPath, creationStrategy)) strategy.getBitmap(
            localPath,
            creationStrategy
        ) else null
    }

    override fun moveTo(oldPath: String, newPath: String): String? {
        return if (isExist(oldPath)) {
            if (newPath.isNotEmpty()) strategy.moveTo(oldPath, newPath) else null
        } else null
    }

    override fun moveTo(
        oldPath: String,
        newPath: String,
        creationStrategy: FileCreationStrategy
    ): String? {
        return if (isExist(oldPath)) {
            if (newPath.isNotEmpty()) strategy.moveTo(oldPath, newPath, creationStrategy) else null
        } else null
    }

    override fun saveTo(inPath: String, outPath: String): String? {
        return if (isExist(inPath)) {
            if (outPath.isNotEmpty()) strategy.saveTo(inPath, outPath) else null
        } else null
    }

    override fun saveTo(
        inPath: String,
        outPath: String,
        creationStrategy: FileCreationStrategy
    ): String? {
        return if (isExist(inPath)) {
            if (outPath.isNotEmpty()) strategy.saveTo(inPath, outPath, creationStrategy) else null
        } else null
    }

    override fun saveTo(
        bitmap: Bitmap,
        outPath: String,
        creationStrategy: FileCreationStrategy
    ): String? {
        return if (outPath.isNotEmpty()) strategy.saveTo(
            bitmap,
            outPath,
            creationStrategy
        ) else null
    }

    override fun saveTo(
        bitmap: Bitmap,
        outPath: String
    ): String? {
        return if (outPath.isNotEmpty()) strategy.saveTo(
            bitmap,
            outPath
        ) else null
    }

    override fun writeTo(inPath: String, outPath: String, overwrite: Boolean): Boolean {
        return if (isExist(inPath)) {
            if (outPath.isNotEmpty()) strategy.writeTo(inPath, outPath, overwrite) else false
        } else false
    }

    override fun writeTo(
        inPath: String,
        outPath: String,
        creationStrategy: FileCreationStrategy,
        overwrite: Boolean
    ): Boolean {
        return if (isExist(inPath)) {
            if (outPath.isNotEmpty()) strategy.writeTo(
                inPath,
                outPath,
                creationStrategy,
                overwrite
            ) else false
        } else false
    }

    override fun deleteFile(localPath: String) {
        if (isExist(localPath)) strategy.deleteFile(localPath)
    }

    override fun deleteFile(localPath: String, creationStrategy: FileCreationStrategy) {
        if (isExist(localPath, creationStrategy)) strategy.deleteFile(localPath, creationStrategy)
    }

    override fun createFile(localPath: String, data: ByteArray?, temp: Boolean): File? {
        return strategy.createFile(localPath, data, temp)
    }

    override fun createFile(
        localPath: String?,
        data: ByteArray?,
        creationStrategy: FileCreationStrategy,
        temp: Boolean
    ): File? {
        return strategy.createFile(
            localPath ?: UUID.randomUUID().toString(),
            data,
            creationStrategy,
            temp
        )
    }

    override fun createPdfFile(localPath: String, document: PdfDocument?): File? {
        return strategy.createPdfFile(localPath, document).also { document?.close() }
    }

    override fun createPdfFile(
        localPath: String?,
        document: PdfDocument?,
        creationStrategy: FileCreationStrategy
    ): File? {
        return strategy.createPdfFile(
            localPath ?: UUID.randomUUID().toString(),
            document,
            creationStrategy
        ).also { document?.close() }
    }

    override fun getFileMD5(localPath: String): String? {
        return if (isExist(localPath)) strategy.getFileMD5(localPath) else null
    }

    override fun getFileMD5(localPath: String, creationStrategy: FileCreationStrategy): String? {
        return if (isExist(localPath, creationStrategy)) strategy.getFileMD5(
            localPath,
            creationStrategy
        ) else null
    }

    override fun getFileBase64(localPath: String): String? {
        return if (isExist(localPath)) strategy.getFileBase64(localPath) else null
    }

    override fun getFileBase64(localPath: String, creationStrategy: FileCreationStrategy): String? {
        return if (isExist(localPath, creationStrategy)) strategy.getFileBase64(
            localPath,
            creationStrategy
        ) else null
    }

    override fun getFileData(localPath: String): ByteArray? {
        return if (isExist(localPath)) strategy.getFileData(localPath) else null
    }

    override fun getFileData(
        localPath: String,
        creationStrategy: FileCreationStrategy
    ): ByteArray? {
        return if (isExist(localPath, creationStrategy)) strategy.getFileData(
            localPath,
            creationStrategy
        ) else null
    }

}