package com.android.exemple.planapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.exemple.planapp.ui.screen.DetailCreateScreen
import com.android.exemple.planapp.ui.screen.DetailScreen
import com.android.exemple.planapp.ui.screen.InitScreen
import com.android.exemple.planapp.ui.screen.MainCreateScreen
import com.android.exemple.planapp.ui.screen.PropertyCreateScreen
import com.android.exemple.planapp.ui.screen.PropertyScreen
import com.android.exemple.planapp.ui.theme.PlanAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable(route = "home") {
                            InitScreen(navController = navController)
                        }
                        composable(route = "mainCreate") {
                            MainCreateScreen(navController = navController)
                        }
                        composable(
                            route = "detail"
                        ) {
                            DetailScreen(navController = navController)
                        }
                        composable(
                            route = "detailCreate"
                        ) {
                            DetailCreateScreen(navController = navController)
                        }
                        composable(
                            route = "property"
                        ) {
                            PropertyScreen(navController = navController)
                        }
                        composable(
                            route = "propertyCreate"
                        ) {
                            PropertyCreateScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}






