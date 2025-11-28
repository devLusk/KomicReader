package com.dv.apps.komic.reader.data.repository

import com.dv.apps.komic.reader.domain.filesystem.VirtualFile
import com.dv.apps.komic.reader.domain.filesystem.VirtualFileSystem
import com.dv.apps.komic.reader.domain.model.Settings
import com.dv.apps.komic.reader.domain.repository.ThumbnailManager
import com.dv.apps.komic.reader.platform.PlatformFile
import com.dv.apps.komic.reader.platform.PlatformFileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class VirtualFileSystemImpl(
    private val thumbnailManager: ThumbnailManager,
    private val platformFileManager: PlatformFileManager
) : VirtualFileSystem {
    override suspend fun buildTree(
        paths: List<String>,
        quality: Settings.Quality
    ): VirtualFile = withContext(Dispatchers.IO) {
        val folders = paths.mapNotNull(platformFileManager::get)
        VirtualFile.Folder(
            name = "/",
            path = "/",
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
    ): VirtualFile = withContext(Dispatchers.IO) {
        when (platformFile.type) {
            PlatformFile.Type.FILE ->
                buildFile(platformFile, quality)

            PlatformFile.Type.FOLDER ->
                buildFolder(platformFile, quality)
        }
    }

    private suspend fun buildFile(
        platformFile: PlatformFile,
        quality: Settings.Quality
    ): VirtualFile {
        val virtualFile = VirtualFile.File(
            platformFile.name,
            platformFile.descriptor,
            platformFile.mimeType
        )
        val thumbnail = thumbnailManager.get(platformFile, quality) ?: return virtualFile

        return VirtualFile.File.WithThumbnail(
            virtualFile,
            thumbnail
        )
    }

    private suspend fun buildFolder(
        platformFile: PlatformFile,
        quality: Settings.Quality
    ): VirtualFile.Folder = withContext(Dispatchers.IO) {
        val children = platformFileManager
            .listFiles(platformFile)
            .map {
                async {
                    buildTree(it, quality)
                }
            }
        VirtualFile.Folder(
            platformFile.name,
            platformFile.descriptor,
            children.awaitAll()
        )
    }

    override fun count(
        virtualFile: VirtualFile
    ): Int = when (virtualFile) {
        is VirtualFile.File -> 1
        is VirtualFile.File.WithThumbnail -> 1
        is VirtualFile.Folder -> virtualFile.children.sumOf(::count)
    }

    override suspend fun buildSuspendedTree(
        quality: Settings.Quality,
        paths: List<String>,
        suspend: suspend () -> Unit
    ): Flow<VirtualFile> = flow {
        val platformFiles = paths.mapNotNull(platformFileManager::get)

        platformFiles.forEach {
            buildSuspendedTree(
                it,
                quality,
                suspend
            )
        }
    }

    private suspend fun FlowCollector<VirtualFile>.buildSuspendedTree(
        platformFile: PlatformFile,
        quality: Settings.Quality,
        suspend: suspend () -> Unit
    ) {
        when (platformFile.type) {
            PlatformFile.Type.FILE -> {
                suspend()

                val file = buildFile(
                    platformFile,
                    quality
                )

                emit(file)
            }

            PlatformFile.Type.FOLDER -> {
                val folder = VirtualFile.Folder(
                    platformFile.name,
                    platformFile.descriptor,
                    emptyList()
                )

                emit(folder)

                platformFileManager
                    .listFiles(platformFile)
                    .forEach { platformFile ->
                        buildSuspendedTree(platformFile, quality, suspend)
                    }
            }
        }
    }
}