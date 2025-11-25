package com.dv.apps.komic.reader.domain.model

import java.io.File

sealed interface PreviewTree {
    data object Empty: PreviewTree

    data class Nested(
        val title: String,
        val children: List<PreviewTree>
    ): PreviewTree

    data class Done(
        val title: String,
        val preview: File? = null
    ): PreviewTree
}