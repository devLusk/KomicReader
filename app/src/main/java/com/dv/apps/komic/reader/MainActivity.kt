package com.dv.apps.komic.reader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.dv.apps.komic.reader.ui.theme.KomicReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KomicReaderTheme {
                MainScreen()
            }
        }
    }
}

enum class Destination {
    SONGS,
    FOLDER
}

@Composable
fun MainScreen() {
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
        NavDisplay(
            backStack = navigationStack,
            entryProvider = entryProvider {
                entry<Destination> {
                    when (it) {
                        Destination.SONGS -> Text("Songs screen")
                        Destination.FOLDER -> Text("Folder screen")
                    }
                }
            }
        )
    }
}