package org.api

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

class PropertiesFileStore {
    fun read(file: File): Properties {
        val props = Properties()
        if (!file.exists()) return props
        try {
            FileInputStream(file).use { input -> props.load(input) }
        } catch (_: Exception) {}
        return props
    }

    @Synchronized
    fun update(file: File, mutate: (Properties) -> Unit) {
        try {
            file.parentFile?.mkdirs()
            val props = read(file)
            mutate(props)
            FileOutputStream(file).use { output ->
                props.store(output, "API PM store")
            }
            file.setReadable(false, false)
            file.setReadable(true, true)
            file.setWritable(false, false)
            file.setWritable(true, true)
        } catch (_: Exception) {}
    }
}