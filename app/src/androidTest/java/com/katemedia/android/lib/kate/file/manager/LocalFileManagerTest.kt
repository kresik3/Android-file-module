package com.katemedia.android.lib.kate.file.manager

import android.app.Application
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.katemedia.android.lib.kate.file.manager.interfaces.ILocalFileManager
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy
import com.katemedia.android.lib.kate.logging.initialization.Initializer
import com.katemedia.android.lib.kate.logging.model.LoggingSetting
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.properties.Delegates

@RunWith(AndroidJUnit4::class)
class LocalFileManagerTest {

    private lateinit var localFileManager: ILocalFileManager

    private var internalPath: String by Delegates.notNull()
    private var externalPath: String by Delegates.notNull()

    @Before
    fun initializeDataSource() {
        val app = ApplicationProvider.getApplicationContext<Application>()

        localFileManager = LocalFileManager(app)
        internalPath = app.filesDir.absolutePath
        externalPath = app.getExternalFilesDir(null)!!.absolutePath

        Initializer.initialize(app, getLoggingData())

        clearStorages()
    }

    private fun getLoggingData(): LoggingSetting {
        return LoggingSetting(
            appLoggingEnabled = false,
            activityLoggingEnabled = false,
            fragmentLoggingEnabled = false,
            clickLoggingEnabled = false,
            stackTraceEnabled = false,
            eventLoggingEnabled = false
        )
    }

    private fun clearStorages() {
        localFileManager.deleteFile(internalPath)
        localFileManager.deleteFile(externalPath)
    }


    //region CHECKING EXIST FILE

    @Test
    fun Success_is_exist_multi_absolute_path_internally() {
        val localPath = "images/test.png"
        val fullPath = "$internalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_exist(fullPath, true)
    }

    @Test
    fun Success_is_exist_single_absolute_path_internally() {
        val localPath = "test.png"
        val fullPath = "$internalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_exist(fullPath, true)
    }

    @Test
    fun Success_is_exist_multi_absolute_path_externally() {
        val localPath = "images/test.png"
        val fullPath = "$externalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_exist(fullPath, true)
    }

    @Test
    fun Success_is_exist_single_absolute_path_externally() {
        val localPath = "test.png"
        val fullPath = "$externalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_exist(fullPath, true)
    }

    @Test
    fun Success_is_not_exist_single_absolute_path() {
        val localPath = "test.png"

        check_exist("$externalPath/$localPath", false)
    }

    @Test
    fun Success_is_exist_empty_absolute_path() {
        check_exist("", false)
    }

    private fun check_exist(path: String, expectedResult: Boolean) {
        val isExist = localFileManager.isExist(path)
        isExist shouldBe expectedResult
    }

    //endregion CHECKING EXIST FILE


    //region CHECKING EXIST FILE WITH STORAGE

    @Test
    fun Success_is_exist_multi_path_internally() {
        val localPath = "images/test.png"

        localFileManager.createFile(localPath, creationStrategy = FileCreationStrategy.INTERNAL)

        check_exist_with_storage(localPath, FileCreationStrategy.INTERNAL, true)
    }

    @Test
    fun Success_is_exist_single_path_internally() {
        val localPath = "test.png"

        localFileManager.createFile(localPath, creationStrategy = FileCreationStrategy.INTERNAL)

        check_exist_with_storage(localPath, FileCreationStrategy.INTERNAL, true)
    }

    @Test
    fun Success_is_exist_multi_path_externally() {
        val localPath = "images/test.png"

        localFileManager.createFile(localPath, creationStrategy = FileCreationStrategy.EXTERNAL)

        check_exist_with_storage(localPath, FileCreationStrategy.EXTERNAL, true)
    }

    @Test
    fun Success_is_exist_single_path_externally() {
        val localPath = "test.png"

        localFileManager.createFile(localPath, creationStrategy = FileCreationStrategy.EXTERNAL)

        check_exist_with_storage(localPath, FileCreationStrategy.EXTERNAL, true)
    }

    @Test
    fun Success_is_not_exist_single_path() {
        val localPath = "test.png"

        check_exist_with_storage(localPath, FileCreationStrategy.INTERNAL, false)
    }

    @Test
    fun Success_is_exist_empty_path() {
        check_exist_with_storage("", FileCreationStrategy.EXTERNAL, false)
    }

    private fun check_exist_with_storage(
        path: String,
        creationStrategy: FileCreationStrategy,
        expectedResult: Boolean
    ) {
        val isExist = localFileManager.isExist(path, creationStrategy)
        isExist shouldBe expectedResult
    }

    //endregion CHECKING EXIST FILE WITH STORAGE


    //region CHECKING GET FILE

    @Test
    fun Success_get_file_exist_multi_absolute_path_internally() {
        val localPath = "images/test.png"
        val fullPath = "$internalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_not_null_file_absolute_path(fullPath)
    }

    @Test
    fun Success_get_file_exist_single_absolute_path_internally() {
        val localPath = "test.png"
        val fullPath = "$internalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_not_null_file_absolute_path(fullPath)
    }

    @Test
    fun Success_get_file_exist_multi_absolute_path_externally() {
        val localPath = "images/test.png"
        val fullPath = "$externalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_not_null_file_absolute_path(fullPath)
    }

    @Test
    fun Success_get_file_exist_single_absolute_path_externally() {
        val localPath = "test.png"
        val fullPath = "$externalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_not_null_file_absolute_path(fullPath)
    }

    @Test
    fun Success_get_not_file_exist_single_absolute_path() {
        val localPath = "test.png"

        check_null_file_absolute_path("$externalPath/$localPath")
    }

    @Test
    fun Success_get_file_empty_absolute_path() {
        check_null_file_absolute_path("")
    }

    private fun check_null_file_absolute_path(path: String) {
        val file = localFileManager.getFile(path)
        file shouldBe null
    }

    private fun check_not_null_file_absolute_path(path: String) {
        val file = localFileManager.getFile(path)
        file shouldNotBe null
        file!!.exists() shouldBe true
    }

    //endregion CHECKING GET FILE


    //region CHECKING GET FILE WITH STORAGE

    @Test
    fun Success_get_file_exist_multi_path_internally() {
        val localPath = "images/test.png"

        localFileManager.createFile(localPath, creationStrategy = FileCreationStrategy.INTERNAL)

        check_not_null_file(localPath, FileCreationStrategy.INTERNAL)
    }

