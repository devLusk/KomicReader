package com.dv.apps.komic.reader.feature.folder

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.documentfile.provider.DocumentFile
import com.dv.apps.komic.reader.R
import com.dv.apps.komic.reader.ui.theme.KomicReaderTheme

@Composable
fun FolderManagementScreen(
    state: State,
    dispatchIntent: (Intent) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            val tree = DocumentFile.fromTreeUri(context, uri)
            val files = tree?.listFiles()
            val names = files?.map {
                it.name
            }
            println(names)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                launcher.launch(null)
            }) {
                Icon(
                    painterResource(R.drawable.ic_android),
                    contentDescription = null
                )
            }
        }
    ) {
        Text("Open documents tree", modifier = Modifier.padding(it))
    }
}

@PreviewScreenSizes
@Composable
private fun FolderScreenPreview() {
    KomicReaderTheme {
        FolderManagementScreen(State()) {}
    }
}