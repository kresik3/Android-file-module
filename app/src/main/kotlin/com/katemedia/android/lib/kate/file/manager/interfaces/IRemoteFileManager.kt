package com.katemedia.android.lib.kate.file.manager.interfaces

import com.katemedia.android.lib.kate.file.manager.strategy.data.FileInfo
import com.katemedia.android.lib.kate.file.manager.strategy.data.callback.IDownloadProgress
import com.katemedia.android.lib.kate.file.manager.strategy.data.response.DownloadFileResponse
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy

interface IRemoteFileManager {

    /**
     * The method uploads a file from remote and stores ot to local path. The root path is created by {@link FileCreationStrategy}.
     * @param uri is a remote path to the file (Type: String?).
     * @param localPath is a local path for storing on a device (Type: String).
     * @param creationStrategy is a type of local internal or external storage {@link FileCreationStrategy}.
     * @return an instance of {@link DownloadFileResponse}. Can be null if something went wrong.
     */
    fun downloadFile(
        uri: String?,
        localPath: String,
        creationStrategy: FileCreationStrategy,
        callback: IDownloadProgress? = null
    ): DownloadFileResponse?

    /**
     * The method retrieve a file info from remote.
     * @param uri is a remote path to the file (Type: String?).
     * @return an instance of {@link FileInfo}. Can be null if something went wrong.
     */
    fun getRemoteFileInfo(uri: String?): FileInfo?

}