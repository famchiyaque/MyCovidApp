package com.app.mycovidapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.mycovidapp.presentation.screens.main.MainScreen
import com.app.mycovidapp.presentation.screens.country.CountryScreen



sealed class Screen(
    val route: String,
) {
    object Main : Screen("main")

    object Country : Screen("country/{countryName}") {
        fun createRoute(countryName: String) = "country/$countryName"
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = modifier,
    ) {
        composable(route = Screen.Main.route) {
            MainScreen(navController = navController)
        }

        composable(
            route = Screen.Country.route,
            arguments = listOf(
                navArgument("countryName") { type = NavType.StringType }
            )
        ) {
            CountryScreen(navController = navController)
        }
    }
}
