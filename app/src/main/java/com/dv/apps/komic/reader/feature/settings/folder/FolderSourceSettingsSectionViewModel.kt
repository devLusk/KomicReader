package com.dv.apps.komic.reader.feature.settings.folder

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dv.apps.komic.reader.domain.folder.Folder
import com.dv.apps.komic.reader.domain.folder.FolderManager
import com.dv.apps.komic.reader.ext.collectInto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class State(
    val isLoading: Boolean = false,
    val noSelection: Boolean = false,
    val selectedFolders: List<Folder> = emptyList()
) {
    fun copyWithSelectedFolders(
        selectedFolders: List<Folder>
    ) = copy(selectedFolders = selectedFolders)
}

sealed interface Intent {
    data class OnFileTreeSelected(val uri: Uri?): Intent
}

class FolderSourceSettingsSectionViewModel(
    private val folderManager: FolderManager
) : ViewModel() {
    val state = MutableStateFlow(State())

    init {
        viewModelScope.launch {
            launch {
                folderManager
                    .getFolders()
                    .collectInto(state, State::copyWithSelectedFolders)
            }
        }
    }

    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.OnFileTreeSelected -> onFileTreeSelected(intent.uri)
        }
    }

    private fun onFileTreeSelected(uri: Uri?) {
        if (uri == null) {
            state.update { it.copy(noSelection = true) }
            return
        }
        viewModelScope.launch {
            folderManager.saveFolder(uri.toString())
        }
        //val tree = DocumentFile.fromTreeUri(context, uri)
        //val files = tree?.listFiles()
    }
}