package com.dv.apps.komic.reader.feature.shelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dv.apps.komic.reader.domain.model.PreviewTree
import com.dv.apps.komic.reader.domain.repository.SettingsManager
import com.dv.apps.komic.reader.domain.repository.VirtualFilesystem
import com.dv.apps.komic.reader.domain.usecase.GetPreviewTree
import com.dv.apps.komic.reader.ext.mapItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class State(
    val previewTrees: List<PreviewTree> = emptyList(),
    val verticalPreviewSpanSize: Int = 1,
    val horizontalPreviewSpanSize: Int = 1,
    val isLoading: Boolean = false
)

sealed interface Intent

class ShelfScreenViewModel(
    private val settingsManager: SettingsManager,
    private val virtualFilesystem: VirtualFilesystem,
    private val getPreviewTree: GetPreviewTree
) : ViewModel() {
    val state = settingsManager
        .getSettings().map { it.selectedFolders }
        .mapItems(virtualFilesystem::scanRecursively)
        .mapItems(getPreviewTree)
        .combine(settingsManager.getSettings()) { preview, settings ->
            State(
                preview,
                settings.verticalPreviewSpanSize,
                settings.horizontalPreviewSpanSize
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            State(isLoading = true)
        )

    fun handleIntent(intent: Intent) {

    }
}