package com.dv.apps.komic.reader.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsManager {
    suspend fun addSelectedFolder(path: String)

    fun getSelectedFolders(): Flow<List<String>>
}