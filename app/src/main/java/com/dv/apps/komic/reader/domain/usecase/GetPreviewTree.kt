package com.dv.apps.komic.reader.domain.usecase

import com.dv.apps.komic.reader.domain.model.PreviewTree
import com.dv.apps.komic.reader.domain.repository.ThumbnailManager
import com.dv.apps.komic.reader.domain.model.VirtualFile

class GetPreviewTree(
    private val thumbnailManager: ThumbnailManager
) : suspend (VirtualFile) -> PreviewTree {
    override suspend fun invoke(
        virtualFile: VirtualFile
    ) = when (virtualFile) {
        is VirtualFile.File -> getPreviewTree(virtualFile)
        is VirtualFile.Folder -> getPreviewTree(virtualFile)
    }

    private suspend fun getPreviewTree(file: VirtualFile.File): PreviewTree {
        val image = thumbnailManager.getOrCache(file)
        return PreviewTree.Done(file.name, image)
    }

    private suspend fun getPreviewTree(file: VirtualFile.Folder): PreviewTree {
        val children = file.children.map { invoke(it) }
        return PreviewTree.Nested(file.name, children)
    }
}