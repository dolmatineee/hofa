package com.example.hofa.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hofa.data.entities.Settings
import com.example.hofa.data.entities.Statistics
import com.example.hofa.viewmodel.MeditationScreenViewModel
import com.example.hofa.viewmodel.SettingsScreenViewModel
import com.example.hofa.viewmodel.StatisticsScreenViewModel
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    homeScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable (Settings) -> Unit,
    statisticsScreenContent: @Composable () -> Unit,
    seanceScreenContent: @Composable (Settings) -> Unit,

) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {


        composable(Screen.Home.route) {
            homeScreenContent()
        }


        composable( Screen.Settings.route) {
            val viewModel: SettingsScreenViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                viewModel.loadSettings()
            }
            val settings by viewModel.settings.collectAsState(null)
            settings?.let { settingsScreenContent(it) }
        }

        composable(Screen.Statistics.route) {
           statisticsScreenContent()

        }



        composable(Screen.Seance.route) {
            val viewModel: MeditationScreenViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                viewModel.loadSettings()
            }
            val settings by viewModel.settings.collectAsState(null)
            settings?.let { seanceScreenContent(it) }
        }



    }
}
