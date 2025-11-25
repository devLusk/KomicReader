package com.dv.apps.komic.reader.domain.model

data class Settings(
    val selectedFolders: List<String>,
    val verticalPreviewSpanSize: Int,
    val horizontalPreviewSpanSize: Int,
)