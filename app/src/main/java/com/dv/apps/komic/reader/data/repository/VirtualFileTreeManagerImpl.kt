package com.dv.apps.komic.reader.data.repository

import com.dv.apps.komic.reader.domain.filesystem.tree.VirtualFileTree
import com.dv.apps.komic.reader.domain.filesystem.tree.VirtualFileTreeManager
import com.dv.apps.komic.reader.domain.model.Settings
import com.dv.apps.komic.reader.domain.repository.ThumbnailManager
import com.dv.apps.komic.reader.platform.PlatformFile
import com.dv.apps.komic.reader.platform.PlatformFileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class VirtualFileTreeManagerImpl(
    private val thumbnailManager: ThumbnailManager,
    private val platformFileManager: PlatformFileManager
) : VirtualFileTreeManager {
    override suspend fun buildTree(
        paths: List<String>,
        quality: Settings.Quality
    ): VirtualFileTree = withContext(Dispatchers.IO) {
        val folders = paths.mapNotNull(platformFileManager::get)
        VirtualFileTree.Folder(
            PlatformFile(),
            children = folders.map {
                async {
                    buildTree(it, quality)
                }
            }.awaitAll()
        )
    }

    private suspend fun buildTree(
        platformFile: PlatformFile,
        quality: Settings.Quality
    ): VirtualFileTree = withContext(Dispatchers.IO) {
        when (platformFile.type) {
            is PlatformFile.Type.File ->
                buildFile(platformFile, quality)

            PlatformFile.Type.Folder ->
                buildFolder(platformFile, quality)
        }
    }

    private suspend fun buildFile(
        platformFile: PlatformFile,
        quality: Settings.Quality
    ): VirtualFileTree {
        val virtualFileTree = VirtualFileTree.File(platformFile)
        val thumbnail = thumbnailManager.get(platformFile, quality) ?: return virtualFileTree

        return VirtualFileTree.File.WithThumbnail(
            virtualFileTree,
            thumbnail
        )
    }

    private suspend fun buildFolder(
        platformFile: PlatformFile,
        quality: Settings.Quality
    ): VirtualFileTree.Folder = withContext(Dispatchers.IO) {
        val children = platformFileManager
            .listFiles(platformFile)
            .map {
                async {
                    buildTree(it, quality)
                }
            }
        VirtualFileTree.Folder(
            platformFile,
            children.awaitAll()
        )
    }

    override fun count(
        virtualFileTree: VirtualFileTree
    ): Int = when (virtualFileTree) {
        is VirtualFileTree.File -> 1
        is VirtualFileTree.File.WithThumbnail -> 1
        is VirtualFileTree.Folder -> virtualFileTree.children.sumOf(::count)
    }
}