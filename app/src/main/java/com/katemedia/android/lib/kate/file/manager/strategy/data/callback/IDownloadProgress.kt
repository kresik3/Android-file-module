package com.katemedia.android.lib.kate.file.manager.strategy.data.callback


interface IDownloadProgress {

    fun onDownloadProgress(progress: Long, limit: Long)

}