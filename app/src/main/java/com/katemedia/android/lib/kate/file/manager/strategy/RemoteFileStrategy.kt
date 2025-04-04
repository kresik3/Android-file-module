package com.katemedia.android.lib.kate.file.manager.strategy

import android.content.Context
import com.katemedia.android.lib.kate.file.manager.strategy.configuration.interfaces.IFileConfiguration
import com.katemedia.android.lib.kate.file.manager.strategy.base.FileStrategy
import com.katemedia.android.lib.kate.file.manager.strategy.data.FileInfo
import com.katemedia.android.lib.kate.file.manager.strategy.data.callback.IDownloadProgress
import com.katemedia.android.lib.kate.file.manager.strategy.data.response.DownloadFileResponse
import com.katemedia.android.lib.kate.file.manager.strategy.interfaces.IRemoteFileStrategy
import com.katemedia.android.lib.kate.file.model.FileCreationStrategy
import com.katemedia.android.lib.kate.file.utils.DateUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.net.HttpURLConnection.HTTP_MULT_CHOICE
import java.net.HttpURLConnection.HTTP_OK
import java.util.concurrent.TimeUnit


open class RemoteFileStrategy(appContext: Context, val config: IFileConfiguration) :
    FileStrategy(appContext), IRemoteFileStrategy {

    private val client: OkHttpClient by lazy { initClient() }

    override fun downloadFile(
        uri: String,
        localPath: String,
        creationStrategy: FileCreationStrategy,
        callback: IDownloadProgress?
    ): DownloadFileResponse? {
        val file = createFile(localPath, creationStrategy) ?: return null

        val fileInfo = makeCall(uri) { parseFile(it, file, callback) } ?: return null

        return DownloadFileResponse(file.absolutePath, fileInfo)
    }

    override fun getRemoteFileInfo(
        uri: String
    ): FileInfo? {
        return makeCall(uri) { parseInfo(it) }
    }

    private inline fun <T> makeCall(uri: String, action: (Response) -> T): T? {
        val request = Request.Builder().url(uri).build()

        return client.newBuilder().build().newCall(request).execute().use {
            val body = it.body
            val code = it.code

            if (code in HTTP_OK until HTTP_MULT_CHOICE && body != null) action(it) else null
        }
    }

    private fun parseFile(
        response: Response,
        file: File,
        callback: IDownloadProgress?
    ): FileInfo? {
        val body = response.body ?: return null

        val length = body.contentLength()

        body.byteStream().use { fileIn ->
            file.outputStream().use { fileOut ->
                var bytesCopied = 0L
                val buffer = ByteArray(8 * 1024)
                var bytes: Int

                while (fileIn.read(buffer).let { bytes = it; it != -1 }) {
                    bytesCopied += bytes
                    callback?.onDownloadProgress(bytesCopied, length)

                    fileOut.write(buffer, 0, bytes)
                }
            }
        }

        return parseInfo(response)
    }

    private fun parseInfo(response: Response): FileInfo = response.run {
        val lastModified = kotlin.runCatching { DateUtil.parseDate(headers["last-modified"])?.time }
        val date = kotlin.runCatching { DateUtil.parseDate(headers["date"])?.time }

        return FileInfo(
            body?.contentLength() ?: -1L,
            body?.contentType()?.type ?: "",
            lastModified.getOrNull() ?: -1L,
            date.getOrNull() ?: -1
        )
    }

    private fun initClient() = OkHttpClient().newBuilder()
        .connectTimeout(config.timeoutSocket, TimeUnit.MILLISECONDS)
        .readTimeout(config.timeoutConnection, TimeUnit.MILLISECONDS)
        .build()

}