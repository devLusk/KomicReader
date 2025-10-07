package com.dv.apps.komic.reader.feature.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.dv.apps.komic.reader.R
import com.dv.apps.komic.reader.feature.folder.FolderManagementScreen
import com.dv.apps.komic.reader.feature.folder.FolderManagementViewModel
import com.dv.apps.komic.reader.ui.theme.KomicReaderTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavigation() {
    val navigationStack = remember { mutableStateListOf(Destination.SONGS) }
    val navigationSuiteState = rememberNavigationSuiteScaffoldState()

    NavigationSuiteScaffold(
        state = navigationSuiteState,
        navigationSuiteItems = {
            Destination.entries.forEachIndexed { index, destination ->
                item(
                    selected = navigationStack.first().ordinal == index,
                    onClick = {
                        navigationStack.clear()
                        navigationStack.add(destination)
                    },
                    icon = {
                        Icon(
                            painterResource(R.drawable.ic_android),
                            contentDescription = null
                        )
                    },
                    label = { Text(destination.name) }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        if (LocalInspectionMode.current) {
            Scaffold {
                Text("Hello World", Modifier.padding(it))
            }
        } else {
            MainNavigator(navigationStack)
        }
    }
}

@Composable
fun MainNavigator(
    navigationStack: MutableList<out Any>
) {
    NavDisplay(
        backStack = navigationStack,
        entryProvider = entryProvider {
            entry<Destination> {
                when (it) {
                    Destination.SONGS -> Text("Songs screen")
                    Destination.FOLDER -> {
                        val vm = koinViewModel<FolderManagementViewModel>()
                        val state by vm.state.collectAsStateWithLifecycle()
                        FolderManagementScreen(state, vm::handleIntent)
                    }
                }
            }
        }
    )
}

enum class Destination {
    SONGS,
    FOLDER
}

@PreviewScreenSizes
@Composable
private fun FolderScreenPreview() {
    KomicReaderTheme {
        MainNavigation()
    }
}