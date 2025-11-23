package com.dv.apps.komic.reader.domain.model

sealed interface FileTree {
    val path: String

    class Folder(
        override val path: String,
        val children: List<FileTree>
    ) : FileTree

    class File(
        override val path: String,
        val type: String
    ) : FileTree
}