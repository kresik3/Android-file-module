package com.katemedia.android.lib.kate.file.manager

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.katemedia.android.lib.kate.file.manager.interfaces.ILocalFileManager
import com.katemedia.android.lib.kate.file.manager.strategy.configuration.FileConfiguration
import com.katemedia.android.lib.kate.file.manager.interfaces.IRemoteFileManager
import com.katemedia.android.lib.kate.file.manager.strategy.RemoteFileStrategy
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.properties.Delegates


@RunWith(AndroidJUnit4::class)
class RemoteFileManagerTest {

    private lateinit var remoteFileManager: IRemoteFileManager
    private lateinit var localFileManager: ILocalFileManager
    private val remoteImageUri =
        "https://avatars.mds.yandex.net/get-pdb/2822363/d3ec6994-2a94-439d-a155-5acc1549ed50/s1200"

    private var internalPath: String by Delegates.notNull()
    private var externalPath: String by Delegates.notNull()

    @Before
    fun initializeDataSource() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val configuration =
            FileConfiguration(30000, 30000)

        remoteFileManager = RemoteFileManager(app, configuration)
        localFileManager = LocalFileManager(app)

        internalPath = app.filesDir.absolutePath
        externalPath = app.getExternalFilesDir(null)!!.absolutePath

        clearStorages()
    }

    private fun clearStorages() {
        localFileManager.deleteFile(internalPath)
        localFileManager.deleteFile(externalPath)
    }


    //region DOWNLOAD FILE

    @Test
    fun Success_download_null_uri_internally() {
        download(null, "", false, FileCreationStrategy.INTERNAL)
    }

    @Test
    fun Success_download_null_uri_externally() {
        download(null, "", false, FileCreationStrategy.EXTERNAL)
    }

    @Test
    fun Success_download_multi_path_internally() {
        val localPath = "images/menu/s1200.jpg"

        download("$internalPath/${localPath}", localPath, true, FileCreationStrategy.INTERNAL)
    }

    @Test
    fun Success_download_multi_path_externally() {
        val localPath = "images/menu/s1200.jpg"

        download("$externalPath/${localPath}", localPath, true, FileCreationStrategy.EXTERNAL)
    }

    @Test
    fun Success_download_single_path_internally() {
        val localPath = "s1200.jpg"

        download("$internalPath/${localPath}", localPath, true, FileCreationStrategy.INTERNAL)
    }

    @Test
    fun Success_download_single_path_externally() {
        val localPath = "s1200.jpg"

        download("$externalPath/${localPath}", localPath, true, FileCreationStrategy.EXTERNAL)
    }

    private fun download(
        fullPath: String?,
        localPath: String,
        isExist: Boolean,
        strategy: FileCreationStrategy
    ) {
        val downloadResponse =
            remoteFileManager.downloadFile(remoteImageUri, localPath, strategy)
        val file = File(downloadResponse?.localPath ?: "")
        file.exists() shouldBe isExist
        downloadResponse?.localPath shouldBe fullPath
    }

    //endregion DOWNLOAD FILE


    //region GET FILE INFO

    @Test
    fun Success_get_file_info_empty_uri() {
        val fileInfo = remoteFileManager.getRemoteFileInfo("")
        fileInfo shouldBe null
    }

    @Test
    fun Success_get_file_info() {
        val fileInfo = remoteFileManager.getRemoteFileInfo(remoteImageUri)
        fileInfo shouldNotBe null
    }

    //endregion GET FILE INFO

}