    @Test
    fun Success_get_file_exist_single_path_internally() {
        val localPath = "test.png"

        localFileManager.createFile(localPath, creationStrategy = FileCreationStrategy.INTERNAL)

        check_not_null_file(localPath, FileCreationStrategy.INTERNAL)
    }

    @Test
    fun Success_get_file_exist_multi_path_externally() {
        val localPath = "images/test.png"

        localFileManager.createFile(localPath, creationStrategy = FileCreationStrategy.EXTERNAL)

        check_not_null_file(localPath, FileCreationStrategy.EXTERNAL)
    }

    @Test
    fun Success_get_file_exist_single_path_externally() {
        val localPath = "test.png"

        localFileManager.createFile(localPath, creationStrategy = FileCreationStrategy.EXTERNAL)

        check_not_null_file(localPath, FileCreationStrategy.EXTERNAL)
    }

    @Test
    fun Success_get_not_file_exist_single_path() {
        val localPath = "test.png"

        check_null_file(localPath, FileCreationStrategy.EXTERNAL)
    }

    @Test
    fun Success_get_file_empty_path() {
        check_null_file("", FileCreationStrategy.INTERNAL)
    }

    private fun check_null_file(path: String, creationStrategy: FileCreationStrategy) {
        val file = localFileManager.getFile(path, creationStrategy)
        file shouldBe null
    }

    private fun check_not_null_file(path: String, creationStrategy: FileCreationStrategy) {
        val file = localFileManager.getFile(path, creationStrategy)
        file shouldNotBe null
        file!!.exists() shouldBe true
    }

    //endregion CHECKING GET FILE WITH STORAGE


    //region CHECKING GET BITMAP

    @Test
    fun Success_get_bitmap_exist_multi_path_internally() {
        val localPath = "images/test.png"
        val fullPath = "$internalPath/$localPath"

        val manualFile = localFileManager.createFile(fullPath)
        create_test_bitmap_file(manualFile)

        check_not_null_bitmap(fullPath)
    }

    @Test
    fun Success_get_bitmap_exist_single_path_internally() {
        val localPath = "test.png"
        val fullPath = "$internalPath/$localPath"

        val manualFile = localFileManager.createFile(fullPath)
        create_test_bitmap_file(manualFile)

        check_not_null_bitmap(fullPath)
    }

    @Test
    fun Success_get_bitmap_exist_multi_path_externally() {
        val localPath = "images/test.png"
        val fullPath = "$externalPath/$localPath"

        val manualFile = localFileManager.createFile(fullPath)
        create_test_bitmap_file(manualFile)

        check_not_null_bitmap(fullPath)
    }

    @Test
    fun Success_get_bitmap_exist_single_path_externally() {
        val localPath = "test.png"
        val fullPath = "$externalPath/$localPath"

        val manualFile = localFileManager.createFile(fullPath)
        create_test_bitmap_file(manualFile)

        check_not_null_bitmap(fullPath)
    }

    @Test
    fun Success_get_not_bitmap_exist_single_path() {
        val localPath = "test.png"

        check_null_bitmap("$externalPath/$localPath")
    }

    @Test
    fun Success_get_bitmap_empty_path() {
        check_null_bitmap("")
    }

