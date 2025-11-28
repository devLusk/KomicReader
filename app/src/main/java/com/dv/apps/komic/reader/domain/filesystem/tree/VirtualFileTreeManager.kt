package com.dv.apps.komic.reader.domain.filesystem.tree

import com.dv.apps.komic.reader.domain.model.Settings

interface VirtualFileTreeManager {
    suspend fun buildTree(
        paths: List<String>,
        quality: Settings.Quality
    ): VirtualFileTree

    fun count(
        virtualFileTree: VirtualFileTree
    ): Int
}