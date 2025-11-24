package com.dv.apps.komic.reader.domain.model

import java.io.File

sealed interface KomicPreviewTree {
    data object Empty: KomicPreviewTree

    data class Nested(
        val title: String,
        val children: List<KomicPreviewTree>
    ): KomicPreviewTree

    data class Done(
        val title: String,
        val preview: File? = null
    ): KomicPreviewTree
}