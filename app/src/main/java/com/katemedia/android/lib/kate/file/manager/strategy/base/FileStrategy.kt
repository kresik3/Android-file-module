package com.katemedia.android.lib.kate.file.manager.strategy.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.util.Base64
import android.util.Base64OutputStream
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy
import com.katemedia.android.lib.kate.file.utils.generateChecksum
import com.katemedia.android.lib.kate.file.utils.logging.LogUtils
import com.katemedia.android.lib.kate.file.utils.logging.data.CONTEXT_ERROR
import com.katemedia.android.lib.kate.logging.logging.factory.LoggingFactory
import com.katemedia.android.lib.kate.logging.logging.interfaces.ILogging
import java.io.*
import java.lang.ref.WeakReference
import java.security.MessageDigest


abstract class FileStrategy(appContext: Context) {

    private val loggingManager: ILogging by lazy { LoggingFactory.getInstance() }

    private val contextReference: WeakReference<Context?> = WeakReference(appContext)
    protected val context: Context?
        get() = contextReference.get()

    protected fun createFile(
        path: String,
        creationStrategy: FileCreationStrategy,
        isTemp: Boolean = false
    ): File? {
        val fileName = parseFileName(path)
        val parent = parseParentDir(path, creationStrategy)?.absolutePath ?: return null
        val newPath = "$parent/$fileName"

        return if (isTemp) createTempFile(newPath) else createFile(newPath)
    }

    protected fun createFile(path: String): File? {
        val fileName = parseFileName(path)
        val parent = parseParentDir(path)

        return File(parent, fileName)
            .let {
                try {
                    it.createNewFile(); it
                } catch (e: Exception) {
                    LogUtils.logCreateFile(path); null
                }
            }
    }

    protected fun createTempFile(path: String): File {
        val name = parseFileName(path)
        val parent = parseParentDir(path)
        val suffixIndex = name.lastIndexOf(".")
        val suffix = if (suffixIndex == -1) null else name.substring(suffixIndex)
        val nameWithoutSuffix = if (suffixIndex == -1) name else name.substring(0, suffixIndex)

        return File.createTempFile(nameWithoutSuffix, suffix, parent)
    }

    protected fun createDir(path: String, creationStrategy: FileCreationStrategy): File? {
        return createDir(getRootDir(creationStrategy), path)
    }

    protected fun write(file: File, data: ByteArray?): File {
        data ?: return file

        try {
            file.mkdirs()

            FileOutputStream(file).apply {
                write(data)
                flush()
                close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loggingManager.logStackTrace(e, "writeInFile")
        }

        return file
    }

    protected fun write(file: File, document: PdfDocument?): File {
        document ?: return file

        try {
            file.mkdirs()

            document.writeTo(FileOutputStream(file))
        } catch (e: Exception) {
            e.printStackTrace()
            loggingManager.logStackTrace(e, "writeInPDF")
        }

        return file
    }

    protected fun write(file: File, data: Bitmap): File {
        try {
            file.mkdirs()

            FileOutputStream(file).apply {
                data.compress(Bitmap.CompressFormat.PNG, 100, this)
                flush()
                close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loggingManager.logStackTrace(e, "writeInBitmap")
        }

        return file
    }


    protected fun write(inFile: File, outFile: File, overwrite: Boolean): Boolean {
        try {
            outFile.mkdirs()

            FileInputStream(inFile).use { fis ->
                FileOutputStream(outFile, !overwrite).use { fos ->
                    val buf = ByteArray(8 * 1024)
                    var len: Int
                    while (fis.read(buf).also { len = it } > 0) {
                        fos.write(buf, 0, len)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loggingManager.logStackTrace(e, "rewriteFile")
            return false
        }

        return true
    }

    protected fun deleteFile(file: File?) {
        if (deleteDir(file) == false) file?.delete()
    }

    protected fun deleteDir(file: File?): Boolean? {
        file ?: return null
        if (!file.isDirectory) return false

        val children = file.list()
        children?.let { let ->
            for (i in children.indices) {
                deleteFile(File(file, children[i]))
            }
        }

        return true
    }

    protected fun getRootDir(creationStrategy: FileCreationStrategy): File? {
        return when (creationStrategy) {
            FileCreationStrategy.EXTERNAL -> context?.getExternalFilesDir(null)
            FileCreationStrategy.INTERNAL -> context?.filesDir
        }.also { if (it == null) LogUtils.logE(CONTEXT_ERROR) }
    }

    private fun parseFileName(path: String) = path.substring(path.lastIndexOf("/") + 1)

    private fun parseParentDir(path: String, creationStrategy: FileCreationStrategy): File? {
        val dir = path.substring(0, path.lastIndexOf("/") + 1)

        return createDir(dir, creationStrategy)
    }

    private fun parseParentDir(path: String): File {
        val dir = path.substring(0, path.lastIndexOf("/") + 1)

        return File(dir).apply { mkdirs() }
    }

    private fun createDir(parent: File?, child: String): File? {
        parent ?: return null

        return File(parent, child).also { it.mkdirs() }
    }

    protected fun getFileChecksum(digest: MessageDigest, file: File): String? {
        val fis = FileInputStream(file)
        try {
            val byteArray = ByteArray(1024)
            var bytesCount: Int
            while (fis.read(byteArray).also { bytesCount = it } != -1) {
                digest.update(byteArray, 0, bytesCount)
            }
        } catch (e: Exception) {
            loggingManager.logStackTrace(e, "getFileChecksum")
            return null
        } finally {
            fis.close()
        }

        return generateChecksum(digest)
    }

    protected fun getFileBase64(file: File): String {
        return ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                file.inputStream().use { inputStream ->
                    inputStream.copyTo(base64FilterStream)
                }
            }

            outputStream.toString()
        }
    }

    protected fun read(file: File): ByteArray? {
        val bytes = ByteArray(file.length().toInt())

        try {
            BufferedInputStream(FileInputStream(file)).run {
                read(bytes, 0, bytes.size)
                close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loggingManager.logStackTrace(e, "readFile")
            return null
        }

        return bytes
    }

}