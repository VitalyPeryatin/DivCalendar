package com.infinity_coder.divcalendar.presentation.export_sheet

import com.infinity_coder.divcalendar.presentation.App
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipManager {
    private const val BYTE_SIZE = 1024
    private const val BUFFER_SIZE = 6 * BYTE_SIZE

    fun zip(files: List<File>, zipName: String): File? {
        val zipFile = File(App.context.filesDir, zipName)
        if (zipFile.exists()) {
            zipFile.delete()
        }
        zipFile.createNewFile()

        val zipOutputStream = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile)))
        zipOutputStream.use { outputStream ->
            val data = ByteArray(BUFFER_SIZE)
            for (file in files) {
                val inputStream = FileInputStream(file)
                val origin = BufferedInputStream(inputStream, BUFFER_SIZE)
                origin.use {
                    val entry = ZipEntry(file.name)
                    outputStream.putNextEntry(entry)
                    var count: Int
                    while (origin.read(data, 0, BUFFER_SIZE).also { count = it } != -1) {
                        outputStream.write(data, 0, count)
                    }
                }
            }
        }
        return zipFile
    }
}