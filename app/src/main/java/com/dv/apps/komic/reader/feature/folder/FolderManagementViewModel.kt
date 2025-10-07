package com.dv.apps.komic.reader.feature.folder

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class State(
    val isLoading: Boolean = false,
    val noSelection: Boolean = false
)

sealed interface Intent {
    data class OnFileTreeSelected(val uri: Uri?): Intent
}

class FolderManagementViewModel(
    private val context: Context
) : ViewModel() {
    val state = MutableStateFlow(State())

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
        context.contentResolver.takePersistableUriPermission(
            uri,
            android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        val tree = DocumentFile.fromTreeUri(context, uri)
        val files = tree?.listFiles()
        println(files)
    }
}