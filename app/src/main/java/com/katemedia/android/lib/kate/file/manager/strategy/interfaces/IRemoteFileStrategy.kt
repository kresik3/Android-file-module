package com.katemedia.android.lib.kate.file.manager.strategy.interfaces

import com.katemedia.android.lib.kate.file.manager.strategy.data.FileInfo
import com.katemedia.android.lib.kate.file.manager.strategy.data.callback.IDownloadProgress
import com.katemedia.android.lib.kate.file.manager.strategy.data.response.DownloadFileResponse
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy

interface IRemoteFileStrategy {

    fun downloadFile(
        uri: String,
        localPath: String,
        creationStrategy: FileCreationStrategy,
        callback: IDownloadProgress? = null
    ): DownloadFileResponse?

    fun getRemoteFileInfo(uri: String): FileInfo?
}