package com.dv.apps.komic.reader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    var selectedDestination by remember { mutableIntStateOf(Destination.SONGS.ordinal) }
    val backStack = remember { mutableStateListOf<Any>(Destination.SONGS) }

    Scaffold { contentPadding ->
        Row {
            NavigationRail(
                Modifier.padding(contentPadding)
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationRailItem(
                        selected = selectedDestination == index,
                        onClick = {
                            backStack.add(destination)
                            selectedDestination = index
                        },
                        icon = { Icon(painterResource(R.drawable.ic_android), contentDescription = null) },
                        label = { Text(destination.name) }
                    )
                }
            }

            NavDisplay(
                modifier = Modifier.padding(contentPadding),
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
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
}