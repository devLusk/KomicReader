package com.dv.apps.komic.reader.feature.settings.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dv.apps.komic.reader.domain.repository.SettingsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class State(
    val verticalPreviewSpanSize: Int = 0,
    val horizontalPreviewSpanSize: Int = 0,
)

sealed interface Intent {
    data class OnVerticalPreviewSpanSizeChanged(val size: Int) : Intent
    data class OnHorizontalPreviewSpanSizeChanged(val size: Int) : Intent
}

class PreviewSettingsSectionViewModel(
    private val settingsManager: SettingsManager
) : ViewModel() {
    val state = settingsManager
        .getSettings()
        .map { (_, verticalPreviewSpanSize, horizontalPreviewSpanSize) ->
            State(
                verticalPreviewSpanSize,
                horizontalPreviewSpanSize
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            State()
        )

    fun handleIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.OnVerticalPreviewSpanSizeChanged ->
                settingsManager.setVerticalPreviewSpanSize(intent.size)

            is Intent.OnHorizontalPreviewSpanSizeChanged ->
                settingsManager.setHorizontalPreviewSpanSize(intent.size)
        }
    }
}