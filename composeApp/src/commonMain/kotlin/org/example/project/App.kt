package org.example.project

import WelcomeScreen
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.di.initKoin
import org.example.project.theme.MyApplicationTheme


@Composable
fun App() {
    initKoin()
    MyApplicationTheme {
        Navigator(WelcomeScreen())
    }
}

