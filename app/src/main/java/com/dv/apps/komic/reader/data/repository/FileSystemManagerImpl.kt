package com.dv.apps.komic.reader.data.repository

import android.content.Context
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.dv.apps.komic.reader.domain.repository.filesystem.FileSystemManager
import com.dv.apps.komic.reader.domain.repository.filesystem.FileTree
import java.io.InputStream

class FileSystemManagerImpl(
    private val context: Context
) : FileSystemManager {
    override fun getFileTree(
        path: String
    ): FileTree {
        val uri = path.toUri()
        val documentFile = DocumentFile.fromTreeUri(context, uri) ?: return FileTree.Empty
        return buildFileTree(documentFile)
    }

    private fun buildFileTree(documentFile: DocumentFile): FileTree {
        return if (documentFile.isDirectory) {
            val children = documentFile.listFiles().map(::buildFileTree)
            FileTree.Folder(
                documentFile.name.orEmpty(),
                documentFile.uri.toString(),
                children
            )
        } else {
            FileTree.File(
                documentFile.name.orEmpty(),
                documentFile.uri.toString(),
                documentFile.type.orEmpty()
            )
        }
    }

    override fun openStream(fileTree: FileTree.File): InputStream? {
        val uri = fileTree.path.toUri()
        return context.contentResolver.openInputStream(uri)
    }
}