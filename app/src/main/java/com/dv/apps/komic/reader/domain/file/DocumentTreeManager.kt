package com.dv.apps.komic.reader.domain.file

import com.dv.apps.komic.reader.domain.model.DocumentTree
import kotlinx.coroutines.flow.Flow

interface DocumentTreeManager {
    suspend fun addDocumentTree(path: String)

    fun getDocumentTrees(): Flow<List<DocumentTree>>
}