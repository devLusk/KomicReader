package com.dv.apps.komic.reader.feature.shelf

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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

//TODO(fetch it dynamically from user's settings)
const val SPAN = 4

@Composable
fun ShelfScreen(
    state: State,
    dispatchIntent: (Intent) -> Unit = {}
) {
    LazyVerticalGrid(
        GridCells.Fixed(SPAN),
        Modifier.padding(8.dp)
    ) {
        item { Spacer(Modifier.windowInsetsPadding(WindowInsets.statusBars)) }

        item(
            span = { GridItemSpan(SPAN) }
        ) {
            Text(
                stringResource(R.string.shelf_screen_title),
                style = MaterialTheme.typography.titleLarge
            )
        }

        for (item in state.komicPreviewTrees) {
            ShelfFileTreePreview(item)
        }

        item { Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars)) }
    }
}

fun LazyGridScope.ShelfFileTreePreview(
    komicPreviewTree: KomicPreviewTree
) {
    when (komicPreviewTree) {
        KomicPreviewTree.Empty -> item {
            Text("Something wrong happened")
        }

        is KomicPreviewTree.Done -> item {
            KomicPreview(
                modifier = Modifier.padding(8.dp),
                title = komicPreviewTree.title,
                preview = komicPreviewTree.preview
            )
        }

        is KomicPreviewTree.Nested -> {
            item(
                span = { GridItemSpan(SPAN) }
            ) {
                Spacer(Modifier.height(32.dp))
            }
            item(
                span = { GridItemSpan(SPAN) }
            ) {
                Text(
                    komicPreviewTree.title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            for (child in komicPreviewTree.children) {
                ShelfFileTreePreview(child)
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
                            )
                        )
                    ),
                    KomicPreviewTree.Nested(
                        "CROSSOVER",
                        listOf(
                            KomicPreviewTree.Done(
                                "A"
                            ),
                            KomicPreviewTree.Nested(
                                "DIGIMON",
                                listOf(
                                    KomicPreviewTree.Done(
                                        "A"
                                    ),
                                    KomicPreviewTree.Done(
                                        "B"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }
}