    private fun create_test_bitmap_file(file: File?) {
        file shouldNotBe null
        file?.isDirectory shouldNotBe true
        FileOutputStream(file).apply {
            val conf = Bitmap.Config.ARGB_8888
            val bmp = Bitmap.createBitmap(20, 20, conf)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, this)
            flush()
            close()
        }
    }

    private fun check_null_bitmap(path: String) {
        val file = localFileManager.getBitmap(path)
        file shouldBe null
    }

    private fun check_not_null_bitmap(path: String) {
        val bitmap = localFileManager.getBitmap(path)
        bitmap shouldNotBe null
    }

    //endregion CHECKING GET BITMAP


    //region CHECKING MOVE TO SAME STORAGE

    @Test
    fun Success_move_exist_from_single_path_internal_to_internal() {
        val localOldPath = "test.png"
        val localNewPath = "test1.png"
        val oldFullPath = "$internalPath/$localOldPath"
        val newFullPath = "$internalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_move_border(
            oldFullPath, newFullPath,
            newFullPath, existOldFile = false, existNewFile = true
        )
    }

    @Test
    fun Success_move_exist_from_multi_path_internal_to_internal() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"
        val oldFullPath = "$internalPath/$localOldPath"
        val newFullPath = "$internalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_move_border(
            oldFullPath, newFullPath,
            newFullPath, existOldFile = false, existNewFile = true
        )
    }

    @Test
    fun Success_move_exist_from_single_path_external_to_external() {
        val localOldPath = "test.png"
        val localNewPath = "test1.png"
        val oldFullPath = "$externalPath/$localOldPath"
        val newFullPath = "$externalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_move_border(
            oldFullPath, newFullPath,
            newFullPath, existOldFile = false, existNewFile = true
        )
    }

    @Test
    fun Success_move_exist_from_multi_path_externa_to_external() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"
        val oldFullPath = "$externalPath/$localOldPath"
        val newFullPath = "$externalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_move_border(
            oldFullPath, newFullPath,
            newFullPath, existOldFile = false, existNewFile = true
        )
    }

    @Test
    fun Success_move_not_exist_from() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"

        check_move_border(
            "$externalPath/$localOldPath", "$externalPath/$localNewPath",
            null, existOldFile = false, existNewFile = false
        )
    }

    @Test
    fun Success_move_from_empty_externa_to_external() {
        val localNewPath = "images/test1.png"

        check_move_border(
            "", "$externalPath/$localNewPath",
            null, existOldFile = false, existNewFile = false
        )
    }

    @Test
    fun Success_move_from_external_to_empty_external() {
        val localOldPath = "images/test.png"

        check_move_border(
            "$externalPath/$localOldPath", "",
            null, existOldFile = false, existNewFile = false
        )
    }

    @Test
    fun Success_move_from_empty_externa_to_empty_external() {
        check_move_border(
            "", "",
            null, false, false
        )
    }

    private fun check_move_border(
        oldFullPath: String,
        newFullPath: String,
        returnedPath: String?,
        existOldFile: Boolean,
        existNewFile: Boolean
    ) {
        val path = localFileManager.moveTo(oldFullPath, newFullPath)
        path shouldBe returnedPath

        val oldFile = File(oldFullPath)
        val newFile = File(newFullPath)
        oldFile.exists() shouldBe existOldFile
        newFile.exists() shouldBe existNewFile
    }

    //endregion CHECKING MOVE TO SAME STORAGE


    //region CHECKING MOVE TO DIFFERENT STORAGE

    @Test
    fun Success_move_exist_from_single_path_internal_to_external() {
        val localOldPath = "test.png"
        val localNewPath = "test1.png"
        val oldFullPath = "$internalPath/$localOldPath"
        val newFullPath = "$externalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_move_out_border(
            oldFullPath,
            newFullPath,
            localNewPath,
            FileCreationStrategy.EXTERNAL,
            newFullPath,
            existOldFile = false,
            existNewFile = true
        )
    }

    @Test
    fun Success_move_exist_from_multi_path_internal_to_external() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"
        val oldFullPath = "$internalPath/$localOldPath"
        val newFullPath = "$externalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_move_out_border(
            oldFullPath,
            newFullPath,
            localNewPath,
            FileCreationStrategy.EXTERNAL,
            newFullPath,
            existOldFile = false,
            existNewFile = true
        )
    }

    @Test
    fun Success_move_exist_from_single_path_external_to_internal() {
        val localOldPath = "test.png"
        val localNewPath = "test1.png"
        val oldFullPath = "$externalPath/$localOldPath"
        val newFullPath = "$internalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_move_out_border(
            oldFullPath,
            newFullPath,
            localNewPath,
            FileCreationStrategy.INTERNAL,
            newFullPath,
            existOldFile = false,
            existNewFile = true
        )
    }

    @Test
    fun Success_move_exist_from_multi_path_external_to_internal() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"
        val oldFullPath = "$externalPath/$localOldPath"
        val newFullPath = "$internalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_move_out_border(
            oldFullPath,
            newFullPath,
            localNewPath,
            FileCreationStrategy.INTERNAL,
            newFullPath,
            existOldFile = false,
            existNewFile = true
        )
    }

    @Test
    fun Success_move_not_exist_from_external_to_internal() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"

        check_move_out_border(
            "$externalPath/$localOldPath",
            "",
            localNewPath,
            FileCreationStrategy.INTERNAL,
            null,
            existOldFile = false,
            existNewFile = false
        )
    }

    @Test
    fun Success_move_not_exist_from_empty_external_to_internal() {
        val localNewPath = "images/test1.png"

        check_move_out_border(
            "", "", localNewPath, FileCreationStrategy.INTERNAL,
            null, false, false
        )
    }

    @Test
    fun Success_move_not_exist_from_external_to_empty_internal() {
        val localOldPath = "images/test.png"

        check_move_out_border(
            "$externalPath/$localOldPath", "", "", FileCreationStrategy.INTERNAL,
            null, existOldFile = false, existNewFile = false
        )
    }

    private fun check_move_out_border(
        oldFullPath: String,
        newFullPath: String,
        newPath: String,
        creationStrategy: FileCreationStrategy,
        returnedPath: String?,
        existOldFile: Boolean,
        existNewFile: Boolean
    ) {
        val path = localFileManager.moveTo(oldFullPath, newPath, creationStrategy)
        path shouldBe returnedPath

        val oldFile = File(oldFullPath)
        val newFile = File(newFullPath)
        oldFile.exists() shouldBe existOldFile
        newFile.exists() shouldBe existNewFile
    }

    //endregion CHECKING MOVE TO DIFFERENT STORAGE


    //region CHECKING SAVE TO FILE IN SAME STORAGE

    @Test
    fun Success_save_single_path_internal_to_internal() {
        val localOldPath = "test.png"
        val localNewPath = "test1.png"
        val oldFullPath = "$internalPath/$localOldPath"
        val newFullPath = "$internalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_save_border(
            oldFullPath, newFullPath,
            newFullPath, existOldFile = true, existNewFile = true
        )
    }

    @Test
    fun Success_save_multi_path_internal_to_internal() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"
        val oldFullPath = "$internalPath/$localOldPath"
        val newFullPath = "$internalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_save_border(
            oldFullPath, newFullPath,
            newFullPath, existOldFile = true, existNewFile = true
        )
    }

    @Test
    fun Success_save_single_path_external_to_external() {
        val localOldPath = "test.png"
        val localNewPath = "test1.png"
        val oldFullPath = "$externalPath/$localOldPath"
        val newFullPath = "$externalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_save_border(
            "$externalPath/$localOldPath", newFullPath,
            newFullPath, existOldFile = true, existNewFile = true
        )
    }

    @Test
    fun Success_save_multi_path_externa_to_external() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"
        val oldFullPath = "$externalPath/$localOldPath"
        val newFullPath = "$externalPath/$localNewPath"

        localFileManager.createFile(oldFullPath)

        check_save_border(
            "$externalPath/$localOldPath", newFullPath,
            newFullPath, existOldFile = true, existNewFile = true
        )
    }

    @Test
    fun Success_save_not_exist() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"

        check_move_border(
            "$externalPath/$localOldPath", "$externalPath/$localNewPath",
            null, existOldFile = false, existNewFile = false
        )
    }

    @Test
    fun Success_save_empty_externa_to_external() {
        val localNewPath = "images/test1.png"

        check_move_border(
            "", "$externalPath/$localNewPath",
            null, existOldFile = false, existNewFile = false
        )
    }

    @Test
    fun Success_save_externa_to_empty_external() {
        val localOldPath = "images/test.png"

        check_move_border(
            "$externalPath/$localOldPath", "",
            null, existOldFile = false, existNewFile = false
        )
    }

    private fun check_save_border(
        oldFullPath: String,
        newFullPath: String,
        returnedPath: String?,
        existOldFile: Boolean,
        existNewFile: Boolean
    ) {
        val path = localFileManager.saveTo(oldFullPath, newFullPath)
        path shouldBe returnedPath

        val oldFile = File(oldFullPath)
        val newFile = File(newFullPath)
        oldFile.exists() shouldBe existOldFile
        newFile.exists() shouldBe existNewFile
    }

    //endregion CHECKING SAVE TO FILE IN SAME STORAGE


    //region CHECKING SAVE TO FILE DIFFERENT STORAGE

    @Test
    fun Success_save_single_path_internal_to_external() {
        val localOldPath = "test.png"
        val localNewPath = "test1.png"

        localFileManager.createFile(localOldPath, creationStrategy = FileCreationStrategy.INTERNAL)

        check_save_out_border(
            "$internalPath/$localOldPath",
            externalPath,
            localNewPath,
            FileCreationStrategy.EXTERNAL,
            "$externalPath/$localNewPath",
            existOldFile = true,
            existNewFile = true
        )
    }

    @Test
    fun Success_save_multi_path_internal_to_external() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"

        localFileManager.createFile(localOldPath, creationStrategy = FileCreationStrategy.INTERNAL)

        check_save_out_border(
            "$internalPath/$localOldPath",
            externalPath,
            localNewPath,
            FileCreationStrategy.EXTERNAL,
            "$externalPath/$localNewPath",
            existOldFile = true,
            existNewFile = true
        )
    }

    @Test
    fun Success_save_single_path_external_to_internal() {
        val localOldPath = "test.png"
        val localNewPath = "test1.png"

        localFileManager.createFile(localOldPath, creationStrategy = FileCreationStrategy.EXTERNAL)

        check_save_out_border(
            "$externalPath/$localOldPath",
            internalPath,
            localNewPath,
            FileCreationStrategy.INTERNAL,
            "$internalPath/$localNewPath",
            existOldFile = true,
            existNewFile = true
        )
    }

    @Test
    fun Success_save_multi_path_external_to_internal() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"

        localFileManager.createFile(localOldPath, creationStrategy = FileCreationStrategy.EXTERNAL)

        check_save_out_border(
            "$externalPath/$localOldPath",
            internalPath,
            localNewPath,
            FileCreationStrategy.INTERNAL,
            "$internalPath/$localNewPath",
            existOldFile = true,
            existNewFile = true
        )
    }

    @Test
    fun Success_save_not_exist_multi_path_external_to_internal() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"

        check_save_out_border(
            "$externalPath/$localOldPath",
            internalPath,
            localNewPath,
            FileCreationStrategy.INTERNAL,
            null,
            existOldFile = false,
            existNewFile = false
        )
    }

    @Test
    fun Success_save_empty() {
        val localNewPath = "images/test1.png"

        check_save_out_border(
            "", internalPath, localNewPath, FileCreationStrategy.INTERNAL,
            null, existOldFile = false, existNewFile = false
        )
    }

    @Test
    fun Success_save_external_to_empty_internal() {
        val localOldPath = "images/test.png"
        val localNewPath = "images/test1.png"

        check_save_out_border(
            "$externalPath/$localOldPath",
            internalPath,
            localNewPath,
            FileCreationStrategy.INTERNAL,
            null,
            existOldFile = false,
            existNewFile = false
        )
    }

    private fun check_save_out_border(
        oldFullPath: String,
        rootNewPath: String,
        newPath: String,
        creationStrategy: FileCreationStrategy,
        returnedPath: String?,
        existOldFile: Boolean,
        existNewFile: Boolean
    ) {
        val path = localFileManager.saveTo(oldFullPath, newPath, creationStrategy)
        path shouldBe returnedPath

        val oldFile = File(oldFullPath)
        val newFile = File("$rootNewPath/$newPath")
        oldFile.exists() shouldBe existOldFile
        newFile.exists() shouldBe existNewFile
    }

    //endregion CHECKING SAVE TO FILE IN DIFFERENT STORAGE


    //region CHECKING WRITE TO SAME STORAGE

    @Test
    fun Success_write_exist_from_single_path_internal_to_internal() {
        val localSourcePaths = listOf("test.txt", "test1.txt", "test2.txt")
        val localTargetPath = "test3.txt"
        val sourceFullPaths = localSourcePaths.map { "$internalPath/$it" }
        val targetFullPath = "$internalPath/$localTargetPath"

        check_write_border(sourceFullPaths, targetFullPath, false)
    }

    @Test
    fun Success_write_exist_from_single_path_internal_to_internal_everwrite() {
        val localSourcePaths = listOf("test.txt", "test1.txt", "test2.txt")
        val localTargetPath = "test3.txt"
        val sourceFullPaths = localSourcePaths.map { "$internalPath/$it" }
        val targetFullPath = "$internalPath/$localTargetPath"

        check_write_border(sourceFullPaths, targetFullPath, true)
    }

    @Test
    fun Success_write_exist_from_multi_path_internal_to_internal() {
        val localSourcePaths = listOf("t_files/test.txt", "t_files/test1.txt", "t_files/test2.txt")
        val localTargetPath = "t_files/test3.txt"
        val sourceFullPaths = localSourcePaths.map { "$internalPath/$it" }
        val targetFullPath = "$internalPath/$localTargetPath"

        check_write_border(sourceFullPaths, targetFullPath, false)
    }

    @Test
    fun Success_write_exist_from_single_path_external_to_external() {
        val localSourcePaths = listOf("test.txt", "test1.txt", "test2.txt")
        val localTargetPath = "test3.txt"
        val sourceFullPaths = localSourcePaths.map { "$externalPath/$it" }
        val targetFullPath = "$externalPath/$localTargetPath"

        check_write_border(sourceFullPaths, targetFullPath, false)
    }

    @Test
    fun Success_write_exist_from_single_path_external_to_external_everwrite() {
        val localSourcePaths = listOf("test.txt", "test1.txt", "test2.txt")
        val localTargetPath = "test3.txt"
        val sourceFullPaths = localSourcePaths.map { "$externalPath/$it" }
        val targetFullPath = "$externalPath/$localTargetPath"

        check_write_border(sourceFullPaths, targetFullPath, true)
    }

    @Test
    fun Success_write_exist_from_multi_path_externa_to_external() {
        val localSourcePaths = listOf("t_files/test.txt", "t_files/test1.txt", "t_files/test2.txt")
        val localTargetPath = "t_files/test3.txt"
        val sourceFullPaths = localSourcePaths.map { "$externalPath/$it" }
        val targetFullPath = "$externalPath/$localTargetPath"

        check_write_border(sourceFullPaths, targetFullPath, false)
    }

    private fun check_write_border(
        sourceFullPath: List<String>,
        targetFullPath: String,
        overwrite: Boolean
    ) {
        val data = "line\n".toByteArray()

        sourceFullPath.onEach { localFileManager.createFile(it, data = data) }

        localFileManager.createFile(targetFullPath)

        sourceFullPath.onEach {
            val success = localFileManager.writeTo(it, targetFullPath, overwrite)
            success shouldBe true
        }

        val resultFile = File(targetFullPath)
        resultFile.exists() shouldBe true

        FileInputStream(resultFile).use { fis ->
            ByteArray(8 * 1024).apply {
                val lines = copyOfRange(0, fis.read(this))
                if (overwrite) {
                    String(lines) shouldBe String(data)
                } else {
                    String(lines) shouldBe sourceFullPath.joinToString(separator = "") { String(data) }
                }
            }
        }
    }

    //endregion CHECKING WRITE TO SAME STORAGE


    //region CHECKING WRITE TO DIFFERENT STORAGE

    @Test
    fun Success_write_exist_from_single_path_strategy_internal_to_internal() {
        val localSourcePaths = listOf("test.txt", "test1.txt", "test2.txt")
        val sourceFullPaths = localSourcePaths.map { "$internalPath/$it" }
        val localTargetPath = "test3.txt"

        check_write_border(sourceFullPaths, localTargetPath, FileCreationStrategy.INTERNAL, false)
    }

    @Test
    fun Success_write_exist_from_single_path_strategy_internal_to_internal_everwrite() {
        val localSourcePaths = listOf("test.txt", "test1.txt", "test2.txt")
        val sourceFullPaths = localSourcePaths.map { "$internalPath/$it" }
        val localTargetPath = "test3.txt"

        check_write_border(sourceFullPaths, localTargetPath, FileCreationStrategy.INTERNAL, true)
    }

    @Test
    fun Success_write_exist_from_multi_path_strategy_internal_to_internal() {
        val localSourcePaths = listOf("t_files/test.txt", "t_files/test1.txt", "t_files/test2.txt")
        val sourceFullPaths = localSourcePaths.map { "$internalPath/$it" }
        val localTargetPath = "t_files/test3.txt"

        check_write_border(sourceFullPaths, localTargetPath, FileCreationStrategy.INTERNAL, false)
    }

    @Test
    fun Success_write_exist_from_single_path_strategy_external_to_external() {
        val localSourcePaths = listOf("test.txt", "test1.txt", "test2.txt")
        val sourceFullPaths = localSourcePaths.map { "$externalPath/$it" }
        val localTargetPath = "test3.txt"

        check_write_border(sourceFullPaths, localTargetPath, FileCreationStrategy.EXTERNAL, false)
    }

    @Test
    fun Success_write_exist_from_single_path_strategy_external_to_external_everwrite() {
        val localSourcePaths = listOf("test.txt", "test1.txt", "test2.txt")
        val sourceFullPaths = localSourcePaths.map { "$externalPath/$it" }
        val localTargetPath = "test3.txt"

        check_write_border(sourceFullPaths, localTargetPath, FileCreationStrategy.EXTERNAL, true)
    }

    @Test
    fun Success_write_exist_from_multi_path_strategy_externa_to_external() {
        val localSourcePaths = listOf("t_files/test.txt", "t_files/test1.txt", "t_files/test2.txt")
        val sourceFullPaths = localSourcePaths.map { "$externalPath/$it" }
        val localTargetPath = "t_files/test3.txt"

        check_write_border(sourceFullPaths, localTargetPath, FileCreationStrategy.EXTERNAL, false)
    }

    private fun check_write_border(
        sourceFullPath: List<String>,
        targetFullPath: String,
        creationStrategy: FileCreationStrategy,
        overwrite: Boolean
    ) {
        val data = "line\n".toByteArray()

        sourceFullPath.onEach { localFileManager.createFile(it, data = data) }

        localFileManager.createFile(targetFullPath, creationStrategy = creationStrategy)

        sourceFullPath.onEach {
            val success = localFileManager.writeTo(it, targetFullPath, creationStrategy, overwrite)
            success shouldBe true
        }

        val resultFile = localFileManager.getFile(targetFullPath, creationStrategy)
        resultFile?.exists() shouldBe true

        FileInputStream(resultFile).use { fis ->
            ByteArray(8 * 1024).apply {
                val lines = copyOfRange(0, fis.read(this))
                if (overwrite) {
                    String(lines) shouldBe String(data)
                } else {
                    String(lines) shouldBe sourceFullPath.joinToString(separator = "") { String(data) }
                }
            }
        }
    }

    //endregion CHECKING WRITE TO DIFFERENT STORAGE


    //region CHECKING SAVE TO BITMAP

    @Test
    fun Success_save_single_path_bitmap_to_absolute_path_internal() {
        val localPath = "test.png"
        val newFullPath = "$internalPath/$localPath"

        check_save_bitmap_out_border(
            newFullPath, "$internalPath/$localPath", newFullPath, true
        )
    }

    @Test
    fun Success_save_multi_path_bitmap_to_absolute_path_internal() {
        val localPath = "images/test.png"
        val newFullPath = "$internalPath/$localPath"

        check_save_bitmap_out_border(
            newFullPath, "$internalPath/$localPath", newFullPath, true
        )
    }

    @Test
    fun Success_save_single_path_bitmap_to_absolute_path_external() {
        val localPath = "test.png"
        val newFullPath = "$externalPath/$localPath"

        check_save_bitmap_out_border(
            newFullPath, "$externalPath/$localPath", newFullPath, true
        )
    }

    @Test
    fun Success_save_multi_path_bitmap_to_absolute_path_external() {
        val localPath = "images/test.png"
        val newFullPath = "$externalPath/$localPath"

        check_save_bitmap_out_border(
            newFullPath, "$externalPath/$localPath", newFullPath, true
        )
    }

    @Test
    fun Success_save_bitmap_empty_path_absolute_path() {
        check_save_bitmap_out_border(
            "", "", null, false
        )
    }

    private fun check_save_bitmap_out_border(
        newFullPath: String,
        newPath: String,
        returnedPath: String?,
        expectedResult: Boolean
    ) {
        val bitmap = create_test_bitmap()
        val path = localFileManager.saveTo(bitmap, newPath)
        path shouldBe returnedPath
        File(newFullPath).exists() shouldBe expectedResult
    }

    private fun create_test_bitmap(): Bitmap {
        val conf = Bitmap.Config.ARGB_8888
        return Bitmap.createBitmap(20, 20, conf)
    }

    //endregion CHECKING SAVE TO BITMAP


    //region CHECKING SAVE TO BITMAP WITH STORAGE

    @Test
    fun Success_save_single_path_bitmap_to_internal() {
        val localPath = "test.png"
        val newFullPath = "$internalPath/$localPath"

        check_save_bitmap_out_border_with_storage(
            newFullPath, localPath, FileCreationStrategy.INTERNAL, newFullPath, true
        )
    }

    @Test
    fun Success_save_multi_path_bitmap_to_internal() {
        val localPath = "images/test.png"
        val newFullPath = "$internalPath/$localPath"

        check_save_bitmap_out_border_with_storage(
            newFullPath, localPath, FileCreationStrategy.INTERNAL, newFullPath, true
        )
    }

    @Test
    fun Success_save_single_path_bitmap_to_external() {
        val localPath = "test.png"
        val newFullPath = "$externalPath/$localPath"

        check_save_bitmap_out_border_with_storage(
            newFullPath, localPath, FileCreationStrategy.EXTERNAL, newFullPath, true
        )
    }

    @Test
    fun Success_save_multi_path_bitmap_to_external() {
        val localPath = "images/test.png"
        val newFullPath = "$externalPath/$localPath"

        check_save_bitmap_out_border_with_storage(
            newFullPath, localPath, FileCreationStrategy.EXTERNAL, newFullPath, true
        )
    }

    @Test
    fun Success_save_bitmap_empty_path() {
        check_save_bitmap_out_border_with_storage(
            "", "", FileCreationStrategy.EXTERNAL, null, false
        )
    }

    private fun check_save_bitmap_out_border_with_storage(
        newFullPath: String,
        newPath: String,
        creationStrategy: FileCreationStrategy,
        returnedPath: String?,
        expectedResult: Boolean
    ) {
        val bitmap = create_test_bitmap_with_storage()
        val path = localFileManager.saveTo(bitmap, newPath, creationStrategy)
        path shouldBe returnedPath
        File(newFullPath).exists() shouldBe expectedResult
    }

    private fun create_test_bitmap_with_storage(): Bitmap {
        val conf = Bitmap.Config.ARGB_8888
        return Bitmap.createBitmap(20, 20, conf)
    }

    //endregion CHECKING SAVE TO BITMAP WITH STORAGE


    //region CHECKING DELETE

    @Test
    fun Success_delete_single_absolute_path_internal() {
        val localPath = "test.png"
        val fullPath = "$internalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_delete(fullPath)
    }

    @Test
    fun Success_delete_multi_absolute_path_internal() {
        val localPath = "images/test.png"
        val fullPath = "$internalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_delete(fullPath)
    }

    @Test
    fun Success_delete_single_absolute_path_external() {
        val localPath = "test.png"
        val fullPath = "$externalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_delete(fullPath)
    }

    @Test
    fun Success_delete_multi_absolute_path_external() {
        val localPath = "images/test.png"
        val fullPath = "$externalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_delete(fullPath)
    }

    @Test
    fun Success_delete_exception_absolute_path() {
        localFileManager.deleteFile("")
    }

    private fun check_delete(path: String) {
        localFileManager.deleteFile(path)
        val file = File(path)
        file.exists() shouldBe false
    }

    //endregion CHECKING DELETE


    //region CHECKING DELETE WITH STORAGE

    @Test
    fun Success_delete_single_path_internal() {
        val localPath = "test.png"
        val fullPath = "$internalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_delete_with_storage(
            fullPath,
            localPath,
            FileCreationStrategy.INTERNAL
        )
    }

    @Test
    fun Success_delete_multi_path_internal() {
        val localPath = "images/test.png"
        val fullPath = "$internalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_delete_with_storage(
            fullPath,
            localPath,
            FileCreationStrategy.INTERNAL
        )
    }

    @Test
    fun Success_delete_single_path_external() {
        val localPath = "test.png"
        val fullPath = "$externalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_delete_with_storage(
            fullPath,
            localPath,
            FileCreationStrategy.EXTERNAL
        )
    }

    @Test
    fun Success_delete_multi_path_external() {
        val localPath = "images/test.png"
        val fullPath = "$externalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_delete_with_storage(
            fullPath,
            localPath,
            FileCreationStrategy.EXTERNAL
        )
    }

    @Test
    fun Success_delete_exception_with_storage() {
        localFileManager.deleteFile("", FileCreationStrategy.INTERNAL)
    }

    private fun check_delete_with_storage(
        absolutePath: String,
        path: String,
        creationStrategy: FileCreationStrategy
    ) {
        localFileManager.deleteFile(path, creationStrategy)
        val file = File(absolutePath)
        file.exists() shouldBe false
    }

    //endregion CHECKING DELETE WITH STORAGE


    //region CHECKING CREATE

    @Test
    fun Success_create_no_empty_no_temp_single_absolute_path_internal() {
        val localPath = "test.png"

        check_create_no_empty("$internalPath/$localPath", false)
    }

    @Test
    fun Success_create_empty_no_temp_single_absolute_path_internal() {
        val localPath = "test.png"

        check_create_empty("$internalPath/$localPath", false)
    }

    @Test
    fun Success_create_no_empty_temp_single_absolute_path_internal() {
        val localPath = "test"

        check_create_no_empty("$internalPath/$localPath", true)
    }

    @Test
    fun Success_create_empty_temp_single_absolute_path_internal() {
        val localPath = "test"

        check_create_empty("$internalPath/$localPath", true)
    }

    @Test
    fun Success_create_no_empty_no_temp_multi_absolute_path_internal() {
        val localPath = "images/test.png"

        check_create_no_empty("$internalPath/$localPath", false)
    }

    @Test
    fun Success_create_empty_no_temp_multi_absolute_path_internal() {
        val localPath = "images/test.png"

        check_create_empty("$internalPath/$localPath", false)
    }

    @Test
    fun Success_create_no_empty_temp_multi_absolute_path_internal() {
        val localPath = "images/test"

        check_create_no_empty("$internalPath/$localPath", true)
    }

    @Test
    fun Success_create_empty_temp_multi_absolute_path_internal() {
        val localPath = "images/test"

        check_create_empty("$internalPath/$localPath", true)
    }

    @Test
    fun Success_create_no_empty_no_temp_single_absolute_path_external() {
        val localPath = "test.png"

        check_create_no_empty("$externalPath/$localPath", false)
    }

    @Test
    fun Success_create_empty_no_temp_single_absolute_path_external() {
        val localPath = "test.png"

        check_create_empty("$externalPath/$localPath", false)
    }

    @Test
    fun Success_create_no_empty_temp_single_absolute_path_external() {
        val localPath = "test"

        check_create_no_empty("$externalPath/$localPath", true)
    }

    @Test
    fun Success_create_empty_temp_single_absolute_path_external() {
        val localPath = "test"

        check_create_empty("$externalPath/$localPath", true)
    }

    @Test
    fun Success_create_no_empty_no_temp_multi_absolute_path_external() {
        val localPath = "images/test.png"

        check_create_no_empty("$externalPath/$localPath", false)
    }

    @Test
    fun Success_create_empty_no_temp_multi_absolute_path_external() {
        val localPath = "images/test.png"

        check_create_empty("$externalPath/$localPath", false)
    }

    @Test
    fun Success_create_no_empty_temp_multi_absolute_path_external() {
        val localPath = "images/test"

        check_create_no_empty("$externalPath/$localPath", true)
    }

    @Test
    fun Success_create_empty_temp_multi_absolute_path_external() {
        val localPath = "images/test"

        check_create_empty("$externalPath/$localPath", true)
    }

    private fun check_create_no_empty(path: String, temp: Boolean) {
        val array = byteArrayOf(0x2E, 0x38)
        val newFile = localFileManager.createFile(path, array, temp)
        newFile shouldNotBe null
        newFile?.exists() shouldBe true
    }

    private fun check_create_empty(path: String, temp: Boolean) {
        val newFile = localFileManager.createFile(path, null, temp)
        newFile shouldNotBe null
        newFile?.exists() shouldBe true
    }

    //endregion CHECKING CREATE


    //region CHECKING CREATE WITH STORAGE

    @Test
    fun Success_create_no_empty_no_temp_single_internal() {
        val localPath = "test.png"

        check_create_no_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL,
            false
        )
    }

    @Test
    fun Success_create_empty_no_temp_single_internal() {
        val localPath = "test.png"

        check_create_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL,
            false
        )
    }

    @Test
    fun Success_create_no_empty_temp_single_internal() {
        val localPath = "test"

        check_create_no_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL,
            true
        )
    }

    @Test
    fun Success_create_empty_temp_single_internal() {
        val localPath = "test"

        check_create_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL,
            true
        )
    }

    @Test
    fun Success_create_no_empty_no_temp_multi_internal() {
        val localPath = "images/test.png"

        check_create_no_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL,
            false
        )
    }

    @Test
    fun Success_create_empty_no_temp_multi_internal() {
        val localPath = "images/test.png"

        check_create_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL,
            false
        )
    }

    @Test
    fun Success_create_no_empty_temp_multi_internal() {
        val localPath = "images/test"

        check_create_no_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL,
            true
        )
    }

    @Test
    fun Success_create_empty_temp_multi_internal() {
        val localPath = "images/test"

        check_create_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL,
            true
        )
    }

    @Test
    fun Success_create_no_empty_no_temp_single_external() {
        val localPath = "test.png"

        check_create_no_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL,
            false
        )
    }

    @Test
    fun Success_create_empty_no_temp_single_external() {
        val localPath = "test.png"

        check_create_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL,
            false
        )
    }

    @Test
    fun Success_create_no_empty_temp_single_external() {
        val localPath = "test"

        check_create_no_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL,
            true
        )
    }

    @Test
    fun Success_create_empty_temp_single_external() {
        val localPath = "test"

        check_create_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL,
            true
        )
    }

    @Test
    fun Success_create_no_empty_no_temp_multi_external() {
        val localPath = "images/test.png"

        check_create_no_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL,
            false
        )
    }

    @Test
    fun Success_create_empty_no_temp_multi_external() {
        val localPath = "images/test.png"

        check_create_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL,
            false
        )
    }

    @Test
    fun Success_create_no_empty_temp_multi_external() {
        val localPath = "images/test"

        check_create_no_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL,
            true
        )
    }

    @Test
    fun Success_create_empty_temp_multi_external() {
        val localPath = "images/test"

        check_create_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL,
            true
        )
    }

    @Test
    fun Success_create_no_empty_temp_null() {
        check_create_no_empty_with_storage(
            null,
            FileCreationStrategy.EXTERNAL,
            true
        )
    }

    @Test
    fun Success_create_empty_temp_null() {
        check_create_empty_with_storage(
            null,
            FileCreationStrategy.EXTERNAL,
            true
        )
    }

    private fun check_create_no_empty_with_storage(
        path: String?,
        creationStrategy: FileCreationStrategy,
        temp: Boolean
    ) {
        val array = byteArrayOf(0x2E, 0x38)
        val newFile = localFileManager.createFile(path, array, creationStrategy, temp)
        newFile shouldNotBe null
        newFile?.exists() shouldBe true
    }

    private fun check_create_empty_with_storage(
        path: String?,
        creationStrategy: FileCreationStrategy,
        temp: Boolean
    ) {
        val newFile = localFileManager.createFile(path, null, creationStrategy, temp)
        newFile shouldNotBe null
        newFile?.exists() shouldBe true
    }

    //endregion CHECKING CREATE WITH STORAGE


    //region CHECKING CREATE PDF

    @Test
    fun Success_create_pdf_no_empty_single_absolute_path_internal() {
        val localPath = "test.pdf"

        check_create_pdf_no_empty("$internalPath/$localPath")
    }

    @Test
    fun Success_create_pdf_empty_single_absolute_path_internal() {
        val localPath = "test.pdf"

        check_create_pdf_empty("$internalPath/$localPath")
    }

    @Test
    fun Success_create_pdf_no_empty_multi_absolute_path_internal() {
        val localPath = "file/test.pdf"

        check_create_pdf_no_empty("$internalPath/$localPath")
    }

    @Test
    fun Success_create_pdf_empty_multi_absolute_path_internal() {
        val localPath = "file/test.pdf"

        check_create_pdf_empty("$internalPath/$localPath")
    }

    @Test
    fun Success_create_pdf_no_empty_single_absolute_path_external() {
        val localPath = "test.pdf"

        check_create_pdf_no_empty("$externalPath/$localPath")
    }

    @Test
    fun Success_create_pdf_empty_single_absolute_path_external() {
        val localPath = "test.pdf"

        check_create_pdf_empty("$externalPath/$localPath")
    }

    @Test
    fun Success_create_pdf_no_empty_multi_absolute_path_external() {
        val localPath = "file/test.pdf"

        check_create_pdf_no_empty("$externalPath/$localPath")
    }

    @Test
    fun Success_create_pdf_empty_multi_absolute_path_external() {
        val localPath = "file/test.pdf"

        check_create_pdf_empty("$externalPath/$localPath")
    }

    private fun check_create_pdf_no_empty(path: String) {
        val document = PdfDocument()
        val newFile = localFileManager.createPdfFile(path, document)
        newFile shouldNotBe null
        newFile?.exists() shouldBe true
    }

    private fun check_create_pdf_empty(path: String) {
        val newFile = localFileManager.createPdfFile(path, null)
        newFile shouldNotBe null
        newFile?.exists() shouldBe true
    }

    //endregion CHECKING CREATE PDF


    //region CHECKING CREATE PDF WITH STORAGE

    @Test
    fun Success_create_pdf_no_empty_single_internal() {
        val localPath = "test.pdf"

        check_create_pdf_no_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL
        )
    }

    @Test
    fun Success_create_pdf_empty_single_internal() {
        val localPath = "test.pdf"

        check_create_pdf_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL
        )
    }

    @Test
    fun Success_create_pdf_no_empty_multi_internal() {
        val localPath = "file/test.pdf"

        check_create_pdf_no_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL
        )
    }

    @Test
    fun Success_create_pdf_empty_multi_internal() {
        val localPath = "file/test.pdf"

        check_create_pdf_empty_with_storage(
            localPath,
            FileCreationStrategy.INTERNAL
        )
    }

    @Test
    fun Success_create_pdf_no_empty_single_external() {
        val localPath = "test.pdf"

        check_create_pdf_no_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL
        )
    }

    @Test
    fun Success_create_pdf_empty_single_external() {
        val localPath = "test.pdf"

        check_create_pdf_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL
        )
    }

    @Test
    fun Success_create_pdf_no_empty_multi_external() {
        val localPath = "file/test.pdf"

        check_create_pdf_no_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL
        )
    }

    @Test
    fun Success_create_pdf_empty_multi_external() {
        val localPath = "file/test.png"

        check_create_pdf_empty_with_storage(
            localPath,
            FileCreationStrategy.EXTERNAL
        )
    }

    @Test
    fun Success_create_pdf_no_empty_null() {
        check_create_pdf_no_empty_with_storage(
            null,
            FileCreationStrategy.EXTERNAL
        )
    }

    @Test
    fun Success_create_pdf_empty_null() {
        check_create_pdf_empty_with_storage(
            null,
            FileCreationStrategy.EXTERNAL
        )
    }

    private fun check_create_pdf_no_empty_with_storage(
        path: String?,
        creationStrategy: FileCreationStrategy
    ) {
        val document = PdfDocument()
        val newFile = localFileManager.createPdfFile(path, document, creationStrategy)
        newFile shouldNotBe null
        newFile?.exists() shouldBe true
    }

    private fun check_create_pdf_empty_with_storage(
        path: String?,
        creationStrategy: FileCreationStrategy
    ) {
        val newFile = localFileManager.createPdfFile(path, null, creationStrategy)
        newFile shouldNotBe null
        newFile?.exists() shouldBe true
    }

    //endregion CHECKING CREATE PDF WITH STORAGE


    //region CHECKING GET MD5

    @Test
    fun Success_get_md5_internal() {
        val localPath = "test.pdf"
        val fullPath = "$internalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_get_md5(fullPath)
    }

    @Test
    fun Success_get_md5_external() {
        val localPath = "test.pdf"
        val fullPath = "$externalPath/$localPath"

        localFileManager.createFile(fullPath)

        check_get_md5(fullPath)
    }

    @Test
    fun Success_get_md5_with_strategy_internal() {
        val localPath = "test.pdf"

        localFileManager.createFile(localPath, creationStrategy = FileCreationStrategy.INTERNAL)

        check_get_md5(localPath, FileCreationStrategy.INTERNAL)
    }

    @Test
    fun Success_get_md5_with_strategy_external() {
        val localPath = "test.pdf"

        localFileManager.createFile(localPath, creationStrategy = FileCreationStrategy.EXTERNAL)

        check_get_md5(localPath, FileCreationStrategy.EXTERNAL)
    }

    @Test
    fun Success_get_md5_empty_path_null() {
        check_get_empty_md5("")
    }

    @Test
    fun Success_get_md5_empty_path_null_with_storage_internal() {
        check_get_empty_md5("", FileCreationStrategy.INTERNAL)
    }

    @Test
    fun Success_get_md5_empty_path_null_with_storage_external() {
        check_get_empty_md5("", FileCreationStrategy.EXTERNAL)
    }

    @Test
    fun Success_get_md5_file_not_exist_null() {
        val localPath = "test.pdf"
        val fullPath = "$internalPath/$localPath"

        check_get_empty_md5(fullPath)
    }

    @Test
    fun Success_get_md5_file_not_exist_null_with_storage_internal() {
        val localPath = "test.pdf"

        check_get_empty_md5(localPath, FileCreationStrategy.INTERNAL)
    }

    @Test
    fun Success_get_md5_file_not_exist_null_with_storage_extearnal() {
        val localPath = "test.pdf"

        check_get_empty_md5(localPath, FileCreationStrategy.EXTERNAL)
    }

    private fun check_get_md5(localPath: String, strategy: FileCreationStrategy? = null) {
        val md5 = if (strategy == null) {
            localFileManager.getFileMD5(localPath)
        } else {
            localFileManager.getFileMD5(localPath, strategy)
        }
        md5 shouldBe "D41D8CD98F00B204E9800998ECF8427E"
    }

    private fun check_get_empty_md5(localPath: String, strategy: FileCreationStrategy? = null) {
        val md5 = if (strategy == null) {
            localFileManager.getFileMD5(localPath)
        } else {
            localFileManager.getFileMD5(localPath, strategy)
        }
        md5 shouldBe null
    }

    //endregion CHECKING GET MD5
}
