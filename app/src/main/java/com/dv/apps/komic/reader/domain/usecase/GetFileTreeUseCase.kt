package com.dv.apps.komic.reader.domain.usecase

import com.dv.apps.komic.reader.domain.repository.filesystem.FileSystemManager
import com.dv.apps.komic.reader.domain.repository.filesystem.FileTree

class GetFileTreeUseCase(
    private val fileSystemManager: FileSystemManager
) : suspend (String) -> FileTree {
    override suspend fun invoke(path: String) = fileSystemManager.getFileTree(path)
}