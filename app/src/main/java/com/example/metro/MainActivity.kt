package com.example.metro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.metro.presentation.home.HomeScreen
import com.example.metro.presentation.settings.SettingsScreen
import com.example.metro.presentation.splash.SplashScreen
import com.example.metro.ui.theme.MetroTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MetroTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        // Splash screen — always shown first
                        composable("splash") {
                            SplashScreen(
                                onSplashFinished = {
                                    navController.navigate("home") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
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
                                    // No login screen — just go back to home
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}