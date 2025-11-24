package com.dv.apps.komic.reader.domain.repository.filesystem

sealed interface VirtualFile {
    val name: String
    val path: String

    data object Empty : VirtualFile {
        override val name = ""
        override val path = ""
    }

    class Folder(
        override val name: String,
        override val path: String,
        val children: List<VirtualFile>
    ) : VirtualFile

    class File(
        override val name: String,
        override val path: String,
        val type: String
    ) : VirtualFile
}