package com.dv.apps.komic.reader.feature.shelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dv.apps.komic.reader.domain.repository.SettingsManager
import com.dv.apps.komic.reader.domain.repository.filesystem.FileTree
import com.dv.apps.komic.reader.domain.usecase.GetFileTreeUseCase
import com.dv.apps.komic.reader.ext.mapItems
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class State(
    val fileTrees: List<FileTree> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface Intent

class ShelfScreenViewModel(
    private val settingsManager: SettingsManager,
    private val getFileTreeUseCase: GetFileTreeUseCase
) : ViewModel() {
    val state = settingsManager
        .getSelectedFolders()
        .mapItems(getFileTreeUseCase)
        .map(::State)
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            State(isLoading = true)
        )

    fun handleIntent(intent: Intent) {

    }
}