package com.dv.apps.komic.reader.feature.settings.folder

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dv.apps.komic.reader.domain.model.DocumentTree
import com.dv.apps.komic.reader.domain.file.DocumentTreeManager
import com.dv.apps.komic.reader.ext.collectInto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class State(
    val isLoading: Boolean = false,
    val noSelection: Boolean = false,
    val selectedFolders: List<DocumentTree> = emptyList()
) {
    fun copyWithSelectedFolders(
        selectedFolders: List<DocumentTree>
    ) = copy(selectedFolders = selectedFolders)
}

sealed interface Intent {
    data class OnFileTreeSelected(val uri: Uri?) : Intent
}

class FolderSourceSettingsSectionViewModel(
    private val documentTreeManager: DocumentTreeManager
) : ViewModel() {
    val state = MutableStateFlow(State())

    init {
        viewModelScope.launch {
            launch {
                documentTreeManager
                    .getDocumentTrees()
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
            documentTreeManager.addDocumentTree(uri.toString())
        }
    }
}