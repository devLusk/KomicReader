package com.dv.apps.komic.reader.data.repository

import android.content.Context
import com.dv.apps.komic.reader.domain.repository.ThumbnailManager
import com.dv.apps.komic.reader.domain.repository.filesystem.VirtualFilesystem
import com.dv.apps.komic.reader.domain.repository.filesystem.VirtualFile
import org.koin.ext.clearQuotes
import java.io.File
import java.util.zip.ZipInputStream

class ThumbnailManagerImpl(
    private val context: Context,
    private val virtualFilesystem: VirtualFilesystem
) : ThumbnailManager {
    override suspend fun getOrCache(
        virtualFile: VirtualFile.File
    ): File? {
        val thumbnailDir = context
            .getExternalFilesDir("thumbnail")
            ?.also(File::mkdirs)
        val thumbnailFile = File(thumbnailDir, virtualFile.name)

        if (thumbnailFile.exists() && thumbnailFile.length() > 0) {
            return thumbnailFile
        }

        thumbnailFile.delete()
        if (!thumbnailFile.createNewFile()) return null

        virtualFilesystem.getInputStream(virtualFile).run(::ZipInputStream).use { zip ->
            if (zip.nextEntry == null) return null
            thumbnailFile.outputStream().use(zip::copyTo)
        }

        return thumbnailFile
    }

    override suspend fun getOrCache(virtualFile: VirtualFile.Folder): File? {
        return null
    }
}