package com.example.metro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.metro.presentation.settings.SettingsScreen
import com.example.metro.presentation.splash.SplashScreen
import com.example.metro.ui.theme.MetroTheme
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {

    private val LOADING = "LOADING"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionDataStore = SessionDataStore(this)

        // Map the flow OUTSIDE of composition
        val emailFlow = sessionDataStore.loggedInEmail.map { it ?: "" }

        setContent {
            MetroTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val loggedInEmail by emailFlow
                        .collectAsState(initial = LOADING)

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        // Splash screen — always shown first
                        composable("splash") {
                            SplashScreen(
                                onSplashFinished = {
                                    val destination =
                                        if (loggedInEmail != LOADING && loggedInEmail.isNotBlank()) {
                                            "home"
                                        } else {
                                            "login"
                                        }
                                    navController.navigate(destination) {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Login screen
                        composable("login") {
                            LoginScreen(navController)
                        }

                        // Home screen
                        composable("home") {
                            HomeScreen(
                                onNavigateToSettings = {
                                    navController.navigate("settings")
                                }
                            )
                        }

                        // Settings screen
                        composable("settings") {
                            SettingsScreen(
                                onBack = { navController.popBackStack() },
                                onLoggedOut = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}