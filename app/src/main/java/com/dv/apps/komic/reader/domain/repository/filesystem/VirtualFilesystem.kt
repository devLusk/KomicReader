package com.dv.apps.komic.reader.domain.repository.filesystem

import java.io.InputStream

interface VirtualFilesystem {
    fun scanRecursively(path: String): VirtualFile

    fun getInputStream(virtualFile: VirtualFile.File): InputStream?
}