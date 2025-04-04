package com.katemedia.android.lib.kate.file.utils

import java.math.BigInteger
import java.security.MessageDigest


internal fun generateChecksum(digest: MessageDigest): String {
    val bytes: ByteArray = digest.digest()
    val output: String = BigInteger(1, bytes).toString(16)

    return String.format("%32S", output).replace(' ', '0')
}