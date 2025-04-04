package com.katemedia.android.lib.kate.file.manager.interfaces

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy
import java.io.File

interface ILocalFileManager {

    /**
     * The method checks a file is exist for an absolute passed path.
     * @param localPath is a local path for a file checking (Type: String).
     * @return true if the file for the path is exist, false in other case.
     */
    fun isExist(localPath: String): Boolean

    /**
     * The method checks a file is exist for a passed path for special type of storages.
     * @param localPath is a local path for a file checking (Type: String).
     * @param creationStrategy is a type of local internal or external storage {@link FileCreationStrategy}.
     * @return true if the file for the path is exist, false in other case.
     */
    fun isExist(localPath: String, creationStrategy: FileCreationStrategy): Boolean

    /**
     * The method takes a file for an absolute passed path.
     * @param localPath is a local path for a file (Type: String).
     * @return {@link File} if the file for the path is exist, null in other case.
     */
    fun getFile(localPath: String): File?

    /**
     * The method takes a file for a passed path for special type of storages.
     * @param localPath is a local path for a file (Type: String).
     * @param creationStrategy is a type of local internal or external storage {@link FileCreationStrategy}.
     * @return {@link File} if the file for the path is exist, null in other case.
     */
    fun getFile(localPath: String, creationStrategy: FileCreationStrategy): File?

    /**
     * The method takes a bitmap for a passed path.
     * @param localPath is a local path for a file (Type: String).
     * @return {@link Bitmap} if the file for the path is exist, null in other case.
     */
    fun getBitmap(localPath: String): Bitmap?

    fun getBitmap(localPath: String, creationStrategy: FileCreationStrategy): Bitmap?

    /**
     * The method moves a file inside one type of storages. The file for the old path won'n be available.
     * @param oldPath is an absolute local path for a source file (Type: String).
     * @param newPath is an absolute local path for a output file (Type: String).
     * @return {@link File#getAbsolutePath()} for a new path. Can be null if something went wrong.
     */
    fun moveTo(oldPath: String, newPath: String): String?

    /**
     * The method moves a file between type of storages. The file for the old path won'n be available.
     * @param oldPath is an absolute local path for a source file (Type: String).
     * @param newPath is a local path for a output file (Type: String).
     * @param creationStrategy is a type of local internal or external storage {@link FileCreationStrategy}.
     * @return {@link File#getAbsolutePath()} for a new path. Can be null if something went wrong.
     */
    fun moveTo(oldPath: String, newPath: String, creationStrategy: FileCreationStrategy): String?

    /**
     * The method saves a file inside one type of storages. The file for the old path will be available.
     * @param inPath is an absolute local path for a source file (Type: String).
     * @param outPath is an absolute local path for a output file (Type: String).
     * @return {@link File#getAbsolutePath()} for a new path. Can be null if something went wrong.
     */
    fun saveTo(inPath: String, outPath: String): String?

    /**
     * The method saves a file between type of storages. The file for the old path will be available.
     * @param inPath is an absolute local path for a source file (Type: String).
     * @param outPath is a local path for a output file (Type: String).
     * @param creationStrategy is a type of local internal or external storage {@link FileCreationStrategy}.
     * @return {@link File#getAbsolutePath()} for a new path. Can be null if something went wrong.
     */
    fun saveTo(inPath: String, outPath: String, creationStrategy: FileCreationStrategy): String?

    /**
     * The method saves a bitmap between type of storages. The file for the old path will be available.
     * @param Bitmap is a local path for a source file (Type: String).
     * @param outPath is a local path for a output file (Type: String).
     * @param creationStrategy is a type of local internal or external storage {@link FileCreationStrategy}.
     * @return {@link File#getAbsolutePath()} for a new path. Can be null if something went wrong.
     */
    fun saveTo(bitmap: Bitmap, outPath: String, creationStrategy: FileCreationStrategy): String?

    /**
     * The method saves a bitmap between type of storages. The file for the old path will be available.
     * @param Bitmap is a local path for a source file (Type: String).
     * @param outPath is a local path for a output file (Type: String).
     * @return {@link File#getAbsolutePath()} for a new path. Can be null if something went wrong.
     */
    fun saveTo(bitmap: Bitmap, outPath: String): String?

