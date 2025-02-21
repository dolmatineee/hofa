package com.example.hofa

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.hofa.ads.RewardedAdManager
import com.example.hofa.screens.MainScreen
import com.example.hofa.ui.theme.HofaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    private lateinit var rewardedAdManager: RewardedAdManager
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rewardedAdManager = RewardedAdManager(this)
        enableEdgeToEdge()
        setContent {
            HofaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) {  paddingValues ->
                    MainScreen(rewardedAdManager)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        rewardedAdManager.destroy()
    }
}

