package com.dv.apps.komic.reader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dv.apps.komic.reader.feature.navigation.MainNavigation
import com.dv.apps.komic.reader.ui.theme.KomicReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KomicReaderTheme {
                MainNavigation()
            }
        }
    }
}