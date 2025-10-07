package com.dv.apps.komic.reader.feature.folder

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.dv.apps.komic.reader.R
import com.dv.apps.komic.reader.ext.dispatchFor
import com.dv.apps.komic.reader.ui.theme.KomicReaderTheme

@Composable
fun FolderManagementScreen(
    state: State,
    dispatchIntent: (Intent) -> Unit
) {
    val openDocumentTreeLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree(),
        dispatchIntent dispatchFor Intent::OnFileTreeSelected
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                openDocumentTreeLauncher.launch(null)
            }) {
                Icon(
                    painterResource(R.drawable.ic_android),
                    contentDescription = null
                )
            }
        }
    ) {
        Column {
            Text("Open documents tree", modifier = Modifier.padding(it))

            if (state.noSelection) {
                Text("You selected no folder. Please, try again")
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun FolderScreenPreview() {
    KomicReaderTheme {
        FolderManagementScreen(State()) {}
    }
}