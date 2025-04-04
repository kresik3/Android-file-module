package com.katemedia.android.lib.kate.file.manager.strategy.configuration

import com.katemedia.android.lib.kate.file.manager.strategy.configuration.interfaces.IFileConfiguration

class FileConfiguration(override val timeoutConnection: Long, override val timeoutSocket: Long) :
    IFileConfiguration