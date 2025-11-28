package com.dv.apps.komic.reader.domain.filesystem

import com.dv.apps.komic.reader.platform.PlatformFile

sealed interface VirtualFile {
    val name: String

    data class Folder(
        val platformFile: PlatformFile = PlatformFile(),
        val children: List<VirtualFile> = emptyList()
    ) : VirtualFile {
        constructor(
            name: String,
            children: List<VirtualFile> = emptyList()
        ) : this(
            PlatformFile(name = name),
            children
        )

        override val name get() = platformFile.name
    }

    data class File(
        val platformFile: PlatformFile
    ) : VirtualFile {
        constructor(name: String) : this(PlatformFile(name = name))

        override val name get() = platformFile.name

        data class WithThumbnail(
            val virtualFile: File,
            val thumbnail: Thumbnail = Thumbnail()
        ) : VirtualFile {
            override val name get() = virtualFile.name
        }
    }

    data class Thumbnail(
        val id: Int = 0,
        val path: String = "",
        val width: Int = 0,
        val height: Int = 0,
        val quality: Int = 0
    )
}