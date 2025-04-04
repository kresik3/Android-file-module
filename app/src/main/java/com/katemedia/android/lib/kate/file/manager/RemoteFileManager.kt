package com.katemedia.android.lib.kate.file.manager

import android.content.Context
import com.katemedia.android.lib.kate.file.manager.interfaces.IRemoteFileManager
import com.katemedia.android.lib.kate.file.manager.strategy.RemoteFileStrategy
import com.katemedia.android.lib.kate.file.manager.strategy.configuration.interfaces.IFileConfiguration
import com.katemedia.android.lib.kate.file.manager.strategy.data.FileInfo
import com.katemedia.android.lib.kate.file.manager.strategy.data.callback.IDownloadProgress
import com.katemedia.android.lib.kate.file.manager.strategy.data.response.DownloadFileResponse
import com.katemedia.android.lib.kate.file.manager.strategy.interfaces.IRemoteFileStrategy
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy
import com.katemedia.android.lib.kate.file.utils.logging.LogUtils.Companion.logDownloadStatus
import com.katemedia.android.lib.kate.file.utils.logging.LogUtils.Companion.logStackTrace
import com.katemedia.android.lib.kate.file.utils.logging.data.DownloadStatus


internal class RemoteFileManager(context: Context, config: IFileConfiguration) :
    IRemoteFileManager {

    private val strategy: IRemoteFileStrategy = RemoteFileStrategy(context, config)

    override fun downloadFile(
        uri: String?,
        localPath: String,
        creationStrategy: FileCreationStrategy,
        callback: IDownloadProgress?
    ): DownloadFileResponse? {
        if (uri.isNullOrEmpty() || localPath.isEmpty()) return null
        return try {
            downloadFileWithException(uri, localPath, creationStrategy, callback)
        } catch (e: Exception) {
            downloadFileWithException(uri, localPath, creationStrategy, callback)
        }
    }

    private fun downloadFileWithException(
        uri: String,
        localPath: String,
        creationStrategy: FileCreationStrategy,
        callback: IDownloadProgress?
    ): DownloadFileResponse? {
        return try {
            logDownloadStatus(DownloadStatus.STARTED, uri)
            strategy.downloadFile(uri, localPath, creationStrategy, callback).also {
                logDownloadStatus(DownloadStatus.SUCCESSFUL, uri)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            logStackTrace(ex, "downloadFile")
            logDownloadStatus(DownloadStatus.FAILED, uri)
            throw ex
        }
    }

    override fun getRemoteFileInfo(uri: String?): FileInfo? {
        if (uri.isNullOrEmpty()) return null

        return try {
            getRemoteFileInfoWithException(uri)
        } catch (e: Exception) {
            e.printStackTrace()
            logStackTrace(e, "getRemoteFileInfo")

            getRemoteFileInfoWithException(uri)
        }
    }

    private fun getRemoteFileInfoWithException(uri: String): FileInfo? {
        return strategy.getRemoteFileInfo(uri)
    }

}