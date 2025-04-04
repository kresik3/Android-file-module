package com.katemedia.android.lib.kate.file.utils.logging

import com.katemedia.android.lib.kate.file.utils.logging.data.DownloadStatus
import com.katemedia.android.lib.kate.file.utils.logging.data.FILE_BITMAP_ERROR
import com.katemedia.android.lib.kate.file.utils.logging.data.FILE_CREATION_ERROR
import com.katemedia.android.lib.kate.logging.logging.delegator.type.LogType
import com.katemedia.android.lib.kate.logging.logging.factory.LoggingFactory
import com.katemedia.android.lib.kate.logging.model.EventInfo
import java.lang.Exception


private const val TAG = "FILE_LIB"

internal class LogUtils {

    companion object {

        @JvmStatic
        fun logCreateFile(path: String) {
            logE("$path $FILE_CREATION_ERROR")
        }

        @JvmStatic
        fun logGetBitmap(path: String) {
            logE("$path $FILE_BITMAP_ERROR")
        }

        @JvmStatic
        fun logE(message: String) {
            log(LogType.ERROR, message)
        }

        @JvmStatic
        fun logDownloadStatus(status: DownloadStatus, uri: String?) {
            LoggingFactory.getInstance().logCustomInfo(EventInfo(LogType.INFO, TAG, "File $uri download ${status.name}"))
        }

        @JvmStatic
        fun logStackTrace(exception: Exception, method: String) {
            LoggingFactory.getInstance().logStackTrace(exception, method)
        }

        @JvmStatic
        private fun log(type: LogType, message: String) {
            LoggingFactory.getInstance().logCustomInfo(EventInfo(type, TAG, message))
        }

    }

}