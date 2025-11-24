package com.dv.apps.komic.reader.feature.shelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dv.apps.komic.reader.domain.model.KomicPreviewTree
import com.dv.apps.komic.reader.domain.repository.SettingsManager
import com.dv.apps.komic.reader.domain.usecase.GetKomicPreviewTree
import com.dv.apps.komic.reader.domain.usecase.ScanVirtualFilesystemRecursively
import com.dv.apps.komic.reader.ext.mapItems
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class State(
    val komicPreviewTrees: List<KomicPreviewTree> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface Intent

class ShelfScreenViewModel(
    private val settingsManager: SettingsManager,
    private val scanVirtualFilesystemRecursively: ScanVirtualFilesystemRecursively,
    private val getKomicPreviewTree: GetKomicPreviewTree
) : ViewModel() {
    val state = settingsManager
        .getSelectedFolders()
        .mapItems(scanVirtualFilesystemRecursively)
        .mapItems(getKomicPreviewTree)
        .map(::State)
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            State(isLoading = true)
        )

    fun handleIntent(intent: Intent) {

    }
}