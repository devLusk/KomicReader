package com.dv.apps.komic.reader.feature.shelf

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dv.apps.komic.reader.R
import com.dv.apps.komic.reader.domain.model.KomicPreviewTree
import com.dv.apps.komic.reader.feature.common.KomicPreview
import com.dv.apps.komic.reader.ui.theme.KomicReaderTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShelfScreen() {
    if (LocalInspectionMode.current) {
        ShelfScreen(State())
    } else {
        val vm = koinViewModel<ShelfScreenViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()
        ShelfScreen(state, vm::handleIntent)
    }
}

@Composable
fun ShelfScreen(
    state: State,
    dispatchIntent: (Intent) -> Unit = {}
) {
    Column(
        Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Text(
            stringResource(R.string.shelf_screen_title),
            style = MaterialTheme.typography.titleLarge
        )

        state.komicPreviewTrees.forEach {
            ShelfFileTree(it)
        }
    }
}

@Composable
fun ShelfFileTree(komicPreviewTree: KomicPreviewTree) {
    when (komicPreviewTree) {
        KomicPreviewTree.Empty -> Text("Something wrong happened")
        is KomicPreviewTree.Done -> {
            KomicPreview(
                modifier = Modifier.padding(8.dp),
                title = komicPreviewTree.title,
                preview = komicPreviewTree.preview
            )
        }

        is KomicPreviewTree.Nested -> {
            Column {
                Text(
                    komicPreviewTree.title,
                    style = MaterialTheme.typography.titleLarge
                )

                LazyVerticalGrid(GridCells.Fixed(4)) {
                    items(komicPreviewTree.children) {
                        ShelfFileTree(it)
                    }
                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun ShelfScreenPreview() {
    KomicReaderTheme {
        ShelfScreen(
            State(
                komicPreviewTrees = listOf(
                    KomicPreviewTree.Nested(
                        "POKEMON",
                        listOf(
                            KomicPreviewTree.Done(
                                "A"
                            ),
                            KomicPreviewTree.Done(
                                "B"
                            ), KomicPreviewTree.Done(
                                "C"
                            )
                        )
                    ),
                    KomicPreviewTree.Nested(
                        "DIGIMON",
                        listOf(
                            KomicPreviewTree.Done(
                                "A"
                            ),
                            KomicPreviewTree.Done(
                                "B"
                            ), KomicPreviewTree.Done(
                                "C"
                            )
                        )
                    ),
                    KomicPreviewTree.Nested(
                        "CHAVEZ",
                        listOf(
                            KomicPreviewTree.Done(
                                "A"
                            ),
                            KomicPreviewTree.Done(
                                "B"
                            ), KomicPreviewTree.Done(
                                "C"
                            )
                        )
                    ),
                    KomicPreviewTree.Nested(
                        "CHAVEZ",
                        listOf(
                            KomicPreviewTree.Done(
                                "A"
                            ),
                            KomicPreviewTree.Done(
                                "B"
                            ), KomicPreviewTree.Done(
                                "C"
                            )
                        )
                    )
                )
            )
        )
    }
}