package com.app.devRuni.lockwallpaper.util

import java.io.FileInputStream
import java.io.InputStream

object FileUtils {

    fun openInput(path: String?): InputStream {
        return FileInputStream(path)
    }
}