    /**
     * The method saves a file inside one type of storages. The file for the old path will be available.
     * @param inPath is an absolute local path for a source file (Type: String).
     * @param outPath is an absolute local path for a output file (Type: String).
     * @param overwrite is flag to rewrite file or not (Type: Boolean; Default: true).
     * @return {@link File#getAbsolutePath()} for a new path. Can be null if something went wrong.
     */
    fun writeTo(inPath: String, outPath: String, overwrite: Boolean = true): Boolean

    /**
     * The method saves a file between type of storages. The file for the old path will be available.
     * @param inPath is an absolute local path for a source file (Type: String).
     * @param outPath is a local path for a output file (Type: String).
     * @param creationStrategy is a type of local internal or external storage {@link FileCreationStrategy}.
     * @param overwrite is flag to rewrite file or not (Type: Boolean; Default: true).
     * @return {@link File#getAbsolutePath()} for a new path. Can be null if something went wrong.
     */
    fun writeTo(
        inPath: String,
        outPath: String,
        creationStrategy: FileCreationStrategy,
        overwrite: Boolean = true
    ): Boolean

    /**
     * The method deletes a file for an absolute passed path.
     * @param localPath is an absolute local path for a file (Type: String).
     */
    fun deleteFile(localPath: String)

    /**
     * The method deletes a file for a passed path for special type of storages.
     * @param localPath is a local path for a file (Type: String).
     * @param creationStrategy is a type of local internal or external storage {@link FileCreationStrategy}.
     */
    fun deleteFile(localPath: String, creationStrategy: FileCreationStrategy)

    /**
     * The method creates a file inside one type of storages.
     * @param localPath is an absolute local path for a source file (Type: String).
     * @param data is data that has to write into the file (Type: ByteArray; Default: null).
     * @param temp is a flag to create temporary file (Type: ByteArray; Default: false).
     * @return instance of the new{@link File}. Can be null if something went wrong.
     */
    fun createFile(localPath: String, data: ByteArray? = null, temp: Boolean = false): File?

    /**
     * The method creates a file inside one type of storages.
     * @param localPath is an absolute local path for a source file (Type: String; Default: null (creates a file with a random name in a root dir)).
     * @param data is data that has to write into the file (Type: ByteArray; Default: null).
     * @param creationStrategy is a type of local internal or external storage {@link FileCreationStrategy}.
     * @param temp is a flag to create temporary file (Type: ByteArray; Default: false).
     * @return instance of the new{@link File}. Can be null if something went wrong.
     */
    fun createFile(
        localPath: String? = null,
        data: ByteArray? = null,
        creationStrategy: FileCreationStrategy,
        temp: Boolean = false
    ): File?

    /**
     * The method creates a pdf file inside one type of storages.
     * @param localPath is an absolute local path for a source file (Type: String).
     * @param document is data that describes pdf image file (Type: PdfDocument; Default: null).
     * @return instance of the new{@link File}. Can be null if something went wrong.
     */
    fun createPdfFile(localPath: String, document: PdfDocument? = null): File?

    /**
     * The method creates a pdf file inside one type of storages.
     * @param localPath is an absolute local path for a source file (Type: String; Default: null (creates a file with a random name in a root dir)).
     * @param document is data that describes pdf image file (Type: PdfDocument; Default: null).
     * @param creationStrategy is a type of local internal or external storage {@link FileCreationStrategy}.
     * @return instance of the new{@link File}. Can be null if something went wrong.
     */
    fun createPdfFile(
        localPath: String? = null,
        document: PdfDocument? = null,
        creationStrategy: FileCreationStrategy
    ): File?

    fun getFileMD5(localPath: String): String?

    fun getFileMD5(localPath: String, creationStrategy: FileCreationStrategy): String?

    fun getFileBase64(localPath: String): String?

    fun getFileBase64(localPath: String, creationStrategy: FileCreationStrategy): String?

    fun getFileData(localPath: String): ByteArray?

    fun getFileData(localPath: String, creationStrategy: FileCreationStrategy): ByteArray?

}