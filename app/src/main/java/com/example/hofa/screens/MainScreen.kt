package com.example.hofa.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.hofa.ads.RewardedAdManager
import com.example.hofa.navigation.AppNavGraph
import com.example.hofa.navigation.Screen
import com.example.hofa.navigation.rememberNavigationState

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen (
    rewardedAdManager: RewardedAdManager
) {

    val navigationState = rememberNavigationState()
    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

    fun showAdAndNavigate() {
        rewardedAdManager.showAd {
            navigationState.navHostController.navigate(Screen.Seance.route)
        }
    }

    Scaffold(
        modifier = Modifier.padding(/*paddingValues*/),
        containerColor = MaterialTheme.colorScheme.background,
    ) {  paddingValues ->

        AppNavGraph(
            navHostController = navigationState.navHostController,
            homeScreenContent = {
                HomeScreen(
                    onSettingsClickListener = {
                        navigationState.navHostController.navigate(Screen.Settings.route)
                    },
                    onStatisticsClickListener = {
                        navigationState.navHostController.navigate(Screen.Statistics.route)
                    },
                    onSeanceClickListener = {
                        showAdAndNavigate()
                    }
                )
            },
            settingsScreenContent = { settings ->
                SettingsScreen(
                    settings = settings,
                    onBackPressed = {
                        navigationState.navHostController.popBackStack()
                    }
                )
            },
            statisticsScreenContent = {
                StatisticsScreen(
                    onBackPressed = {
                        navigationState.navHostController.popBackStack()
                    }
                )
            },
            seanceScreenContent = { settings ->
                MeditationScreen(
                    settings = settings,
                    onExitClick =  {
                        navigationState.navHostController.popBackStack()
                    }
                )
            }
        )
    }
}