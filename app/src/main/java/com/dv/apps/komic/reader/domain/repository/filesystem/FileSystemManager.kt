package com.dv.apps.komic.reader.domain.repository.filesystem

import java.io.InputStream

interface FileSystemManager {
    fun getFileTree(path: String): FileTree
    fun openStream(fileTree: FileTree.File): InputStream?
}