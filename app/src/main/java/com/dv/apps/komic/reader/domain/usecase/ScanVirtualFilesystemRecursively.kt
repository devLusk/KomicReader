package com.dv.apps.komic.reader.domain.usecase

import com.dv.apps.komic.reader.domain.repository.filesystem.VirtualFilesystem
import com.dv.apps.komic.reader.domain.repository.filesystem.VirtualFile

class ScanVirtualFilesystemRecursively(
    private val virtualFilesystem: VirtualFilesystem
) : suspend (String) -> VirtualFile {
    override suspend fun invoke(path: String) = virtualFilesystem.scanRecursively(path)
}