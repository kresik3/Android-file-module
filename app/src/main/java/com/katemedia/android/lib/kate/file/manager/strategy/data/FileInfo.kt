package com.katemedia.android.lib.kate.file.manager.strategy.data

data class FileInfo(
    val contentLength: Long,
    val contentType: String,
    val lastModified: Long,
    val date: Long
)