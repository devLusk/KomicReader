package com.dv.apps.komic.reader.feature.shelf

import android.content.res.Configuration
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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.dv.apps.komic.reader.R
import com.dv.apps.komic.reader.domain.model.PreviewTree
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
fun getSettingsSpan(state: State): Int? {
    val configuration = LocalConfiguration.current
    val isVertical = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    return if (isVertical) {
        state.verticalPreviewSpanSize
    } else {
        state.horizontalPreviewSpanSize
    }.takeIf { it > 0 }
}

@Composable
fun getDefaultSpan(): Int {
    val windowInfo = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)
    val windowSizeClass = windowInfo.windowSizeClass
    val horizontallyLarge = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )
    return if (horizontallyLarge) {
        4
    } else {
        2
    }
}

@Composable
fun ShelfScreen(
    state: State,
    dispatchIntent: (Intent) -> Unit = {}
) {
    val columns = getSettingsSpan(state) ?: getDefaultSpan()
    val span = GridItemSpan(columns)

    LazyVerticalGrid(
        GridCells.Fixed(columns)
    ) {
        item { Spacer(Modifier.windowInsetsPadding(WindowInsets.statusBars)) }

        item(span = { span }) {
            Text(
                stringResource(R.string.shelf_screen_title),
                style = MaterialTheme.typography.titleLarge
            )
        }

        for (item in state.previewTrees) {
            ShelfPreviewTree(span, item)
        }

        item { Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars)) }
    }
}

fun LazyGridScope.ShelfPreviewTree(
    span: GridItemSpan,
    previewTree: PreviewTree
) {
    when (previewTree) {
        is PreviewTree.Done -> item {
            KomicPreview(
                modifier = Modifier.padding(8.dp),
                title = previewTree.title,
                preview = previewTree.preview
            )
        }

        is PreviewTree.More -> {
            item(span = { span }) {
                Spacer(Modifier.height(32.dp))
            }

            item(span = { span }) {
                Text(
                    previewTree.title,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            for (child in previewTree.children) {
                ShelfPreviewTree(span, child)
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
                previewTrees = listOf(
                    PreviewTree.More(
                        "POKEMON",
                        listOf(
                            PreviewTree.Done(
                                "A"
                            ),
                            PreviewTree.Done(
                                "B"
                            )
                        )
                    ),
                    PreviewTree.More(
                        "DIGIMON",
                        listOf(
                            PreviewTree.Done(
                                "A"
                            ),
                            PreviewTree.Done(
                                "B"
                            )
                        )
                    ),
                    PreviewTree.More(
                        "CROSSOVER",
                        listOf(
                            PreviewTree.Done(
                                "A"
                            ),
                            PreviewTree.More(
                                "DIGIMON",
                                listOf(
                                    PreviewTree.Done(
                                        "A"
                                    ),
                                    PreviewTree.Done(
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