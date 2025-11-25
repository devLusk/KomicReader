package com.dv.apps.komic.reader.domain.usecase

import com.dv.apps.komic.reader.domain.model.PreviewTree
import com.dv.apps.komic.reader.domain.repository.ThumbnailManager
import com.dv.apps.komic.reader.domain.model.VirtualFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class GetPreviewTree(
    private val thumbnailManager: ThumbnailManager
) : suspend (VirtualFile) -> PreviewTree {
    override suspend fun invoke(
        virtualFile: VirtualFile
    ) = getPreviewTree(virtualFile)

    private suspend fun getPreviewTree(
        virtualFile: VirtualFile
    ): PreviewTree = when (virtualFile) {
        is VirtualFile.File -> getPreviewTree(virtualFile)
        is VirtualFile.Folder -> getPreviewTree(virtualFile)
    }

    private suspend fun getPreviewTree(file: VirtualFile.File): PreviewTree {
        val image = thumbnailManager.getOrCache(file)
        return PreviewTree.Done(file.name, image)
    }

    private suspend fun getPreviewTree(
        file: VirtualFile.Folder
    ) = withContext(Dispatchers.Default) {
        val children = file.children.map {
            async { getPreviewTree(it) }
        }
        PreviewTree.More(file.name, children.awaitAll())
    }
}