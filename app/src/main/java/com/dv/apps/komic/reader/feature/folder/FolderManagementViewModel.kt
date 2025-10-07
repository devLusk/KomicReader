package com.dv.apps.komic.reader.feature.folder

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

data class State(
    val isLoading: Boolean = false
)

sealed interface Intent
data class OnFileTreeSelected(val uri: Uri?): Intent

class FolderManagementViewModel() : ViewModel() {
    val state = MutableStateFlow(State())

    fun handleIntent(intent: Intent) {
        when (intent) {
            is OnFileTreeSelected -> onFileTreeSelected(intent.uri)
        }
    }

    private fun onFileTreeSelected(uri: Uri?) {

    }
}