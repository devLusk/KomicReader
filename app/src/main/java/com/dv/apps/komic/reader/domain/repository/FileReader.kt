package com.dv.apps.komic.reader.domain.repository

import com.dv.apps.komic.reader.domain.filesystem.VirtualFile
import com.dv.apps.komic.reader.platform.PlatformFile
import java.io.OutputStream

interface FileReader {

    fun open(virtualFile: VirtualFile.File): State?

    fun open(platformFile: PlatformFile): State?

    interface State : Iterator<Readable>, AutoCloseable

    interface Readable {
        suspend fun readTo(outputStream: OutputStream)
    }
}