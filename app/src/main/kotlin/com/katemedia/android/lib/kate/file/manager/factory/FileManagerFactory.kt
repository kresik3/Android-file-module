package com.katemedia.android.lib.kate.file.manager.factory

import android.content.Context
import com.katemedia.android.lib.kate.file.manager.LocalFileManager
import com.katemedia.android.lib.kate.file.manager.RemoteFileManager
import com.katemedia.android.lib.kate.file.manager.interfaces.ILocalFileManager
import com.katemedia.android.lib.kate.file.manager.interfaces.IRemoteFileManager
import com.katemedia.android.lib.kate.file.manager.strategy.configuration.interfaces.IFileConfiguration

class FileManagerFactory private constructor() {

    companion object {

        @JvmStatic
        fun getLocalFileManager(context: Context): ILocalFileManager = LocalFileManager(context)

        @JvmStatic
        fun getRemoteFileManager(context: Context, config: IFileConfiguration): IRemoteFileManager =
            RemoteFileManager(context, config)

    }

}