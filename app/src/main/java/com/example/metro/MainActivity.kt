package com.example.metro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.metro.data.local.SessionDataStore
import com.example.metro.presentation.home.HomeScreen
import com.example.metro.presentation.login.LoginScreen
import com.example.metro.ui.theme.MetroTheme
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {

    private val LOADING = "LOADING"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionDataStore = SessionDataStore(this)

        // Map the nullable flow to a sentinel: start with LOADING,
        // then emit the actual value (null = no session, non-blank = logged in)
        val sessionFlow = sessionDataStore.loggedInEmail

        setContent {
            MetroTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // initial = LOADING means we haven't read from disk yet
                    val loggedInEmail by sessionFlow.map { it ?: "" }
                        .collectAsState(initial = LOADING)

                    // While the DataStore is still reading, render nothing (avoids login flash)
                    if (loggedInEmail == LOADING) {
                        Box(Modifier.fillMaxSize()) // blank splash
                        return@Surface
                    }

                    val startDestination = if (loggedInEmail.isNotBlank()) "home" else "login"

                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("login") { LoginScreen(navController) }
                        composable("home")  { HomeScreen() }
                        // composable("signup") { SignUpScreen(navController) }
                    }
                }
            }
        }
    }
}