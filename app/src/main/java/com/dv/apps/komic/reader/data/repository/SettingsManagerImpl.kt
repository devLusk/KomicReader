package com.dv.apps.komic.reader.data.repository

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.dv.apps.komic.reader.data.protobuf.settingsDatastore
import com.dv.apps.komic.reader.domain.repository.SettingsManager
import com.dv.apps.komic.reader.proto.Settings
import com.dv.apps.komic.reader.proto.copy
import kotlinx.coroutines.flow.map

class SettingsManagerImpl(
    private val context: Context,
) : SettingsManager {
    override suspend fun addSelectedFolder(path: String) {
        context.contentResolver.takePersistableUriPermission(
            path.toUri(),
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        context.settingsDatastore.updateData {
            it.copy { selectedFolders += path }
        }
    }

    override fun getSelectedFolders() = context
        .settingsDatastore
        .data
        .map(Settings::getSelectedFoldersList)
}